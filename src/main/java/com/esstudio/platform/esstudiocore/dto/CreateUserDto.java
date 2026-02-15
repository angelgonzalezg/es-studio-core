package com.esstudio.platform.esstudiocore.dto;

import com.esstudio.platform.esstudiocore.validation.ExistsByEmail;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateUserDto {

    @Email
    @ExistsByEmail
    @NotBlank
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(min = 8, max = 64)
    private String password;

    @Size(max = 100)
    private String firstName;
    
    @Size(max = 100)
    private String lastName;
    
    @Size(max = 30)
    private String phone;

    private boolean isAdmin;

    // Optional Client profile information for users
    private CreateClientProfileDto clientProfile;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public CreateClientProfileDto getClientProfile() {
        return clientProfile;
    }

    public void setClientProfile(CreateClientProfileDto clientProfile) {
        this.clientProfile = clientProfile;
    }
}
