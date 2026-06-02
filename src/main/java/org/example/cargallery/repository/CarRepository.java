package org.example.cargallery.repository;

import org.example.cargallery.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByAvailable(boolean available);

    List<Car> findByBrandContainingIgnoreCase(String brand);

    List<Car> findByBrandContainingIgnoreCaseAndAvailable(String brand, boolean available);

    long countByAvailable(boolean available);

    boolean existsByBrandIgnoreCase(String brand);

    List<Car> findTop5ByOrderByIdDesc();
}