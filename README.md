# 🚗 ÇALIŞKAN AUTO - Galeri ve Müşteri Yönetim Sistemi (Full-Stack)

![Java](https://img.shields.io/badge/Java-17-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-purple.svg)
![Chart.js](https://img.shields.io/badge/Chart.js-Interactive-blue.svg)

Bu proje, **Orta Düzey Programlama** dersi final projesi kapsamında geliştirilmiş, kurumsal düzeyde bir **Araba Galerisi Yönetim Sistemi** otomasyonudur. 

---

## ✨ Öne Çıkan Profesyonel Özellikler

### 🛡️ Güçlü Backend (API) Mimarisi
* **İlişkisel Veritabanı:** Araç, Müşteri ve Satış tabloları arasında `@ManyToOne` ilişkileri.
* **Derived Queries:** Spring Data JPA ile özel filtreleme ve arama metotları.
* **Global Exception Handling:** Hataların `@RestControllerAdvice` ile JSON formatında şık bir şekilde yakalanması.
* **Validasyon:** `@Valid` ve DTO'lar aracılığıyla gelen verilerin sıkı kontrolü.
* **Rol Tabanlı Güvenlik:** Spring Security ile Admin ve User yetkilendirmeleri.

### 💻 Modern Frontend ve UX (Kullanıcı Deneyimi)
* **Gece/Gündüz Modu (Dark Theme):** Tek tıkla tüm arayüzü premium karanlık temaya geçirme.
* **İnteraktif Dashboard:** Chart.js kullanılarak veritabanından anlık çekilen dinamik Araç Dağılımı (Pasta Grafik) ve Satış Hacmi (Sütun Grafik) analizleri.
* **CRM Paneli:** Müşterilerin geçmiş alımlarını ve toplam cirolarını gösteren akıllı "Müşteri Profil" ekranı.
* **PDF Fatura Çıktısı:** Gerçekleşen satışlar için sistem tarafından otomatik "Kurumsal Satış Sözleşmesi ve Fatura" oluşturma ve yazdırma.
* **Akıllı Maskeleme:** Telefon numaraları (`+90 (5XX)`), KM ve Fiyat bilgilerinin anlık formatlanması.
* **Excel'e Aktarım:** Tablolardaki anlık verilerin tek tıkla `.csv` (Excel) formatında indirilmesi.
* **Canlı Arama (Live Search):** Sayfa yenilenmeden, arka plan API'si ile haberleşerek araç filtreleme.
* **SweetAlert2 Entegrasyonu:** Tarayıcının sıkıcı uyarıları yerine modern, animasyonlu bildirim pop-up'ları.

---

## 🛠️ Kullanılan Teknolojiler

**Backend:**
* Java 17 & Spring Boot
* Spring Data JPA & Hibernate
* H2 Database (In-Memory)
* Spring Security & Validation

**Frontend:**
* HTML5, CSS3, Vanilla JavaScript (Fetch API)
* Bootstrap 5 (Responsive Tasarım)
* Chart.js (Veri Görselleştirme)
* SweetAlert2 (Animasyonlu Bildirimler)

---

## 🚀 Kurulum ve Çalıştırma

1. Projeyi bilgisayarınıza indirin (ZIP veya `git clone`).
2. Projeyi **IntelliJ IDEA** veya tercih ettiğiniz bir IDE ile açın.
3. Maven bağımlılıklarının yüklenmesini bekleyin.
4. `CargalleryApplication.java` sınıfını bularak projeyi başlatın (Run).
5. Tarayıcınızdan **`http://localhost:8080`** adresine gidin.

> **Not:** H2 veritabanı paneline erişmek için `http://localhost:8080/h2-console` adresini kullanabilirsiniz.

---

## 🔐 API Kullanımı ve Güvenlik (Basic Auth)

Projede Spring Security aktiftir ve arka plan uç noktalarına erişim izne tabidir:

| Rol | Yetki Alanı | Kullanıcı Adı | Şifre |
| :--- | :--- | :--- | :--- |
| **USER** | Sadece Okuma / Listeleme (GET) | `user` | `1234` |
| **ADMIN** | Tam Yetki (CRUD İşlemleri) | `admin` | `admin123` |

*(Arayüz üzerinden yapılan tüm otomatik istekler Admin yetkisiyle gönderilecek şekilde yapılandırılmıştır).*
