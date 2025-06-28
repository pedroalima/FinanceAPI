package com.test.finance_api.dto.auth;

public record LoginResponseDTO(String access_token, UserDTO user) {
}
