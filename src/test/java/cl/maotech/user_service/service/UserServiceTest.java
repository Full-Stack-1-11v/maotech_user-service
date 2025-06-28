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

import cl.maotech.user_service.dto.AuthDTO;
import cl.maotech.user_service.dto.StatusEditDTO;
import cl.maotech.user_service.dto.UserDTO;
import cl.maotech.user_service.dto.UserEditDTO;
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
        List<User> result = userService.findAll();
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

    @Test
    public void testFindByStatusFalse(){
        // Given
        List<User> inactiveUsers = new ArrayList<>();
        inactiveUsers.add(new User(1, "test@,mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", false, null));
        // When
        when(userRepository.findByStatusFalse()).thenReturn(inactiveUsers);
        // Then
        List<User> result = userService.findByStatusFalse();
        assertEquals(inactiveUsers, result);
        assertEquals(1, result.size());
        verify(userRepository).findByStatusFalse();
    }

    @Test
    public void testDeleteStatusFalse(){
        // Given
        List<User> inactiveUsers = new ArrayList<>();
        inactiveUsers.add(new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", false, null));
        // When
        when(userRepository.findByStatusFalse()).thenReturn(inactiveUsers);
        // Then
        userService.deleteStatusFalse();
        verify(userRepository).deleteAll(inactiveUsers);
    }

    @Test
    public void testFindInactivesDto(){
        // Given
        List<UserDTO> inactiveUsers = new ArrayList<>();
        inactiveUsers.add(new UserDTO(1, "test@mail.com", "11.111.111-1", "Admin1", "Admin1", false, null));
        // When
        when(userRepository.findInactivesAsDto()).thenReturn(inactiveUsers);
        // Then
        List<UserDTO> result = userService.findInactivesDto();
        assertEquals(inactiveUsers, result);
        assertEquals(1, result.size());
        verify(userRepository).findInactivesAsDto();
    }

    @Test
    public void testLogin(){
        // Given
        String email = "test@mail.com";
        String password = "password123";
        User user = new User(1, email, password, "11.111.111-1", "Admin1", "Admin1", true, null);
        // When
        when(userRepository.findByEmail(email)).thenReturn(user);
        // Then
        boolean loginResult = userService.login(email, password);
        assertEquals(true, loginResult);
        verify(userRepository).findByEmail(email);
    }

    @Test
    public void testLoginNullValues(){
        // Given
        String email = null;
        String password = null;
        // When
        boolean loginResult = userService.login(email, password);
        // Then
        assertEquals(false, loginResult);
        verify(userRepository).findByEmail(email);
    }

    @Test
    public void testToDto(){
        // Given
        User user = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
        // When
        UserDTO userDTO = userService.toDto(user);
        // Then
        assertEquals(1, userDTO.getUserId());
        assertEquals("test@mail.com", userDTO.getEmail());
        assertEquals("11.111.111-1", userDTO.getRut());
        assertEquals("Admin1", userDTO.getFirstName());
        assertEquals("Admin1", userDTO.getLastName());
        assertEquals(true, userDTO.getStatus());
        assertEquals(null, userDTO.getRole());
    }

    @Test
    public void testToDtoNullUser(){
        // Given
        User user = null;
        // When
        UserDTO userDTO = userService.toDto(user);
        // Then
        assertEquals(null, userDTO);
    }

    @Test
    public void testGetAllAsDto(){
        // Given
        List<UserDTO> userDTOs = new ArrayList<>();
        userDTOs.add(new UserDTO(1, "test@mail.com", "11.111.111-1", "Admin1", "Admin1", true, null));
        // When
        when(userRepository.findAllAsDto()).thenReturn(userDTOs);
        // Then
        List<UserDTO> result = userService.getAllAsDto();
        assertEquals(userDTOs, result);
        assertEquals(1, result.size());
        verify(userRepository).findAllAsDto();
    }

    @Test
    public void testToEditDto(){
        // Given
        User user = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
        // When
        UserEditDTO userEditDTO = userService.toEditDto(user);
        // Then
        assertEquals(1, userEditDTO.getUserId());
        assertEquals("test@mail.com", userEditDTO.getEmail());
        assertEquals("Admin1", userEditDTO.getFirstName());
        assertEquals("Admin1", userEditDTO.getLastName());
    }

    @Test
    public void testToEditDtoNullUser(){
        // Given
        User user = null;
        // When
        UserEditDTO userEditDTO = userService.toEditDto(user);
        // Then
        assertEquals(null, userEditDTO);
    }

    @Test
    public void testUpdateFromDto(){
        // Given
        UserEditDTO userEditDTO = new UserEditDTO(1, "test@mail.com", "Admin1", "Admin1");
        User user = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
        // When
        User updatedUser = userService.updateFromDto(userEditDTO, user);
        // Then
        assertEquals(1, updatedUser.getUserId());
        assertEquals("test@mail.com", updatedUser.getEmail());
        assertEquals("Admin1", updatedUser.getFirstName());
        assertEquals("Admin1", updatedUser.getLastName());
    }

    @Test
    public void testUpdateFromDtoNullValues(){
        // Given
        UserEditDTO userEditDTO = new UserEditDTO(null, null, null, null);
        User user = new User(null, null, null, null, null, null, null, null);
        // When
        User updatedUser = userService.updateFromDto(userEditDTO, user);
        // Then
        assertEquals(null, updatedUser.getUserId());
        assertEquals(null, updatedUser.getEmail());
        assertEquals(null, updatedUser.getFirstName());
        assertEquals(null, updatedUser.getLastName());
    }

    @Test
    public void testUpdateFromDtoNullDto(){
        // Given
        UserEditDTO userEditDTO = null;
        User user = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
        // When
        User updatedUser = userService.updateFromDto(userEditDTO, user);
        // Then
        assertEquals(1, updatedUser.getUserId());
        assertEquals("test@mail.com", updatedUser.getEmail());
        assertEquals("password123", updatedUser.getPassword());
        assertEquals("11.111.111-1", updatedUser.getRut());
        assertEquals("Admin1", updatedUser.getFirstName());
        assertEquals("Admin1", updatedUser.getLastName());
        assertEquals(true, updatedUser.getStatus());
        assertEquals(null, updatedUser.getRole());
    }

    @Test
    public void testUpdateFromDtoNullUser() {
        // Given
        UserEditDTO userEditDTO = new UserEditDTO(1, "test@mail.com", "Admin1", "Admin1");
        User user = null;
        // When
        User updatedUser = userService.updateFromDto(userEditDTO, user);
        // Then
        assertEquals(null, updatedUser);
    }

    @Test
    public void testUpdateStatusDto(){
        // Given
        StatusEditDTO statusEditDTO = new StatusEditDTO(false);
        User user = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
        // When
        User updatedUser = userService.updateStatusDto(statusEditDTO, user);
        // Then
        assertEquals(1, updatedUser.getUserId());
        assertEquals("test@mail.com", updatedUser.getEmail());
        assertEquals("password123", updatedUser.getPassword());
        assertEquals("11.111.111-1", updatedUser.getRut());
        assertEquals("Admin1", updatedUser.getFirstName());
        assertEquals("Admin1", updatedUser.getLastName());
        assertEquals(false, updatedUser.getStatus());
        assertEquals(null, updatedUser.getRole());
    }

    @Test
    public void testUpdateStatusDtoNullValues(){
        // Given
        StatusEditDTO statusEditDTO = new StatusEditDTO(null);
        User user = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
        // When
        User updatedUser = userService.updateStatusDto(statusEditDTO, user);
        // Then
        assertEquals(1, updatedUser.getUserId());
        assertEquals("test@mail.com", updatedUser.getEmail());
        assertEquals("password123", updatedUser.getPassword());
        assertEquals("11.111.111-1", updatedUser.getRut());
        assertEquals("Admin1", updatedUser.getFirstName());
        assertEquals("Admin1", updatedUser.getLastName());
        assertEquals(true, updatedUser.getStatus());
        assertEquals(null, updatedUser.getRole());
    }

    @Test
    public void testUpdateStatusDtoNullStatus(){
        // Given
        StatusEditDTO statusEditDTO = null;
        User user = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
        // When
        User updatedUser = userService.updateStatusDto(statusEditDTO, user);
        // Then
        assertEquals(1, updatedUser.getUserId());
        assertEquals("test@mail.com", updatedUser.getEmail());
        assertEquals("password123", updatedUser.getPassword());
        assertEquals("11.111.111-1", updatedUser.getRut());
        assertEquals("Admin1", updatedUser.getFirstName());
        assertEquals("Admin1", updatedUser.getLastName());
        assertEquals(true, updatedUser.getStatus());
        assertEquals(null, updatedUser.getRole());
    }
    
    @Test
    public void testUpdateStatusDtoNullUser() {
        // Given
        StatusEditDTO statusEditDTO = new StatusEditDTO(false);
        User user = null;
        // When
        User updatedUser = userService.updateStatusDto(statusEditDTO, user);
        // Then
        assertEquals(null, updatedUser);
    }
    
    @Test
    public void testUpdateRoleDto(){
        // Given
        Integer roleId = 1;
        User user = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
        // When
        User updatedUser = userService.updateRoleDto(roleId, user);
        // Then
        assertEquals(1, updatedUser.getUserId());
        assertEquals("test@mail.com", updatedUser.getEmail());
        assertEquals("Admin1", updatedUser.getFirstName());
        assertEquals("Admin1", updatedUser.getLastName());
        assertEquals(roleId, updatedUser.getRole().getRoleId());
    }

    @Test
    public void testUpdateRoleDtoNullValues(){
        // Given
        Integer roleId = null;
        User user = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
        // When
        User updatedUser = userService.updateRoleDto(roleId, user);
        // Then
        assertEquals(1, updatedUser.getUserId());
        assertEquals("test@mail.com", updatedUser.getEmail());
        assertEquals("Admin1", updatedUser.getFirstName());
        assertEquals("Admin1", updatedUser.getLastName());
        assertEquals(null, updatedUser.getRole());
    }

    @Test
    public void testAuthDto(){
        // Given
        User user = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
        // When
        AuthDTO authDTO = userService.authDto(user);
        // Then
        assertEquals("test@mail.com", authDTO.getEmail());
        assertEquals("password123", authDTO.getPassword());
    }

    @Test
    public void testAuthDtoNullUser(){
        // Given
        User user = null;
        // When
        AuthDTO authDTO = userService.authDto(user);
        // Then
        assertEquals(null, authDTO);
    }
}
