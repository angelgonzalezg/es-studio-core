package com.esstudio.platform.esstudiocore.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.esstudio.platform.esstudiocore.dto.ClientProfileDto;
import com.esstudio.platform.esstudiocore.entities.ClientProfile;
import com.esstudio.platform.esstudiocore.entities.User;
import com.esstudio.platform.esstudiocore.repository.ClientProfileRepository;

@Service
public class ClientProfileServiceImpl implements ClientProfileService {

    @Autowired
    private ClientProfileRepository clientProfileRepository;

    @Override
    public ClientProfile updateProfile(User user, ClientProfileDto dto) {
        if (user == null || dto == null) {
            throw new IllegalArgumentException("User and DTO cannot be null");
        }

        ClientProfile profile = user.getClientProfile();

        if (profile == null) {
            profile = new ClientProfile();
            profile.setUser(user);
        }

        if (dto.getCompanyName() != null) {
            profile.setCompanyName(dto.getCompanyName());
        }
        if (dto.getTaxId() != null) {
            profile.setTaxId(dto.getTaxId());
        }
        if (dto.getBillingAddress() != null) {
            profile.setBillingAddress(dto.getBillingAddress());
        }

        user.setClientProfile(profile);

        return clientProfileRepository.save(profile);
    }
}
