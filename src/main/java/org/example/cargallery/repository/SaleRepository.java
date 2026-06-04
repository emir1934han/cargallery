package org.example.cargallery.repository;

import org.example.cargallery.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    // Belirli bir arabanın (ID'ye göre) satış kayıtlarını getir
    List<Sale> findByCarId(Long carId);

    // Belirli bir müşterinin (ID'ye göre) yaptığı tüm satın almaları getir
    List<Sale> findByCustomerId(Long customerId);
}