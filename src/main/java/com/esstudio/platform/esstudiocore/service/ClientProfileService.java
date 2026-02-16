package com.esstudio.platform.esstudiocore.service;

import com.esstudio.platform.esstudiocore.dto.ClientProfileDto;
import com.esstudio.platform.esstudiocore.entities.ClientProfile;
import com.esstudio.platform.esstudiocore.entities.User;

public interface ClientProfileService {
    
    ClientProfile updateProfile(User user, ClientProfileDto dto);
}
