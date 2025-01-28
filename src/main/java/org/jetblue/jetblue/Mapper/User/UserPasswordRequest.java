package org.jetblue.jetblue.Mapper.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserPasswordRequest(
        @NotBlank(message = "User name required")
        String userName,
        @NotBlank(message = "Old password required")
        String oldPassword,
        @NotNull
        @NotBlank(message = "New password can it be null")
        String password
) {
}
