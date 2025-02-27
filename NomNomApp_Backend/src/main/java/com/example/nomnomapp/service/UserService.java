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

        validateUsername(aUsername);
        validateEmail(aEmailAddress);

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
        return userRepository.findByUsername(aUsername);
    }

    public NomNomUser getNomNomUserByName(String name) {

            
    return userRepository.findByUsername(name)
    .orElseThrow(() -> new IllegalArgumentException("User with name "+ name+ " not found"));
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
        return userRepository.findByEmailAddress(aEmailAddress);
    }

    /**
     * Gets user profile pic, given a username
     * 
     * @param aUsername the username of the user whose profile pic to find
     * @return the profile pic of the user with given username
     */
    public String getUserProfilePicture(String aUsername) {
        // get the NomNomUser object with the given username
        Optional<NomNomUser> userOptional = userRepository.findByUsername(aUsername);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User with username '" + aUsername + "' not found.");
        }
        NomNomUser user = userOptional.get();

        return user.getProfilePicture();
    }

    /**
     * Gets user biography, given a username
     * 
     * @param aUsername the username of the user whose biography to find
     * @return the biography of the user with given username
     */
    public String getUserBiography(String aUsername) {
        // get the NomNomUser object with the given username
        Optional<NomNomUser> userOptional = userRepository.findByUsername(aUsername);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User with username '" + aUsername + "' not found.");
        }
        NomNomUser user = userOptional.get();

        return user.getBiography();
    }

    /**
     * Gets user password, given a username
     * 
     * @param aUsername the username of the user whose password to find
     * @return the password of the user with given username
     */
    public String getUserPassword(String aUsername) {
        // get the NomNomUser object with the given username
        Optional<NomNomUser> userOptional = userRepository.findByUsername(aUsername);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User with username '" + aUsername + "' not found.");
        }
        NomNomUser user = userOptional.get();

        return user.getPassword();
    }

    /**
     * Gets user email, given a username
     * 
     * @param aUsername the username of the user whose email to find
     * @return the email of the user with given username
     */
    public String getUserEmail(String aUsername) {
        // get the NomNomUser object with the given username
        Optional<NomNomUser> userOptional = userRepository.findByUsername(aUsername);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User with username '" + aUsername + "' not found.");
        }
        NomNomUser user = userOptional.get();

        return user.getEmailAddress();
    }

    /**
     * Gets user username, given an email address
     * 
     * @param aEmailAddress the email address of the user whose username to find
     * @return the username of the user with given email address
     */
    public String getUserUsername(String aEmailAddress) {
        // get the NomNomUser object with the given username
        Optional<NomNomUser> userOptional = userRepository.findByEmailAddress(aEmailAddress);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User with email '" + aEmailAddress + "' not found.");
        }
        NomNomUser user = userOptional.get();

        return user.getUsername();
    }

    /**
     * Sets user profile pic, given a username and a profile pic
     * 
     * @param aUsername       the username of the user whose profile pic to set
     * @param aProfilePicture the new profile pic
     * @return whether the profile pic was set successfully
     */
    public boolean setUserProfilePicture(String aUsername, String aProfilePicture) {
        // get the NomNomUser object with the given username
        Optional<NomNomUser> userOptional = userRepository.findByUsername(aUsername);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User with username '" + aUsername + "' not found.");
        }
        NomNomUser user = userOptional.get();

        // check if profile pic is empty
        if (aProfilePicture == null || aProfilePicture.trim().length() == 0) {
            throw new IllegalArgumentException("Profile picture cannot be empty");
        }
        user.setProfilePicture(aProfilePicture);
        userRepository.save(user);
        return true;
    }

    /**
     * Sets user biography, given a username and a biography
     * 
     * @param aUsername  the username of the user whose biography to set
     * @param aBiography the new biography
     * @return whether the biography was set successfully
     */
    public boolean setUserBiography(String aUsername, String aBiography) {
        // get the NomNomUser object with the given username
        Optional<NomNomUser> userOptional = userRepository.findByUsername(aUsername);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User with username '" + aUsername + "' not found.");
        }
        NomNomUser user = userOptional.get();

        // check if biography is empty
        if (aBiography == null || aBiography.trim().length() == 0) {
            throw new IllegalArgumentException("Biography cannot be empty");
        }
        user.setBiography(aBiography);
        userRepository.save(user);
        return true;
    }

    /**
     * Sets user password, given a username and a password
     * 
     * @param aUsername the username of the user whose password to set
     * @param aPassword the new password
     * @return whether the password was set successfully
     */
    public boolean setUserPassword(String aUsername, String aPassword) {
        // get the NomNomUser object with the given username
        Optional<NomNomUser> userOptional = userRepository.findByUsername(aUsername);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User with username '" + aUsername + "' not found.");
        }
        NomNomUser user = userOptional.get();

        // check if password is empty
        if (aPassword == null || aPassword.trim().length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        user.setPassword(aPassword);
        userRepository.save(user);
        return true;
    }

    /**
     * Sets user email, given a username and an email address
     * 
     * @param aUsername     the username of the user whose email to set
     * @param aEmailAddress the new email address
     * @return whether the email was set successfully
     */
    public boolean setUserEmail(String aUsername, String aEmailAddress) {
        // get the NomNomUser object with the given username
        Optional<NomNomUser> userOptional = userRepository.findByUsername(aUsername);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User with username '" + aUsername + "' not found.");
        }
        NomNomUser user = userOptional.get();

        // check if email is empty
        if (aEmailAddress == null || aEmailAddress.trim().length() == 0) {
            throw new IllegalArgumentException("Email address cannot be empty");
        }
        user.setEmailAddress(aEmailAddress);
        userRepository.save(user);
        return true;
    }

    /**
     * Sets user username, given an email address and a new username
     * 
     * @param aEmailAddress the email address of the user whose username to set
     * @param aNewUsername  the new username
     * @return whether the username was set successfully
     */
    public boolean setUserUsername(String aEmailAddress, String aNewUsername) {
        // get the NomNomUser object with the given email address
        Optional<NomNomUser> userOptional = userRepository.findByEmailAddress(aEmailAddress);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User with email '" + aEmailAddress + "' not found.");
        }
        NomNomUser user = userOptional.get();

        // check if username is empty
        if (aNewUsername == null || aNewUsername.trim().length() == 0) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        user.setUsername(aNewUsername);
        userRepository.save(user);
        return true;
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

    public void deleteAllUsers(){
        userRepository.deleteAll();
    }
    public void validateUsername(String username) {
        if (!username.matches("^[a-zA-Z0-9_]*$")) {
            throw new IllegalArgumentException("Invalid username format.");
        }
    }

    public void validateEmail(String email) {
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }

}
