package com.test.finance_api.dto.auth;

public record LoginResponseDTO(String message, String access_token, UserDTO user) {}
