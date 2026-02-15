package com.esstudio.platform.esstudiocore.mapper;

import org.springframework.stereotype.Component;

import com.esstudio.platform.esstudiocore.dto.CreateClientProfileDto;
import com.esstudio.platform.esstudiocore.dto.UpdateUserDto;
import com.esstudio.platform.esstudiocore.entities.ClientProfile;

@Component
public class ClientProfileMapper {

    public ClientProfile toEntity(CreateClientProfileDto dto) {

        if (dto == null) return null;

        ClientProfile profile = new ClientProfile();

        profile.setCompanyName(dto.getCompanyName());
        profile.setTaxId(dto.getTaxId());
        profile.setBillingAddress(dto.getBillingAddress());

        return profile;
    }


    public CreateClientProfileDto toDto(ClientProfile profile) {

        if (profile == null) return null;

        CreateClientProfileDto dto = new CreateClientProfileDto();

        dto.setCompanyName(profile.getCompanyName());
        dto.setTaxId(profile.getTaxId());
        dto.setBillingAddress(profile.getBillingAddress());

        return dto;
    }


    public void updateEntity(ClientProfile profile, UpdateUserDto dto) {
        if (profile == null) return;

        profile.setCompanyName(dto.getClientProfile().getCompanyName());
        profile.setTaxId(dto.getClientProfile().getTaxId());
        profile.setBillingAddress(dto.getClientProfile().getBillingAddress());
    }
}
