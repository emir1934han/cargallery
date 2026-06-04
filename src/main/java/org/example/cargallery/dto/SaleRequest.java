package org.example.cargallery.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class SaleRequest {

    @NotNull(message = "Araç ID boş olamaz.")
    private Long carId;

    @NotNull(message = "Müşteri ID boş olamaz.")
    private Long customerId;

    @NotNull(message = "Satış tarihi girilmelidir.")
    private LocalDate saleDate;

    @Min(value = 0, message = "Fiyat 0'dan küçük olamaz.")
    private double price;

    public SaleRequest() {}

    public Long getCarId() { return carId; }
    public void setCarId(Long carId) { this.carId = carId; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public LocalDate getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDate saleDate) { this.saleDate = saleDate; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}