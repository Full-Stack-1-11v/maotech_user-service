package cl.maotech.user_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import cl.maotech.user_service.model.User;
import cl.maotech.user_service.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testSave(){
        // Given
        User user = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
        // When
        when(userRepository.save(user)).thenReturn(user);
        // Then
        User savedUser = userService.save(user);
        assertEquals(user, savedUser);
        verify(userRepository).save(user);
    }

    @Test
    public void testFindAll(){
        // Given
        List<User> users = new ArrayList<>();
        // When
        when(userRepository.findAll()).thenReturn(users);
        // Then
        List<User> result = userRepository.findAll();
        assertEquals(users, result);
        assertEquals(0, result.size());
        verify(userRepository).findAll();
    }

    @Test
    public void testFindById(){
        // Given
        int userId = 1;
        User user = new User(userId, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
        // When
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        // Then
        User foundUser = userService.findById(userId);
        assertEquals(userId, foundUser.getUserId());
        assertEquals(user, foundUser);
        verify(userRepository).findById(userId);
    }

    @Test
    public void testDelete(){
        // Given
        int userId = 1;
        List<User> users = new ArrayList<>();
        users.add(new User(userId, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null));
        // When
        when(userRepository.existsById(userId)).thenReturn(true);
        // Then
        userService.delete(userId);
        verify(userRepository).deleteById(userId);
    }
}
