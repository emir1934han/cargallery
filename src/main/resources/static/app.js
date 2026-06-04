const AUTH = 'Basic ' + btoa('admin:admin123');
const HEADERS = { 'Authorization': AUTH, 'Content-Type': 'application/json' };

// Global Grafik Değişkenleri
let distributionChartObj = null;
let salesChartObj = null;
let globalCars = [];

document.addEventListener('DOMContentLoaded', () => {
    checkTheme();
    updateDashboardStats();
    loadCars();
    setupPhoneMask();
    setupSmartNumbers(); // Yeni: Akıllı KM ve Para maskelemesini başlat
});

// ==========================================
// GECE / GÜNDÜZ MODU (DARK THEME)
// ==========================================
function toggleTheme() {
    const html = document.documentElement;
    const isDark = html.getAttribute('data-bs-theme') === 'dark';
    const newTheme = isDark ? 'light' : 'dark';
    html.setAttribute('data-bs-theme', newTheme);
    localStorage.setItem('theme', newTheme);
    document.getElementById('themeIcon').className = isDark ? 'bi bi-moon-fill' : 'bi bi-sun-fill text-warning';
    updateDashboardStats();
}

function checkTheme() {
    const savedTheme = localStorage.getItem('theme') || 'light';
    document.documentElement.setAttribute('data-bs-theme', savedTheme);
    document.getElementById('themeIcon').className = savedTheme === 'dark' ? 'bi bi-sun-fill text-warning' : 'bi bi-moon-fill';
}

// ==========================================
// MENÜ VE İSTATİSTİKLER
// ==========================================
function switchTab(tabId) {
    document.querySelectorAll('.view-section').forEach(el => el.classList.remove('active'));
    document.querySelectorAll('.nav-link').forEach(el => el.classList.remove('active'));
    document.getElementById(tabId).classList.add('active');
    event.target.classList.add('active');
    updateDashboardStats();
    if(tabId === 'cars') loadCars();
    if(tabId === 'customers') loadCustomers();
    if(tabId === 'sales') loadSales();
}

function updateDashboardStats() {
    Promise.all([
        fetch('/cars', { headers: HEADERS }).then(res => res.json()),
        fetch('/customers', { headers: HEADERS }).then(res => res.json()),
        fetch('/sales', { headers: HEADERS }).then(res => res.json())
    ]).then(([cars, customers, sales]) => {
        globalCars = cars;
        document.getElementById('stat-total-cars').innerText = cars.length;
        document.getElementById('stat-available-cars').innerText = cars.filter(c => c.available).length;
        document.getElementById('stat-customers').innerText = customers.length;
        document.getElementById('stat-sales').innerText = sales.length;
        drawCharts(sales);
    });
}

function updatePieChart() {
    if (!globalCars.length) return;
    const filter = document.getElementById('chartFilter').value;
    const counts = {};
    globalCars.forEach(c => { const val = c[filter] ? c[filter] : 'Belirtilmemiş'; counts[val] = (counts[val] || 0) + 1; });
    const isDark = document.documentElement.getAttribute('data-bs-theme') === 'dark';
    const textColor = isDark ? '#cbd5e1' : '#475569';
    if (distributionChartObj) distributionChartObj.destroy();
    distributionChartObj = new Chart(document.getElementById('distributionChart').getContext('2d'), {
        type: 'doughnut',
        data: { labels: Object.keys(counts), datasets: [{ data: Object.values(counts), backgroundColor: ['#3b82f6', '#10b981', '#f59e0b', '#8b5cf6', '#ef4444', '#64748b', '#ec4899'] }] },
        options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { labels: { color: textColor } } } }
    });
}

function drawCharts(sales) {
    updatePieChart();
    const isDark = document.documentElement.getAttribute('data-bs-theme') === 'dark';
    const textColor = isDark ? '#cbd5e1' : '#475569';
    const lastSales = sales.slice(-5);
    const saleLabels = lastSales.map(s => s.car.brand);
    const saleData = lastSales.map(s => s.price);
    if (salesChartObj) salesChartObj.destroy();
    salesChartObj = new Chart(document.getElementById('salesChart').getContext('2d'), {
        type: 'bar',
        data: { labels: saleLabels, datasets: [{ label: 'Satış Tutarı (TL)', data: saleData, backgroundColor: '#10b981', borderRadius: 5 }] },
        options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { labels: { color: textColor } } }, scales: { x: { ticks: { color: textColor } }, y: { ticks: { color: textColor } } } }
    });
}

// ==========================================
// YENİ: EXCEL'E AKTARMA (TABLE TO CSV)
// ==========================================
function exportToExcel(tableId, filename) {
    const table = document.getElementById(tableId);
    let csv = [];
    for (let i = 0; i < table.rows.length; i++) {
        let row = [], cols = table.rows[i].querySelectorAll("td, th");
        // Son sütunlar (İşlemler/Butonlar) excel'e girmesin diye "cols.length - 1" yaptık
        for (let j = 0; j < cols.length - 1; j++) {
            let data = cols[j].innerText.replace(/(\r\n|\n|\r)/gm, "").trim();
            row.push('"' + data + '"');
        }
        csv.push(row.join(";")); // Türkçe Excel için noktalı virgül
    }
    const csvFile = new Blob(["\uFEFF" + csv.join("\n")], { type: "text/csv;charset=utf-8;" }); // UTF-8 Bom for Türkçe karakterler
    const downloadLink = document.createElement("a");
    downloadLink.download = filename + ".csv";
    downloadLink.href = window.URL.createObjectURL(csvFile);
    downloadLink.style.display = "none";
    document.body.appendChild(downloadLink);
    downloadLink.click();
    document.body.removeChild(downloadLink);

    Swal.fire({ icon: 'success', title: 'İndirildi!', text: 'Excel raporu başarıyla indirildi.', timer: 1500, showConfirmButton: false });
}

// ==========================================
// YENİ: AKILLI SAYI MASKESİ (KM VE PARA İÇİN)
// ==========================================
function setupSmartNumbers() {
    document.querySelectorAll('.smart-number').forEach(input => {
        input.addEventListener('input', function(e) {
            // Sadece sayıları al
            let value = this.value.replace(/\D/g, '');
            // Sayı varsa Türk lirası/KM formatına (Noktalı) çevir
            if (value !== '') {
                value = parseInt(value, 10).toLocaleString('tr-TR');
            }
            this.value = value;
        });
    });
}

// Backend'e gönderirken noktaları geri silmemiz lazım! Yardımcı Fonksiyon:
function parseSmartNumber(val) {
    if (!val) return 0;
    return parseFloat(val.replace(/\./g, ''));
}

// ==========================================
// ARAÇLAR İŞLEMLERİ
// ==========================================
function searchCars() {
    const keyword = document.getElementById('liveSearch').value;
    const url = keyword.trim() === '' ? '/cars' : `/cars/search?brand=${keyword}`;
    fetch(url, { headers: HEADERS }).then(res => res.json()).then(cars => renderCars(cars));
}

function renderCars(cars) {
    const grid = document.getElementById('carGrid');
    if (cars.length === 0) { grid.innerHTML = '<div class="col-12 text-center text-muted my-5"><i class="bi bi-search display-4"></i><p class="mt-3">Sonuç bulunamadı.</p></div>'; return; }
    grid.innerHTML = cars.map(car => `
        <div class="col-md-6 col-lg-4">
            <div class="card h-100 ${!car.available ? 'opacity-75' : ''}">
                <div class="card-header-img position-relative">
                    <img src="/logos/${car.brand.toLowerCase()}.png" class="logo-img" onerror="this.src='/logos/default.png'">
                    <span class="position-absolute top-0 end-0 m-3 badge rounded-pill ${car.available ? 'bg-success' : 'bg-danger'} shadow-sm">
                        ${car.available ? '<i class="bi bi-check-circle me-1"></i> Satışta' : '<i class="bi bi-x-circle me-1"></i> Satıldı'}
                    </span>
                </div>
                <div class="card-body">
                    <h5 class="fw-bold mb-0 text-dark">${car.brand} ${car.series}</h5>
                    <p class="text-primary small fw-bold mb-3">${car.model} • ${car.year}</p>
                    <div class="row g-2 mb-2">
                        <div class="col-6"><div class="car-detail-item"><i class="bi bi-speedometer"></i> ${car.km ? car.km.toLocaleString('tr-TR') : '0'} KM</div></div>
                        <div class="col-6"><div class="car-detail-item"><i class="bi bi-fuel-pump"></i> ${car.fuelType || '-'}</div></div>
                        <div class="col-6"><div class="car-detail-item"><i class="bi bi-gear-wide-connected"></i> ${car.transmission ? car.transmission + ' Vites' : '-'}</div></div>
                        <div class="col-6"><div class="car-detail-item"><i class="bi bi-palette"></i> ${car.color || '-'}</div></div>
                        <div class="col-6"><div class="car-detail-item"><i class="bi bi-lightning-charge"></i> ${car.enginePower || '-'}</div></div>
                        <div class="col-6"><div class="car-detail-item"><i class="bi bi-funnel"></i> ${car.engineCapacity || '-'}</div></div>
                    </div>
                </div>
                <div class="card-footer bg-transparent border-top-0 d-flex justify-content-between p-3 pt-0">
                    <button class="btn btn-sm btn-outline-primary btn-rounded fw-semibold" onclick="prepareCarEdit('${encodeURIComponent(JSON.stringify(car))}')"><i class="bi bi-pencil-square"></i> Düzenle</button>
                    <button class="btn btn-sm btn-outline-danger btn-rounded fw-semibold" onclick="deleteData('/cars', ${car.id}, loadCars)"><i class="bi bi-trash"></i> Sil</button>
                </div>
            </div>
        </div>
    `).join('');
}
function loadCars() { fetch('/cars', { headers: HEADERS }).then(res => res.json()).then(cars => renderCars(cars)); }

// ==========================================
// FORMLAR (ARAÇ VE MÜŞTERİ)
// ==========================================
function prepareCarAdd() { document.getElementById('carForm').reset(); document.getElementById('carId').value = ''; document.getElementById('carSubmitBtn').innerText = 'Araç Ekle'; new bootstrap.Modal(document.getElementById('carModal')).show(); }

function prepareCarEdit(encodedCar) {
    const car = JSON.parse(decodeURIComponent(encodedCar));
    document.getElementById('carId').value = car.id;
    document.getElementById('carSubmitBtn').innerText = 'Değişiklikleri Kaydet';
    document.getElementById('c_brand').value = car.brand; document.getElementById('c_series').value = car.series; document.getElementById('c_model').value = car.model; document.getElementById('c_year').value = car.year; document.getElementById('c_fuelType').value = car.fuelType || ""; document.getElementById('c_transmission').value = car.transmission;
    document.getElementById('c_color').value = car.color; document.getElementById('c_bodyType').value = car.bodyType || "";
    document.getElementById('c_enginePower').value = car.enginePower ? car.enginePower.replace(' HP', '') : '';
    document.getElementById('c_engineCapacity').value = car.engineCapacity ? car.engineCapacity.replace(' CC', '') : '';

    // Akıllı KM formatını ekrana basarken uygula
    document.getElementById('c_km').value = car.km ? car.km.toLocaleString('tr-TR') : '';

    new bootstrap.Modal(document.getElementById('carModal')).show();
}

document.getElementById('carForm').addEventListener('submit', (e) => {
    e.preventDefault();
    const id = document.getElementById('carId').value;
    let powerInput = document.getElementById('c_enginePower').value; let capacityInput = document.getElementById('c_engineCapacity').value;
    const data = {
        brand: document.getElementById('c_brand').value, series: document.getElementById('c_series').value, model: document.getElementById('c_model').value, year: parseInt(document.getElementById('c_year').value), fuelType: document.getElementById('c_fuelType').value, transmission: document.getElementById('c_transmission').value,
        km: parseSmartNumber(document.getElementById('c_km').value), // Noktaları temizleyerek gönder
        color: document.getElementById('c_color').value, bodyType: document.getElementById('c_bodyType').value, enginePower: powerInput ? powerInput + ' HP' : '', engineCapacity: capacityInput ? capacityInput + ' CC' : '', available: true
    };
    saveData(id ? `/cars/${id}` : '/cars', id ? 'PUT' : 'POST', data, 'carModal', loadCars);
});

function setupPhoneMask() {
    const phoneInput = document.getElementById('cu_phone');
    phoneInput.addEventListener('focus', function() { if(this.value === '') this.value = '+90 '; });
    phoneInput.addEventListener('input', function(e) {
        let val = this.value.replace(/\D/g, ''); if (val.startsWith('90')) val = val.substring(2);
        let formatted = '+90 ';
        if (val.length > 0) formatted += '(' + val.substring(0, 3); if (val.length >= 4) formatted += ') ' + val.substring(3, 6); if (val.length >= 7) formatted += ' ' + val.substring(6, 8); if (val.length >= 9) formatted += ' ' + val.substring(8, 10);
        this.value = formatted;
    });
}

function loadCustomers() {
    fetch('/customers', { headers: HEADERS }).then(res => res.json()).then(customers => {
        document.getElementById('customerTableBody').innerHTML = customers.map(c => `
            <tr>
                <td class="ps-4 fw-bold opacity-75">#${c.id}</td><td class="fw-semibold">${c.firstName} ${c.lastName}</td><td class="font-monospace">${c.phone}</td><td>${c.email}</td>
                <td class="pe-4 text-end">
                    <button class="btn btn-sm btn-info btn-rounded text-white fw-bold me-1" onclick="showCustomerProfile(${c.id}, '${c.firstName} ${c.lastName}')"><i class="bi bi-person-lines-fill"></i> Profil</button>
                    <button class="btn btn-sm btn-outline-danger btn-rounded" onclick="deleteData('/customers', ${c.id}, loadCustomers)"><i class="bi bi-trash"></i></button>
                </td>
            </tr>
        `).join('');
    });
}
function prepareCustomerAdd() { document.getElementById('customerForm').reset(); document.getElementById('customerId').value = ''; document.getElementById('cu_phone').value = '+90 '; new bootstrap.Modal(document.getElementById('customerModal')).show(); }
document.getElementById('customerForm').addEventListener('submit', (e) => { e.preventDefault(); const data = { firstName: document.getElementById('cu_firstName').value, lastName: document.getElementById('cu_lastName').value, phone: document.getElementById('cu_phone').value, email: document.getElementById('cu_email').value }; saveData('/customers', 'POST', data, 'customerModal', loadCustomers); });

// ==========================================
// YENİ: CRM MÜŞTERİ PROFİLİ (Backend'deki Kullanılmayan Metot Devrede!)
// ==========================================
function showCustomerProfile(customerId, customerName) {
    document.getElementById('crmCustomerName').innerText = customerName;
    document.getElementById('crmSalesList').innerHTML = '<div class="text-center"><div class="spinner-border text-primary"></div></div>';

    // Backend'deki "getSalesByCustomer" endpointine (Türetilmiş Sorgu) istek atıyoruz!
    fetch(`/sales/customer/${customerId}`, { headers: HEADERS }).then(res => res.json()).then(sales => {
        let totalSpend = 0;
        if(sales.length === 0) {
            document.getElementById('crmSalesList').innerHTML = '<p class="text-muted text-center mt-3">Henüz bir araç satın almamış.</p>';
        } else {
            document.getElementById('crmSalesList').innerHTML = sales.map(s => {
                totalSpend += s.price;
                return `
                <div class="border rounded p-3 mb-2 bg-light">
                    <div class="d-flex justify-content-between align-items-center mb-2">
                        <span class="fw-bold text-dark"><i class="bi bi-car-front-fill text-primary me-1"></i> ${s.car.brand} ${s.car.series}</span>
                        <span class="badge bg-secondary">${s.saleDate}</span>
                    </div>
                    <div class="text-success fw-bold text-end">${s.price.toLocaleString('tr-TR')} ₺</div>
                </div>`;
            }).join('');
        }
        document.getElementById('crmTotalSpend').innerText = totalSpend.toLocaleString('tr-TR') + ' ₺';

        const offcanvas = new bootstrap.Offcanvas(document.getElementById('crmOffcanvas'));
        offcanvas.show();
    });
}

// ==========================================
// SATIŞLAR VE YAZDIRMA
// ==========================================
function loadSales() {
    fetch('/sales', { headers: HEADERS }).then(res => res.json()).then(sales => {
        document.getElementById('saleTableBody').innerHTML = sales.map(s => `
            <tr>
                <td class="ps-4 fw-bold opacity-75">#${s.id}</td><td class="fw-semibold text-primary">${s.car.brand} ${s.car.series}</td><td class="fw-semibold">${s.customer.firstName} ${s.customer.lastName}</td><td class="opacity-75">${s.saleDate}</td><td class="text-end text-success fw-bold font-monospace">${s.price.toLocaleString('tr-TR')} ₺</td>
                <td class="pe-4 text-end text-center">
                    <button class="btn btn-sm btn-dark btn-rounded shadow-sm" onclick="printInvoice('${encodeURIComponent(JSON.stringify(s))}')"><i class="bi bi-printer"></i></button>
                </td>
            </tr>
        `).join('');
    });
}
function prepareSaleAdd() {
    document.getElementById('saleForm').reset(); document.getElementById('s_date').valueAsDate = new Date();
    fetch('/cars', { headers: HEADERS }).then(res => res.json()).then(cars => { const availableCars = cars.filter(c => c.available === true); document.getElementById('s_carId').innerHTML = availableCars.length > 0 ? '<option value="" disabled selected>Araç Seçiniz...</option>' + availableCars.map(c => `<option value="${c.id}">${c.brand} ${c.series} (Yıl: ${c.year})</option>`).join('') : '<option value="">Satılacak uygun araç yok!</option>'; });
    fetch('/customers', { headers: HEADERS }).then(res => res.json()).then(customers => { document.getElementById('s_customerId').innerHTML = '<option value="" disabled selected>Müşteri Seçiniz...</option>' + customers.map(c => `<option value="${c.id}">${c.firstName} ${c.lastName}</option>`).join(''); });
    new bootstrap.Modal(document.getElementById('saleModal')).show();
}

document.getElementById('saleForm').addEventListener('submit', (e) => {
    e.preventDefault();
    const data = {
        carId: parseInt(document.getElementById('s_carId').value),
        customerId: parseInt(document.getElementById('s_customerId').value),
        saleDate: document.getElementById('s_date').value,
        price: parseSmartNumber(document.getElementById('s_price').value) // Noktaları temizleyerek gönder
    };
    saveData('/sales', 'POST', data, 'saleModal', loadSales);
});

function printInvoice(encodedSale) {
    const s = JSON.parse(decodeURIComponent(encodedSale));
    const win = window.open('', '_blank', 'width=800,height=600');
    win.document.write(`<html><head><title>Fatura - #${s.id}</title><style>body { font-family: 'Segoe UI', Arial, sans-serif; padding: 40px; color: #333; line-height: 1.6; } .header { text-align: center; border-bottom: 2px solid #3b82f6; padding-bottom: 20px; margin-bottom: 30px; } .logo { font-size: 28px; font-weight: 800; color: #0f172a; } .details { display: flex; justify-content: space-between; margin-bottom: 40px; } .box { border: 1px solid #e2e8f0; padding: 20px; border-radius: 8px; width: 45%; background: #f8fafc; } table { width: 100%; border-collapse: collapse; margin-bottom: 30px; } th { background: #0f172a; color: white; padding: 12px; text-align: left; } td { padding: 12px; border-bottom: 1px solid #e2e8f0; } .price { font-size: 24px; font-weight: bold; color: #10b981; text-align: right; }</style></head><body><div class="header"><div class="logo">🏎️ ÇALIŞKAN AUTO</div><p style="margin:5px 0; color:#64748b;">Kurumsal Araç Satış Sözleşmesi ve Fatura</p></div><div class="details"><div class="box"><b style="color:#0f172a;">Alıcı Bilgileri:</b><br><br>${s.customer.firstName} ${s.customer.lastName}<br>${s.customer.phone}<br>${s.customer.email}</div><div class="box"><b style="color:#0f172a;">Satış Bilgileri:</b><br><br><b>Fatura No:</b> #INV-${s.id}<br><b>İşlem Tarihi:</b> ${s.saleDate}</div></div><h3 style="color:#0f172a; margin-bottom:15px;">Araç Detayları</h3><table><tr><th>Marka/Seri</th><th>Model Yılı</th><th>Kilometre</th><th>Tutar</th></tr><tr><td><b>${s.car.brand}</b> ${s.car.series}</td><td>${s.car.year}</td><td>${s.car.km ? s.car.km.toLocaleString('tr-TR') : '0'} KM</td><td><b>${s.price.toLocaleString('tr-TR')} ₺</b></td></tr></table><div class="price">Genel Toplam: ${s.price.toLocaleString('tr-TR')} ₺</div><div style="margin-top: 60px; display:flex; justify-content: space-between; text-align:center;"><div style="width:40%; border-top:1px solid #333; padding-top:10px;">Alıcı İmza</div><div style="width:40%; border-top:1px solid #333; padding-top:10px;">Satıcı (ÇALIŞKAN AUTO) İmza</div></div><p style="margin-top: 50px; text-align: center; color: #94a3b8; font-size: 12px;">Bu belge sistem tarafından elektronik olarak üretilmiştir. Bizi tercih ettiğiniz için teşekkür ederiz.</p><script>setTimeout(() => { window.print(); }, 500); window.onafterprint = function(){ window.close(); }<\/script></body></html>`);
    win.document.close();
}

function saveData(url, method, data, modalId, callback) {
    fetch(url, { method: method, headers: HEADERS, body: JSON.stringify(data) }).then(res => {
        if(!res.ok) { Swal.fire({ icon: 'error', title: 'Hata!', text: 'Lütfen bilgileri kontrol edip tekrar deneyin.', confirmButtonColor: '#3b82f6' }); throw new Error('Kayıt Başarısız'); }
        bootstrap.Modal.getInstance(document.getElementById(modalId)).hide();
        Swal.fire({ icon: 'success', title: 'Başarılı!', text: 'Kayıt işlemi başarıyla tamamlandı.', timer: 1500, showConfirmButton: false });
        callback(); updateDashboardStats();
    });
}
function deleteData(url, id, callback) {
    Swal.fire({ title: 'Emin misiniz?', text: "Bu kaydı sildiğinizde geri alamazsınız!", icon: 'warning', showCancelButton: true, confirmButtonColor: '#d33', cancelButtonColor: '#6c757d', confirmButtonText: 'Evet, Sil!', cancelButtonText: 'İptal'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`${url}/${id}`, { method: 'DELETE', headers: HEADERS }).then(() => {
                Swal.fire({ icon: 'success', title: 'Silindi!', text: 'Kayıt başarıyla silindi.', timer: 1500, showConfirmButton: false });
                callback(); updateDashboardStats();
            });
        }
    });
}