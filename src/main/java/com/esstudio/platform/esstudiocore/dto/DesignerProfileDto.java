package com.esstudio.platform.esstudiocore.dto;

import jakarta.validation.constraints.Size;

public class DesignerProfileDto {

    private String specialty;

    @Size(max = 500)
    private String bio;

    private String portfolioUrl;

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPortfolioUrl() {
        return portfolioUrl;
    }

    public void setPortfolioUrl(String portfolioUrl) {
        this.portfolioUrl = portfolioUrl;
    }
}
