package org.example.cargallery.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CustomerRequest {

    @NotBlank(message = "İsim boş bırakılamaz.")
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank(message = "Soyisim boş bırakılamaz.")
    private String lastName;

    @NotBlank(message = "Telefon numarası boş bırakılamaz.")
    private String phone;

    @Email(message = "Geçerli bir email adresi giriniz.")
    private String email;

    public CustomerRequest() {}

    public CustomerRequest(String firstName, String lastName, String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}