package com.example.demo.user.dto;

import com.example.demo.user.entity.UserEntity;
import lombok.*;

@Getter@Setter@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private String email;
    private String username;
    private String token;//인증토큰

    public UserResponseDTO(UserEntity entity) {
        this.email = entity.getEmail();
        this.username = entity.getUsername();
    }
}