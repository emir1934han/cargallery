package org.example.cargallery.service;

import org.example.cargallery.dto.CarRequest;
import org.example.cargallery.entity.Car;
import org.example.cargallery.repository.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    // --- TEMEL CRUD İŞLEMLERİ ---

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    @Transactional
    public Car createCar(CarRequest req) {
        Car car = new Car();
        mapRequestToEntity(req, car);
        return carRepository.save(car);
    }

    @Transactional
    public Car updateCar(Long id, CarRequest req) {
        Optional<Car> optionalCar = carRepository.findById(id);
        if (optionalCar.isPresent()) {
            Car existingCar = optionalCar.get();
            mapRequestToEntity(req, existingCar);
            return carRepository.save(existingCar);
        }
        return null;
    }

    @Transactional
    public boolean deleteCar(Long id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // --- ÖZEL METOTLAR (Sorgular ve Testler) ---

    public List<Car> getCarsByAvailableStatus(boolean available) {
        return carRepository.findByAvailable(available);
    }

    public List<Car> searchCarsByBrand(String brand) {
        return carRepository.findByBrandContainingIgnoreCase(brand);
    }

    public List<Car> filterCarsByBrandAndAvailable(String brand, boolean available) {
        return carRepository.findByBrandContainingIgnoreCaseAndAvailable(brand, available);
    }

    public long countCarsByAvailableStatus(boolean available) {
        return carRepository.countByAvailable(available);
    }

    public boolean existsCarByBrand(String brand) {
        return carRepository.existsByBrandIgnoreCase(brand);
    }

    public List<Car> getLatestFiveCars() {
        return carRepository.findTop5ByOrderByIdDesc();
    }

    @Transactional
    public Car createCarWithRollbackTest(CarRequest req) {
        Car car = new Car();
        mapRequestToEntity(req, car);
        Car savedCar = carRepository.save(car);

        // Hocanın istediği bilinçli hata (Rollback testi için)
        if (true) {
            throw new RuntimeException("Rollback testi için bilinçli hata oluşturuldu.");
        }
        return savedCar;
    }

    private void mapRequestToEntity(CarRequest req, Car car) {
        car.setBrand(req.getBrand());
        car.setSeries(req.getSeries());
        car.setModel(req.getModel());
        car.setYear(req.getYear());
        car.setFuelType(req.getFuelType());
        car.setTransmission(req.getTransmission());
        car.setAvailable(req.getAvailable() != null ? req.getAvailable() : false);
        car.setKm(req.getKm());
        car.setBodyType(req.getBodyType());
        car.setEnginePower(req.getEnginePower());
        car.setEngineCapacity(req.getEngineCapacity());
        car.setDrivetrain(req.getDrivetrain());
        car.setColor(req.getColor());
    }
}