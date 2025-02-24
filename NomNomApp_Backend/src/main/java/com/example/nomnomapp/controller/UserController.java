package com.example.nomnomapp.controller;

import com.example.nomnomapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

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
