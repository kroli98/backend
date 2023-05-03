package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    public User registerUser(User user) {

        User savedUser = userRepository.save(user);

        // Visszaadjuk a mentett felhasználót a hívónak
        return savedUser;
    }


    public User loginUser(String username, String password)  {
        // Ellenőrizzük, hogy a felhasználó létezik-e az adatbázisban
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        // Ellenőrizzük, hogy a jelszó helyes-e
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new InvalidPasswordException("Invalid password");
        }

        // Sikeres bejelentkezés esetén visszaadjuk a felhasználót
        return user;
    }
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
    public static class InvalidPasswordException extends RuntimeException {
        public InvalidPasswordException(String message) {
            super(message);
        }
    }


}
