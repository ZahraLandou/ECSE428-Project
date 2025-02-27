package com.example.nomnomapp.controller;

import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.service.UserService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Creates a new user.
     * Example: POST /api/users
     * 
     * @param username the unique username of new user to create
     * @param email    the unique email of new user to create
     * @param password the password of new user to create
     * 
     * @return user and 201 Created if successful, 400 Bad Request if unsuccessful
     */
    @PostMapping
    public ResponseEntity<NomNomUser> createUser(@RequestBody NomNomUser user) {
        try {
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Gets a user by username.
     * Example: GET /api/users/username/{username}
     * 
     * @param username the username of user to find
     * 
     * @return user and 200 OK if successful, 404 Not Found if unsuccessful
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<NomNomUser> getUserByUsername(@PathVariable String username) {
        Optional<NomNomUser> user = userService.getUserByUsername(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Gets a user by email.
     * Example: GET /api/users/email/{email}
     *
     * @param email the email address of user to find
     * @return user and 200 OK if successful, 404 Not Found if unsuccessful
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<NomNomUser> getUserByEmail(@PathVariable String email) {
        Optional<NomNomUser> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Gets the profile picture of a user by username.
     * Example: GET /api/users/username/{username}/profile-picture
     *
     * @param username the username of the user whose profile pic to find
     * @return profile pic and 200 OK if successful, 404 Not Found if unsuccessful
     */
    @GetMapping("/username/{username}/profile-picture")
    public ResponseEntity<String> getUserProfilePicture(@PathVariable String username) {
        try {
            String profilePicture = userService.getUserProfilePicture(username);
            return ResponseEntity.ok(profilePicture);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Gets the biography of a user by username.
     * Example: GET /api/users/username/{username}/biography
     *
     * @param username the username of the user whose biography to find
     * @return biography and 200 OK if successful, 404 Not Found if unsuccessful
     */
    @GetMapping("/username/{username}/biography")
    public ResponseEntity<String> getUserBiography(@PathVariable String username) {
        try {
            String biography = userService.getUserBiography(username);
            return ResponseEntity.ok(biography);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Gets the email of a user by username.
     * Example: GET /api/users/username/{username}/email
     *
     * @param username the username of the user with given email address
     * @return email and 200 OK if successful, 404 Not Found if unsuccessful
     */
    @GetMapping("/username/{username}/email")
    public ResponseEntity<String> getUserEmail(@PathVariable String username) {
        try {
            String email = userService.getUserEmail(username);
            return ResponseEntity.ok(email);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Gets the username of a user by email.
     * Example: GET /api/users/email/{email}/username
     *
     * @param email the email of the user with given username
     * @return username and 200 OK if successful, 404 Not Found if unsuccessful
     */
    @GetMapping("/email/{email}/username")
    public ResponseEntity<String> getUserUsername(@PathVariable String email) {
        try {
            String username = userService.getUserUsername(email);
            return ResponseEntity.ok(username);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Updates the profile picture of a user.
     * Example: PUT /api/users/username/{username}/profile-picture
     *
     * @param username       the username of the user whose profile pic to set
     * @param profilePicture the new profile pic
     * @return 200 OK if successful, 400 Bad Request if unsuccessful
     */
    @PutMapping("/username/{username}/profile-picture")
    public ResponseEntity<String> setUserProfilePicture(@PathVariable String username,
            @RequestBody String profilePicture) {
        try {
            userService.setUserProfilePicture(username, profilePicture);
            return ResponseEntity.ok("Profile picture updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Updates the biography of a user.
     * Example: PUT /api/users/username/{username}/biography
     *
     * @param username  the username of the user whose biography to set
     * @param biography the new biography
     * @return 200 OK if successful, 400 Bad Request if unsuccessful
     */
    @PutMapping("/username/{username}/biography")
    public ResponseEntity<String> setUserBiography(@PathVariable String username,
            @RequestBody String biography) {
        try {
            userService.setUserBiography(username, biography);
            return ResponseEntity.ok("Biography updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Updates the password of a user.
     * Example: PUT /api/users/username/{username}/password
     *
     * @param username the username of the user whose password to set
     * @param password the new password
     * @return 200 OK if successful, 400 Bad Request if unsuccessful
     */
    @PutMapping("/username/{username}/password")
    public ResponseEntity<String> setUserPassword(@PathVariable String username,
            @RequestBody String password) {
        try {
            userService.setUserPassword(username, password);
            return ResponseEntity.ok("Password updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Updates the email of a user.
     * Example: PUT /api/users/username/{username}/email
     *
     * @param username the username of the user whose email to set
     * @param email    the new email
     * @return 200 OK if successful, 400 Bad Request if unsuccessful
     */
    @PutMapping("/username/{username}/email")
    public ResponseEntity<String> setUserEmail(@PathVariable String username,
            @RequestBody String email) {
        try {
            userService.setUserEmail(username, email);
            return ResponseEntity.ok("Email updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Updates the username of a user.
     * Example: PUT /api/users/email/{email}/username
     *
     * @param email    the email address of the user whose username to set
     * @param username the new username to set
     * @return 200 OK if successful, 400 Bad Request if unsuccessful
     */
    @PutMapping("/email/{email}/username")
    public ResponseEntity<String> setUserUsername(@PathVariable String email,
            @RequestBody String username) {
        try {
            userService.setUserUsername(email, username);
            return ResponseEntity.ok("Username updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Deletes a user profile by userId.
     *
     * @param userId The ID of the user to delete.
     * @return HTTP 200 OK if deleted, or HTTP 404 if the user is not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") int userId) {
        try {
            userService.deleteUserById(userId);
            return ResponseEntity.ok("User with ID " + userId + " deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
