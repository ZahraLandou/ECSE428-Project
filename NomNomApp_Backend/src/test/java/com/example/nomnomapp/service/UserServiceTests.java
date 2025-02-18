package com.example.nomnomapp.service;

import static org.junit.jupiter.api.Assertions.*;
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
    }

    /** Successfully deletes an existing user */
    @Test
    void testDeleteUserByUsername_Success() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));

        assertDoesNotThrow(() -> userService.deleteUserByUsername("testUser"));

        verify(userRepository, times(1)).delete(testUser);
    }

    /** Error Case: Attempting to delete a non-existent user should throw an exception */
    @Test
    void testDeleteUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("nonexistentUser")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> userService.deleteUserByUsername("nonexistentUser"));

        assertEquals("User with username 'nonexistentUser' not found.", exception.getMessage());
        verify(userRepository, never()).delete(any());
    }

    /** Edge Case: Attempting to delete with an empty username should throw an exception */
    @Test
    void testDeleteUserByUsername_EmptyUsername() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> userService.deleteUserByUsername(""));

        assertEquals("User with username '' not found.", exception.getMessage());
        verify(userRepository, never()).delete(any());
    }

    /** Edge Case: Attempting to delete with a null username should throw an exception */
    @Test
    void testDeleteUserByUsername_NullUsername() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> userService.deleteUserByUsername(null));

        assertEquals("User with username 'null' not found.", exception.getMessage());
        verify(userRepository, never()).delete(any());
    }
}
