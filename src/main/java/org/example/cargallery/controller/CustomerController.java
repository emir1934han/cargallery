package org.example.cargallery.controller;

import jakarta.validation.Valid;
import org.example.cargallery.dto.CustomerRequest;
import org.example.cargallery.entity.Customer;
import org.example.cargallery.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) { this.customerService = customerService; }

    @GetMapping
    public List<Customer> getAll() { return customerService.getAllCustomers(); }

    @PostMapping
    public ResponseEntity<Customer> create(@Valid @RequestBody CustomerRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        boolean deleted = customerService.deleteCustomer(id);
        return deleted ? ResponseEntity.ok("Müşteri silindi.") : ResponseEntity.notFound().build();
    }
}