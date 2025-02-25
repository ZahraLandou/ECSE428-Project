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
     * Creates a new user, given a username, email address, and password
     * 
     * @param aUsername     the unique username of new user to create
     * @param aEmailAddress the unique email of new user to create
     * @param aPassword     the password of new user to create
     * @return the created NomNomUser object
     */
    public NomNomUser createUser(String aUsername, String aEmailAddress, String aPassword) {

        // check if username is empty
        if (aUsername == null || aUsername.trim().length() == 0) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        // check if email is empty
        if (aEmailAddress == null || aEmailAddress.trim().length() == 0) {
            throw new IllegalArgumentException("Email address cannot be empty");
        }
        // check if password is empty
        if (aPassword == null || aPassword.trim().length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        // check if a user with the same username already exists
        if (userRepository.findByUsername(aUsername).isPresent()) {
            throw new IllegalArgumentException("User with username '" + aUsername + "' already exists");
        }
        // check if a user with the same email already exists
        if (userRepository.findByEmailAddress(aEmailAddress).isPresent()) {
            throw new IllegalArgumentException("User with email '" + aEmailAddress + "' already exists");
        }

        // create new NomNomUser entity
        NomNomUser user = new NomNomUser(aUsername, aEmailAddress, aPassword);

        // save new NomNomUser in repository
        return userRepository.save(user);
    }

    /**
     * Gets a user by username
     * 
     * @param aUsername the username of user to find
     * @return the NomNomUser object with the given username
     */
    public Optional<NomNomUser> getUserByUsername(String aUsername) {
        // check if username is empty
        if (aUsername == null || aUsername.trim().length() == 0) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        // check if user with username exists
        if (userRepository.existsByUsername(aUsername)) {
            return null;
        }

        return userRepository.findByUsername(aUsername);
    }

    /**
     * Gets a user by email
     * 
     * @param aEmailAddress the email address of user to find
     * @return the NomNomUser object with the given email address
     */
    public Optional<NomNomUser> getUserByEmail(String aEmailAddress) {
        // check if email is empty
        if (aEmailAddress == null || aEmailAddress.trim().length() == 0) {
            throw new IllegalArgumentException("Email address cannot be empty");
        }
        // check if user with email exists
        if (userRepository.existsByEmailAddress(aEmailAddress)) {
            return null;
        }

        return userRepository.findByEmailAddress(aEmailAddress);
    }


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

    /**
     * Deletes a user account by user ID.
     *
     * @param userId The ID of the user to delete.
     * @throws IllegalArgumentException if the user does not exist.
     */
    public void deleteUserById(int userId) {
        Optional<NomNomUser> user = userRepository.findById(userId);
        if (user.isPresent()) {
            userRepository.delete(user.get());
        } else {
            throw new IllegalArgumentException("User with ID '" + userId + "' not found.");
        }
    }
}
