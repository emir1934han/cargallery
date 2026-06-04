package org.example.cargallery.controller;

import jakarta.validation.Valid;
import org.example.cargallery.dto.SaleRequest;
import org.example.cargallery.entity.Sale;
import org.example.cargallery.service.SaleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) { this.saleService = saleService; }

    @GetMapping
    public List<Sale> getAllSales() {
        return saleService.getAllSales();
    }

    @PostMapping
    public ResponseEntity<Sale> createSale(@Valid @RequestBody SaleRequest req) {
        Sale sale = saleService.makeSale(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(sale);
    }

    @GetMapping("/customer/{customerId}")
    public List<Sale> getSalesByCustomer(@PathVariable Long customerId) {
        return saleService.getSalesByCustomerId(customerId);
    }
}