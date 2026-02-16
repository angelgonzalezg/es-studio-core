package com.esstudio.platform.esstudiocore.service;

import com.esstudio.platform.esstudiocore.entities.User;
import com.esstudio.platform.esstudiocore.security.enums.RoleType;

public interface RoleService {
    
    void assignRoletoUser(User user, RoleType type);

    boolean hasRole(User user, RoleType type);

}
