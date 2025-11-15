package com.pultyn.spring_jwt.dto;

import com.pultyn.spring_jwt.enums.UserRole;
import com.pultyn.spring_jwt.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private Set<UserRole> roles;

    public UserDTO(UserEntity user)
    {
        this.id = user.getId();
        this.email = user.getEmail();
        this.roles = user.getRoles();
    }
}
