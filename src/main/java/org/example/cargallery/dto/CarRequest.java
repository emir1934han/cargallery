package org.example.cargallery.dto;

import jakarta.validation.constraints.*;

public class CarRequest {

    @NotBlank(message = "Marka boş bırakılamaz.")
    @Size(min = 2, max = 50, message = "Marka 2 ile 50 karakter arasında olmalıdır.")
    private String brand;

    @NotBlank(message = "Seri boş bırakılamaz.")
    private String series;

    @NotBlank(message = "Model boş bırakılamaz.")
    private String model;

    @Min(value = 1900, message = "Yıl 1900'den küçük olamaz.")
    @Max(value = 2026, message = "Gelecek tarihli yıl giremezsiniz.")
    private int year;

    @NotBlank(message = "Yakıt tipi belirtilmelidir.")
    private String fuelType;

    private String transmission;

    private Boolean available; // Burada '= true' atamasını kaldırdık, yapıcı metotta halledeceğiz.

    @Min(value = 0, message = "KM 0'dan küçük olamaz.")
    private int km;

    private String bodyType;
    private String enginePower;
    private String engineCapacity;
    private String drivetrain;
    private String color;

    // --- HOCANIN TARZINA UYGUN YAPICI METOTLAR (CONSTRUCTORS) EKLENDİ ---

    public CarRequest() {
    }

    public CarRequest(String brand, String series, String model, int year, String fuelType,
                      String transmission, Boolean available, int km, String bodyType,
                      String enginePower, String engineCapacity, String drivetrain, String color) {
        this.brand = brand;
        this.series = series;
        this.model = model;
        this.year = year;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.available = available != null ? available : true; // Eğer boş gelirse true (Satışa uygun) yap
        this.km = km;
        this.bodyType = bodyType;
        this.enginePower = enginePower;
        this.engineCapacity = engineCapacity;
        this.drivetrain = drivetrain;
        this.color = color;
    }

    // --- GETTERS & SETTERS ---

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getSeries() { return series; }
    public void setSeries(String series) { this.series = series; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    public String getTransmission() { return transmission; }
    public void setTransmission(String transmission) { this.transmission = transmission; }

    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }

    public int getKm() { return km; }
    public void setKm(int km) { this.km = km; }

    public String getBodyType() { return bodyType; }
    public void setBodyType(String bodyType) { this.bodyType = bodyType; }

    public String getEnginePower() { return enginePower; }
    public void setEnginePower(String enginePower) { this.enginePower = enginePower; }

    public String getEngineCapacity() { return engineCapacity; }
    public void setEngineCapacity(String engineCapacity) { this.engineCapacity = engineCapacity; }

    public String getDrivetrain() { return drivetrain; }
    public void setDrivetrain(String drivetrain) { this.drivetrain = drivetrain; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}