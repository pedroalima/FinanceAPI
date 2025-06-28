package com.test.finance_api.controllers;

import com.test.finance_api.dto.auth.LoginRequestDTO;
import com.test.finance_api.dto.auth.LoginResponseDTO;
import com.test.finance_api.dto.auth.RegisterRequestDTO;
import com.test.finance_api.dto.auth.UserDTO;
import com.test.finance_api.entity.User;
import com.test.finance_api.exceptions.EmailAlreadyExistsException;
import com.test.finance_api.exceptions.InvalidAccessException;
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
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO body)  {
        User user = this.userRepository.findByEmail(body.email()).orElseThrow(InvalidAccessException::new);

        if(passwordEncoder.matches(body.password(), user.getPassword())) {
            String token = tokenService.generateToken(user);

            return ResponseEntity.ok(new LoginResponseDTO(token, new UserDTO(user.getName(), user.getEmail())));
        }
        throw new InvalidAccessException();
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> register(@RequestBody RegisterRequestDTO body)  {
        Optional<User> existingUser = this.userRepository.findByEmail(body.email());

        if (existingUser.isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        User newUser = new User();
        newUser.setName(body.username());
        newUser.setEmail(body.email());
        newUser.setPassword(passwordEncoder.encode(body.password()));

        this.userRepository.save(newUser);

        String token = tokenService.generateToken(newUser);

        return ResponseEntity.ok(new LoginResponseDTO(token, new UserDTO(newUser.getName(), newUser.getEmail())));
    }
}
