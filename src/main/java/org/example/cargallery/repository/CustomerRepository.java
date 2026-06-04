package org.example.cargallery.repository;

import org.example.cargallery.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByFirstNameContainingIgnoreCase(String firstName);

    boolean existsByEmailIgnoreCase(String email);
}