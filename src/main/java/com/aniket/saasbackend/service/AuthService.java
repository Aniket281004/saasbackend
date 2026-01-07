package com.aniket.saasbackend.service;


import com.aniket.saasbackend.model.User;
import com.aniket.saasbackend.repository.UserRepository;
import com.aniket.saasbackend.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public String login(String email, String password) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (!encoder.matches(password, user.getPassword())) {
        throw new RuntimeException("Invalid credentials");
    }

    return JwtUtil.generateToken(user.getEmail());
}


    public User register(String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        user.setRole("USER");

        return userRepository.save(user);
    }
}
