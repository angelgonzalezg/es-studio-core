package com.esstudio.platform.esstudiocore.dto;

import com.esstudio.platform.esstudiocore.security.enums.RoleType;


public class UpdateRoleDto {

    private RoleType role;

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }
}
