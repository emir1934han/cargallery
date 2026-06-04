package org.example.cargallery.repository;

import org.example.cargallery.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // İsme göre müşteri arama
    List<Customer> findByFirstNameContainingIgnoreCase(String firstName);

    // Bu email adresiyle kayıtlı müşteri var mı kontrolü
    boolean existsByEmailIgnoreCase(String email);
}