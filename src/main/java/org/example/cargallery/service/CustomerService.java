package org.example.cargallery.service;

import org.example.cargallery.dto.CustomerRequest;
import org.example.cargallery.entity.Customer;
import org.example.cargallery.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() { return customerRepository.findAll(); }

    public Optional<Customer> getCustomerById(Long id) { return customerRepository.findById(id); }

    @Transactional
    public Customer createCustomer(CustomerRequest req) {
        Customer customer = new Customer(null, req.getFirstName(), req.getLastName(), req.getPhone(), req.getEmail());
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer updateCustomer(Long id, CustomerRequest req) {
        return customerRepository.findById(id).map(existing -> {
            existing.setFirstName(req.getFirstName());
            existing.setLastName(req.getLastName());
            existing.setPhone(req.getPhone());
            existing.setEmail(req.getEmail());
            return customerRepository.save(existing);
        }).orElse(null);
    }

    @Transactional
    public boolean deleteCustomer(Long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Özel Sorgular
    public List<Customer> searchByName(String name) { return customerRepository.findByFirstNameContainingIgnoreCase(name); }
    public boolean checkEmailExists(String email) { return customerRepository.existsByEmailIgnoreCase(email); }
}