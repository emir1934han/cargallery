package org.example.cargallery.service;

import org.example.cargallery.dto.SaleRequest;
import org.example.cargallery.entity.Car;
import org.example.cargallery.entity.Customer;
import org.example.cargallery.entity.Sale;
import org.example.cargallery.repository.CarRepository;
import org.example.cargallery.repository.CustomerRepository;
import org.example.cargallery.repository.SaleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SaleService {

    private final SaleRepository saleRepository;
    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;

    public SaleService(SaleRepository saleRepository, CarRepository carRepository, CustomerRepository customerRepository) {
        this.saleRepository = saleRepository;
        this.carRepository = carRepository;
        this.customerRepository = customerRepository;
    }

    public List<Sale> getAllSales() { return saleRepository.findAll(); }

    @Transactional
    public Sale makeSale(SaleRequest req) {
        // 1. Veritabanından arabayı ve müşteriyi bul
        Car car = carRepository.findById(req.getCarId())
                .orElseThrow(() -> new RuntimeException("Araç bulunamadı!"));

        Customer customer = customerRepository.findById(req.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Müşteri bulunamadı!"));

        // 2. Arabayı satıldı olarak işaretle ve güncelle
        car.setAvailable(false);
        carRepository.save(car);

        // 3. Satışı kaydet
        Sale sale = new Sale(null, car, customer, req.getSaleDate(), req.getPrice());
        return saleRepository.save(sale);
    }

    // Özel Sorgular: Bir arabanın veya müşterinin geçmiş satışları
    public List<Sale> getSalesByCarId(Long carId) { return saleRepository.findByCarId(carId); }
    public List<Sale> getSalesByCustomerId(Long customerId) { return saleRepository.findByCustomerId(customerId); }
}