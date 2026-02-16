package com.esstudio.platform.esstudiocore.service;

import com.esstudio.platform.esstudiocore.dto.DesignerProfileDto;
import com.esstudio.platform.esstudiocore.entities.DesignerProfile;
import com.esstudio.platform.esstudiocore.entities.User;

public interface DesignerProfileService {
    
    DesignerProfile updateProfile(User user, DesignerProfileDto dto);
}
