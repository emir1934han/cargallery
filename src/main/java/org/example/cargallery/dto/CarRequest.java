package org.example.cargallery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CarRequest {

    @NotBlank(message = "Marka boş bırakılamaz.")
    @Size(min = 2, max = 50, message = "Marka 2 ile 50 karakter arasında olmalıdır.")
    private String brand;

    @NotBlank(message = "Model boş bırakılamaz.")
    @Size(max = 100, message = "Model en fazla 100 karakter olabilir.")
    private String model;

    private Boolean available;

    public CarRequest() {
    }

    public CarRequest(String brand, String model, Boolean available) {
        this.brand = brand;
        this.model = model;
        this.available = available;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}