package com.test.finance_api.validators;

import com.test.finance_api.exceptions.UserNotFoundException;
import com.test.finance_api.repositories.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {
    private final UserRepository _userRepository;

    public UserValidator(UserRepository userRepository) {
        this._userRepository = userRepository;
    }

    public void verify(String userId) {
        this._userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("UserId: " + userId + " não encontrado"));
    }
}
