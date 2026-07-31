package com.furnihub.backend.dto;

import com.furnihub.backend.entity.User;

public record UserDto(Long id, String fullName, String email, String mobile, String role) {

    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getMobile(),
                user.getRole()
        );
    }
}
