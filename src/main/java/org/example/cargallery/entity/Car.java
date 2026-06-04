package org.example.cargallery.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private String series;
    private String model;

    @Column(name = "car_year")
    private int year;

    private String fuelType;
    private String transmission;
    private boolean available;
    private int km;
    private String bodyType;
    private String enginePower;
    private String engineCapacity;
    private String drivetrain;
    private String color;

    public Car() {}

    public Car(Long id, String brand, String series, String model, int year, String fuelType,
               String transmission, boolean available, int km, String bodyType,
               String enginePower, String engineCapacity, String drivetrain, String color) {
        this.id = id;
        this.brand = brand;
        this.series = series;
        this.model = model;
        this.year = year;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.available = available;
        this.km = km;
        this.bodyType = bodyType;
        this.enginePower = enginePower;
        this.engineCapacity = engineCapacity;
        this.drivetrain = drivetrain;
        this.color = color;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
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

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                '}';
    }
}