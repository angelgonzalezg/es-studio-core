package com.esstudio.platform.esstudiocore.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.esstudio.platform.esstudiocore.dto.UserDto;
import com.esstudio.platform.esstudiocore.entities.User;
import com.esstudio.platform.esstudiocore.mapper.UserMapper;
import com.esstudio.platform.esstudiocore.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @Mock
    private ClientProfileService clientProfileService;

    @Mock
    private DesignerProfileService designerProfileService;

    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserDto testUserDto;

    // Initializes common test data before each test execution
    @BeforeEach
    void setUp() {
        // Create a sample User entity
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");

        // Create a sample User DTO
        testUserDto = new UserDto();
        testUserDto.setId(1L);
        testUserDto.setEmail("test@example.com");
    }

    /**
     * Test case for getUsers().
     * Verifies that the service returns a list of UserDto objects when users exist
     */
    @Test
    void getUsers_ShouldReturnListOfUserDtos() {
        // Arrange: Prepare mock data and behavior
        List<User> users = Arrays.asList(testUser);
        List<UserDto> userDtos = Arrays.asList(testUserDto);

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDto(testUser)).thenReturn(testUserDto);

        // Act: Call the method under test
        List<UserDto> result = userService.getUsers();

        // Assert: Verify the result and interactions
        assertEquals(1, result.size());
        assertEquals("test@example.com", result.get(0).getEmail());
        verify(userRepository).findAll();
        verify(userMapper).toDto(testUser);
    }

    /**
     * Test case for existsByEmail().
     * Verifies that the method returns true when the email exists in the database.
     */
    @Test
    void existsByEmail_ShouldReturnTrue_WhenEmailExists() {
        String email = "test@example.com";
        when(userRepository.existsUserByEmail(email)).thenReturn(true);

        boolean result = userService.existsByEmail(email);

        assertTrue(result);
        verify(userRepository).existsUserByEmail(email);
    }

    /**
     * Test case for existsByEmail().
     * Verifies that the method returns false when the email does NOT exist in the database.
     */

    @Test
    void existsByEmail_ShouldReturnFalse_WhenEmailDoesNotExist() {
        String email = "nonexistent@example.com";
        when(userRepository.existsUserByEmail(email)).thenReturn(false);

        boolean result = userService.existsByEmail(email);

        assertFalse(result);
        verify(userRepository).existsUserByEmail(email);
    }
}