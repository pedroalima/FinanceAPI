package com.test.finance_api.dto.user;

import com.test.finance_api.entity.User;

import java.util.List;

public record GetAllUserResponse(String message, List<User> users) {}
