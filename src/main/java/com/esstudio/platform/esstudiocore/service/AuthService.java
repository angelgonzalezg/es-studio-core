package com.esstudio.platform.esstudiocore.service;

import com.esstudio.platform.esstudiocore.dto.AuthDto;
import com.esstudio.platform.esstudiocore.entities.User;

public interface AuthService {
    
    AuthDto<?> login(User loginRequest);
    
    void logout(String token);

} 
