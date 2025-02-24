package com.example.nomnomapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private NomNomUser testUser;

    @BeforeEach
    void setUp() {
        testUser = new NomNomUser("testUser", "test@example.com", "password123");
        testUser.setUserId(1);
    }

    /** Successfully deletes an existing user by userId */
    @Test
    void testDeleteUserById_Success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));

        assertDoesNotThrow(() -> userService.deleteUserById(1));

        verify(userRepository).deleteById(1);
    }

    /** Deleting a non-existent user should throw an exception */
    @Test
    void testDeleteUserById_UserNotFound() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUserById(99);
        });

        assertEquals("User with ID '99' not found.", exception.getMessage());
    }

    /** Edge Case: Deleting with a negative userId */
    @Test
    void testDeleteUserById_NegativeId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUserById(-5);
        });

        assertEquals("Invalid user ID: -5", exception.getMessage());
    }

    /** Edge Case: Deleting with userId = 0 */
    @Test
    void testDeleteUserById_ZeroId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUserById(0);
        });

        assertEquals("Invalid user ID: 0", exception.getMessage());
    }

    /** Edge Case: Deleting with an extremely large userId */
    @Test
    void testDeleteUserById_LargeId() {
        int largeId = Integer.MAX_VALUE;
        when(userRepository.findById(largeId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUserById(largeId);
        });

        assertEquals("User with ID '" + largeId + "' not found.", exception.getMessage());
    }

    /** Ensures deleteUserById does not throw if called multiple times */
    @Test
    void testDeleteUserById_MultipleDeletions() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> userService.deleteUserById(1));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUserById(1);
        });

        assertEquals("User with ID '1' not found.", exception.getMessage());
    }
}
