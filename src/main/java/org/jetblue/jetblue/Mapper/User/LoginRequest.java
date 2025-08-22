package org.jetblue.jetblue.Mapper.User;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "User name required")
        String userName,
        @NotBlank(message = "Password name required")
        String password
) {
}
