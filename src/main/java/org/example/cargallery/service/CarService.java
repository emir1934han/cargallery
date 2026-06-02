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

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    @Transactional
    public Car createCar(CarRequest carRequest) {
        Car car = new Car();

        car.setBrand(carRequest.getBrand());
        car.setModel(carRequest.getModel());

        if (carRequest.getAvailable() != null) {
            car.setAvailable(carRequest.getAvailable());
        } else {
            car.setAvailable(false);
        }

        return carRepository.save(car);
    }

    @Transactional
    public Car updateCar(Long id, CarRequest carRequest) {
        Optional<Car> optionalCar = carRepository.findById(id);

        if (optionalCar.isPresent()) {
            Car existingCar = optionalCar.get();

            existingCar.setBrand(carRequest.getBrand());
            existingCar.setModel(carRequest.getModel());

            if (carRequest.getAvailable() != null) {
                existingCar.setAvailable(carRequest.getAvailable());
            }

            return carRepository.save(existingCar);
        }

        return null;
    }

    @Transactional
    public boolean deleteCar(Long id) {
        Optional<Car> optionalCar = carRepository.findById(id);

        if (optionalCar.isPresent()) {
            carRepository.deleteById(id);
            return true;
        }

        return false;
    }

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
    public Car createCarWithRollbackTest(CarRequest carRequest) {
        Car car = new Car();

        car.setBrand(carRequest.getBrand());
        car.setModel(carRequest.getModel());

        if (carRequest.getAvailable() != null) {
            car.setAvailable(carRequest.getAvailable());
        } else {
            car.setAvailable(false);
        }

        Car savedCar = carRepository.save(car);

        if (true) {
            throw new RuntimeException("Rollback testi için bilinçli hata oluşturuldu.");
        }

        return savedCar;
    }
}