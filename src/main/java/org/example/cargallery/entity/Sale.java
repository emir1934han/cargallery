package org.example.cargallery.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Hangi araba satıldı? (Car tablosuna bağlantı)
    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    // Hangi müşteriye satıldı? (Customer tablosuna bağlantı)
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private LocalDate saleDate;
    private double price;

    public Sale() {}

    public Sale(Long id, Car car, Customer customer, LocalDate saleDate, double price) {
        this.id = id;
        this.car = car;
        this.customer = customer;
        this.saleDate = saleDate;
        this.price = price;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Car getCar() { return car; }
    public void setCar(Car car) { this.car = car; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public LocalDate getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDate saleDate) { this.saleDate = saleDate; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}