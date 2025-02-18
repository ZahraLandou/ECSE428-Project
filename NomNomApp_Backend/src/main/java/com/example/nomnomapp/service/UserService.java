package com.example.nomnomapp.service;

import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Deletes a user account by username.
     *
     * @param username The username of the user to delete.
     * @throws IllegalArgumentException if the user does not exist.
     */
    public void deleteUserByUsername(String username) {
        Optional<NomNomUser> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            userRepository.delete(user.get());
        } else {
            throw new IllegalArgumentException("User with username '" + username + "' not found.");
        }
    }

    /**
     * Deletes a user account by email address.
     *
     * @param emailAddress The email address of the user to delete.
     * @throws IllegalArgumentException if the user does not exist.
     */
    public void deleteUserByEmail(String emailAddress) {
        Optional<NomNomUser> user = userRepository.findByEmailAddress(emailAddress);
        if (user.isPresent()) {
            userRepository.delete(user.get());
        } else {
            throw new IllegalArgumentException("User with email '" + emailAddress + "' not found.");
        }
    }
}
