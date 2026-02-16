package com.esstudio.platform.esstudiocore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esstudio.platform.esstudiocore.dto.DesignerProfileDto;
import com.esstudio.platform.esstudiocore.entities.DesignerProfile;
import com.esstudio.platform.esstudiocore.entities.User;
import com.esstudio.platform.esstudiocore.repository.DesignerProfileRepository;

@Service
public class DesignerProfileServiceImpl implements DesignerProfileService {

    @Autowired
    private DesignerProfileRepository designerProfileRepository;

    @Override
    public DesignerProfile updateProfile(User user, DesignerProfileDto dto) {
        if (user == null || dto == null) {
            throw new IllegalArgumentException("User and DTO cannot be null");
        }

        DesignerProfile profile = user.getDesignerProfile();

        if (profile == null) {
            profile = new DesignerProfile();
            profile.setUser(user);
        }

        if (dto.getSpecialty() != null) {
            profile.setSpecialty(dto.getSpecialty());
        }
        if (dto.getBio() != null) {
            profile.setBio(dto.getBio());
        }
        if (dto.getPortfolioUrl() != null) {
            profile.setPortfolioUrl(dto.getPortfolioUrl());
        }

        user.setDesignerProfile(profile);

        return designerProfileRepository.save(profile);
    }
}
