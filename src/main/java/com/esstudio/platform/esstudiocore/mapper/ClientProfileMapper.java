package com.esstudio.platform.esstudiocore.mapper;

import org.springframework.stereotype.Component;

import com.esstudio.platform.esstudiocore.dto.ClientProfileDto;
import com.esstudio.platform.esstudiocore.entities.ClientProfile;

@Component
public class ClientProfileMapper {

    // ClientProfile (entity) -> ClientProfileDto (client response)
    public ClientProfileDto toDto(ClientProfile profile) {

        if (profile == null)
            return null;

        ClientProfileDto dto = new ClientProfileDto();

        dto.setCompanyName(profile.getCompanyName());
        dto.setTaxId(profile.getTaxId());
        dto.setBillingAddress(profile.getBillingAddress());

        return dto;
    }

    // ClientProfileDto -> ClientProfile (entity) (for create/update)
    public ClientProfile toEntity(
            ClientProfileDto dto,
            ClientProfile profile) {

        if (dto == null)
            return profile;

        if (profile == null) {
            profile = new ClientProfile();
        }

        if (dto.getCompanyName() != null)
            profile.setCompanyName(dto.getCompanyName());

        if (dto.getTaxId() != null)
            profile.setTaxId(dto.getTaxId());

        if (dto.getBillingAddress() != null)
            profile.setBillingAddress(dto.getBillingAddress());

        return profile;
    }
}
