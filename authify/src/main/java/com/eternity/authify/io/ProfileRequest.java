package com.eternity.authify.io;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileRequest {
    @NotBlank(message = "name should not be empty")
    private String name;
    @NotNull(message = "email should not be empty")
    @Email(message = "Enter a valid email")
    private String email;
    @Size(min = 6, message = "Password must be atleast 6 characters")
    @NotNull(message = "Required Password")
    private String password;
}
