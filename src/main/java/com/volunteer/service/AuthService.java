package com.volunteer.service;

import com.volunteer.dto.AuthDto;
import com.volunteer.entity.User;
import com.volunteer.exception.DuplicateResourceException;
import com.volunteer.repository.UserRepository;
import com.volunteer.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public AuthDto.LoginResponse login(AuthDto.LoginRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (Exception ex) {
            throw new BadCredentialsException("Invalid email or password");
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtUtils.generateToken(userDetails);

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new BadCredentialsException("User not found"));

        return new AuthDto.LoginResponse(token, user.getEmail(), user.getFullName(), user.getRole().name());
    }

    @Transactional
    public AuthDto.LoginResponse register(AuthDto.RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered: " + request.getEmail());
        }

        User user = User.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .fullName(request.getFullName())
            .role(User.Role.VOLUNTEER)
            .build();

        userRepository.save(user);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtUtils.generateToken(userDetails);

        return new AuthDto.LoginResponse(token, user.getEmail(), user.getFullName(), user.getRole().name());
    }
}
