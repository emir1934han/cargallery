package org.example.cargallery.controller;

import jakarta.validation.Valid;
import org.example.cargallery.dto.CarRequest;
import org.example.cargallery.entity.Car;
import org.example.cargallery.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    // --- TEMEL CRUD İŞLEMLERİ ---

    @GetMapping
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    // Hocanın eklediği: Tekil veri getirme metodu
    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        return carService.getCarById(id)
                .map(car -> ResponseEntity.ok(car))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Car> createCar(@Valid @RequestBody CarRequest carRequest) {
        Car createdCar = carService.createCar(carRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCar);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(
            @PathVariable Long id,
            @Valid @RequestBody CarRequest carRequest
    ) {
        Car updatedCar = carService.updateCar(id, carRequest);

        if (updatedCar != null) {
            return ResponseEntity.ok(updatedCar);
        }

        return ResponseEntity.notFound().build();
    }

    // Hocanın tarzına uygun olarak String mesaj dönen silme işlemi
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCar(@PathVariable Long id) {
        boolean deleted = carService.deleteCar(id);

        if (deleted) {
            return ResponseEntity.ok("Araç silindi.");
        }

        return ResponseEntity.notFound().build();
    }

    // --- DERIVED QUERY METHODS (Hocanın 7. Madde İstediği Kısımlar) ---

    @GetMapping("/search")
    public List<Car> searchCarsByBrand(@RequestParam String brand) {
        return carService.searchCarsByBrand(brand);
    }

    @GetMapping("/filter")
    public List<Car> filterCars(
            @RequestParam String brand,
            @RequestParam boolean available
    ) {
        return carService.filterCarsByBrandAndAvailable(brand, available);
    }

    @GetMapping("/count")
    public long countCarsByAvailableStatus(@RequestParam boolean available) {
        return carService.countCarsByAvailableStatus(available);
    }

    @GetMapping("/exists")
    public boolean existsCarByBrand(@RequestParam String brand) {
        return carService.existsCarByBrand(brand);
    }

    @GetMapping("/latest")
    public List<Car> getLatestFiveCars() {
        return carService.getLatestFiveCars();
    }

    // Hocanın Transactional (Rollback) testini birebir ekledik
    @PostMapping("/rollback-test")
    public ResponseEntity<Car> createCarWithRollbackTest(@Valid @RequestBody CarRequest carRequest) {
        Car createdCar = carService.createCarWithRollbackTest(carRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCar);
    }
}