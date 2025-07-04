package com.test.finance_api.services;

import com.test.finance_api.dto.user.GetAllUserResponse;
import com.test.finance_api.entity.User;
import com.test.finance_api.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository _userRepository;

    public UserService(UserRepository userRepository) {
        this._userRepository = userRepository;
    }

    public ResponseEntity<GetAllUserResponse> getAllUsers() {
        List<User> users = this._userRepository.findAll();

        if (users.isEmpty()) {
            return ResponseEntity.ok(new GetAllUserResponse("Nenhum usuário encontrado", List.of()));
        }

        return ResponseEntity.ok(new GetAllUserResponse("Todas as transações recebidas com sucesso", users));
    }
}
