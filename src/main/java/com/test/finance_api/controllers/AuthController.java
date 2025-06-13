package com.test.finance_api.controllers;

import com.test.finance_api.dto.LoginRequestDTO;
import com.test.finance_api.dto.LoginResponseDTO;
import com.test.finance_api.dto.RegisterRequestDTO;
import com.test.finance_api.dto.UserDTO;
import com.test.finance_api.entity.user.User;
import com.test.finance_api.infra.security.TokenService;
import com.test.finance_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body)  {
        User user = this.userRepository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found"));

        if(passwordEncoder.matches(body.password(), user.getPassword())) {
            String token = tokenService.generateToken(user);

            return ResponseEntity.ok(new LoginResponseDTO(token, new UserDTO(user.getName(), user.getEmail())));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body)  {
        Optional<User> user = this.userRepository.findByEmail(body.email());

        if(user.isEmpty()) {
            User newUser = new User();
            newUser.setName(body.username());
            newUser.setEmail(body.email());
            newUser.setPassword(passwordEncoder.encode(body.password()));

            this.userRepository.save(newUser);

            String token = tokenService.generateToken(newUser);

            return ResponseEntity.ok(new LoginResponseDTO(token, new UserDTO(newUser.getName(), newUser.getEmail())));
        }

        return ResponseEntity.badRequest().build();
    }
}
