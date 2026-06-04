package org.example.cargallery.repository;

import org.example.cargallery.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findByCarId(Long carId);

    List<Sale> findByCustomerId(Long customerId);
}