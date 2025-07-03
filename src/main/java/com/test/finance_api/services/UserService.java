package com.test.finance_api.services;

import com.test.finance_api.dto.user.GetAllUserResponse;
import com.test.finance_api.entity.User;
import com.test.finance_api.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<GetAllUserResponse> getAllUsers() {
        List<User> users = this.userRepository.findAll();

        return ResponseEntity.ok(new GetAllUserResponse("All users retrieved successfully", users));
    }
}
