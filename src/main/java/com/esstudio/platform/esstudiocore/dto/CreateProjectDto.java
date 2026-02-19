package com.esstudio.platform.esstudiocore.dto;

import java.util.Set;

public class CreateProjectDto {
    
    private String name;
    private String description;
    private Set<Long> clientIds;
    private Set<Long> designerIds;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Long> getClientIds() {
        return clientIds;
    }

    public void setClientIds(Set<Long> clientIds) {
        this.clientIds = clientIds;
    }

    public Set<Long> getDesignerIds() {
        return designerIds;
    }

    public void setDesignerIds(Set<Long> designerIds) {
        this.designerIds = designerIds;
    }
}
