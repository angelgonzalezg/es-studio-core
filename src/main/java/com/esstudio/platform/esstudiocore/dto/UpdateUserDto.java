package com.esstudio.platform.esstudiocore.dto;

import java.time.LocalDateTime;

public class UpdateUserDto {

    // user fields
    private String firstName;

    private String lastName;

    private String phone;

    private LocalDateTime updateTime;

    private ClientProfileDto clientProfile;

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

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public ClientProfileDto getClientProfile() {
        return clientProfile;
    }

    public void setClientProfile(ClientProfileDto clientProfile) {
        this.clientProfile = clientProfile;
    }

    

    
}
