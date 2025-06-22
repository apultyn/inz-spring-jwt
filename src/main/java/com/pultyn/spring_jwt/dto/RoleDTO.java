package com.pultyn.spring_jwt.dto;

import com.pultyn.spring_jwt.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private String name;

    public RoleDTO(Role role) {
        this.name = role.getName();
    }
}
