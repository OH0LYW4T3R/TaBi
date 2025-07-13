package com.example.tabi.appuser.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EmailAuthRequest {
    @NotBlank(message = "Don't make it blank")
    @Pattern(
        regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
        message = "Invalid email format"
    )
    String email;

    @NotBlank(message = "Don't make it blank")
    String code;
}
