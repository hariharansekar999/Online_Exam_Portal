package com.cts.onlineexamportall.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
	@NotBlank(message = "User name is required")
	private String userName;

	@NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password should be at least 6 characters long")
	private String password;
}
