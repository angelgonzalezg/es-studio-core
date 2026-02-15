package com.esstudio.platform.esstudiocore.dto;

import jakarta.validation.constraints.Size;

public class CreateClientProfileDto {

    @Size(max = 100)
    private String companyName;
    
    @Size(max = 20)
    private String taxId;

    @Size(max = 100)
    private String billingAddress;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }
}
