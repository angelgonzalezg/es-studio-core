package com.esstudio.platform.esstudiocore.mapper;

import org.springframework.stereotype.Component;

import com.esstudio.platform.esstudiocore.dto.DesignerProfileDto;
import com.esstudio.platform.esstudiocore.dto.UpdateUserDto;
import com.esstudio.platform.esstudiocore.entities.DesignerProfile;

@Component
public class DesignerProfileMapper {

    // DesignerProfile (entity) -> DesignerProfileDto (client response)
    public DesignerProfile toEntity(DesignerProfileDto dto) {

        if (dto == null) return null;

        DesignerProfile profile = new DesignerProfile();
        profile.setSpecialty(dto.getSpecialty());
        profile.setBio(dto.getBio());
        profile.setPortfolioUrl(dto.getPortfolioUrl());

        return profile;
    }

    // DesignerProfileDto -> DesignerProfile (entity) (for create/update)
    public DesignerProfileDto toDto(DesignerProfile profile) {

        if (profile == null) return null;

        DesignerProfileDto dto = new DesignerProfileDto();
        dto.setSpecialty(profile.getSpecialty());
        dto.setBio(profile.getBio());
        dto.setPortfolioUrl(profile.getPortfolioUrl());

        return dto;
    }


    public void updateEntity(DesignerProfile profile, UpdateUserDto dto) {
        if (profile == null) return;

        profile.setSpecialty(dto.getDesignerProfile().getSpecialty());
        profile.setBio(dto.getDesignerProfile().getBio());
        profile.setPortfolioUrl(dto.getDesignerProfile().getPortfolioUrl());
    }
}
