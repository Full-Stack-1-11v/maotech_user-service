package cl.maotech.user_service.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import cl.maotech.user_service.dto.StatusEditDTO;
import cl.maotech.user_service.dto.UserDTO;
import cl.maotech.user_service.dto.UserEditDTO;
import cl.maotech.user_service.model.Role;
import cl.maotech.user_service.model.User;
import cl.maotech.user_service.service.UserService;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void testCreateUser() throws Exception {
            // Given
            User user = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
            // When
            Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(user);
            // Then
            mockMvc.perform(post("/api/v1/users/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                            "{\"userId\": 1, \"email\": \"test@mail.com\", \"password\": \"password123\", \"rut\": \"11.111.111-1\", \"firstName\": \"Admin1\", \"lastName\": \"Admin1\", \"status\": true, \"role\": null}"))
                            .andExpect(status().isCreated())
                            .andExpect(jsonPath("$.userId").value(1))
                            .andExpect(jsonPath("$.email").value("test@mail.com"))
                            .andExpect(jsonPath("$.rut").value("11.111.111-1"))
                            .andExpect(jsonPath("$.firstName").value("Admin1"))
                            .andExpect(jsonPath("$.lastName").value("Admin1"))
                            .andExpect(jsonPath("$.status").value(true));
    }

    @Test
    void testCreateUserBadRequest() throws Exception {
            // Given
            // When
            Mockito.when(userService.save(Mockito.any(User.class))).thenThrow(new RuntimeException("Bad Request"));
            // Then
            mockMvc.perform(post("/api/v1/users/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                            "{\"userId\": 1, \"email\": \"test@mail.com\", \"password\": \"password123\", \"rut\": \"11.111.111-1\", \"firstName\": \"Admin1\", \"lastName\": \"Admin1\", \"status\": true, \"role\": null}"))
                            .andExpect(status().isNotFound());
    }

    @Test
    void testListUsers() throws Exception {
            // Given
            UserDTO user = new UserDTO(1, "test@mail.com", "11.111.111-1", "Admin1", "Admin1", true, null);
            // When
            Mockito.when(userService.getAllAsDto()).thenReturn(List.of(user));
            // Then
            mockMvc.perform(get("/api/v1/users/list")
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$", hasSize(1)))
                            .andExpect(jsonPath("$[0].userId").value(1))
                            .andExpect(jsonPath("$[0].email").value("test@mail.com"))
                            .andExpect(jsonPath("$[0].firstName").value("Admin1"))
                            .andExpect(jsonPath("$[0].lastName").value("Admin1"))
                            .andExpect(jsonPath("$[0].status").value(true));
    }

    @Test
    void testListUsersNoContent() throws Exception {
            // Given
            // When
            Mockito.when(userService.getAllAsDto()).thenReturn(Collections.emptyList());
            // Then
            mockMvc.perform(get("/api/v1/users/list")
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isNoContent());
    }

    @Test
    void testListUsersInternalServerError() throws Exception {
            // Given
            // When
            Mockito.when(userService.getAllAsDto()).thenThrow(new RuntimeException("Internal Server Error"));
            // Then
            mockMvc.perform(get("/api/v1/users/list")
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isInternalServerError());
    }

    @Test
    void testListUsersAdmin() throws Exception {
            // Given
            User user = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
            // When
            Mockito.when(userService.findAll()).thenReturn(List.of(user));
            // Then
            mockMvc.perform(get("/api/v1/users/list/admin")
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$", hasSize(1)))
                            .andExpect(jsonPath("$[0].userId").value(1))
                            .andExpect(jsonPath("$[0].email").value("test@mail.com"))
                            .andExpect(jsonPath("$[0].rut").value("11.111.111-1"))
                            .andExpect(jsonPath("$[0].firstName").value("Admin1"))
                            .andExpect(jsonPath("$[0].lastName").value("Admin1"))
                            .andExpect(jsonPath("$[0].status").value(true));

    }

    @Test
    void testListUsersAdminNoContent() throws Exception {
            // Given
            // When
            Mockito.when(userService.findAll()).thenReturn(Collections.emptyList());
            // Then
            mockMvc.perform(get("/api/v1/users/list/admin")
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isNoContent());
    }

    @Test
    void testListUsersAdminNotFound() throws Exception {
            // Given
            // When
            Mockito.when(userService.findAll()).thenThrow(new RuntimeException("Not Found"));
            // Then
            mockMvc.perform(get("/api/v1/users/list/admin")
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isNotFound());
    }

    @Test
    void testFindByUserDTO() throws Exception {
            // Given
            User user = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
            UserDTO userDTO = new UserDTO(1, "test@mail.com", "11.111.111-1", "Admin1", "Admin1", true, null);
            // When
            Mockito.when(userService.findById(1)).thenReturn(user);
            Mockito.when(userService.toDto(user)).thenReturn(userDTO);
            // Then
            mockMvc.perform(get("/api/v1/users/1/details")
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.userId").value(1))
                            .andExpect(jsonPath("$.email").value("test@mail.com"))
                            .andExpect(jsonPath("$.firstName").value("Admin1"))
                            .andExpect(jsonPath("$.lastName").value("Admin1"))
                            .andExpect(jsonPath("$.status").value(true));
    }

    @Test
    void testFindByUserDTONotFound() throws Exception {
            // Given
            // When
            Mockito.when(userService.findById(1)).thenThrow(new RuntimeException("Not Found"));
            // Then
            mockMvc.perform(get("/api/v1/users/1/details")
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isNotFound());
    }

    @Test
    void testFindById() throws Exception {
            // Given
            User user = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
            // When
            Mockito.when(userService.findById(1)).thenReturn(user);
            // Then
            mockMvc.perform(get("/api/v1/users/1/details/admin")
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.userId").value(1))
                            .andExpect(jsonPath("$.email").value("test@mail.com"))
                            .andExpect(jsonPath("$.rut").value("11.111.111-1"))
                            .andExpect(jsonPath("$.firstName").value("Admin1"))
                            .andExpect(jsonPath("$.lastName").value("Admin1"))
                            .andExpect(jsonPath("$.status").value(true));
    }

    @Test
    void testFindByIdNotFound() throws Exception {
            // Given
            // When
            Mockito.when(userService.findById(1)).thenThrow(new RuntimeException("Not Found"));
            // Then
            mockMvc.perform(get("/api/v1/users/1/details/admin")
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isNotFound());
    }

    @Test
    void testFullUpdate() throws Exception {
            // Given
            User user = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
            // When
            Mockito.when(userService.findById(1)).thenReturn(user);
            // Then
            mockMvc.perform(put("/api/v1/users/1/full-update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                            "{\"userId\": 1, \"email\": \"test2@mail.com\", \"password\": \"password123\", \"rut\": \"11.111.111-1\", \"firstName\": \"Admin1\", \"lastName\": \"Admin1\", \"status\": false, \"role\": null}"))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.userId").value(1))
                            .andExpect(jsonPath("$.email").value("test2@mail.com"))
                            .andExpect(jsonPath("$.password").value("password123"))
                            .andExpect(jsonPath("$.rut").value("11.111.111-1"))
                            .andExpect(jsonPath("$.firstName").value("Admin1"))
                            .andExpect(jsonPath("$.lastName").value("Admin1"))
                            .andExpect(jsonPath("$.status").value(false));
    }

    @Test
    void testFullUpdateNotFound() throws Exception {
            // Given
            // When
            Mockito.when(userService.findById(1)).thenThrow(new RuntimeException("Not Found"));
            // Then
            mockMvc.perform(put("/api/v1/users/1/full-update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                            "{\"userId\": 1, \"email\": \"test@mail.com\", \"password\": \"password123\", \"rut\": \"11.111.111-1\", \"firstName\": \"Admin1\", \"lastName\": \"Admin1\", \"status\": false, \"role\": null}"))
                            .andExpect(status().isNotFound());
    }

    @Test
    void testUpdate() throws Exception {
            // Given
            User user = new User(1, "test2@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true,
                            null);
            // When
            Mockito.when(userService.findById(1)).thenReturn(user);
            Mockito.when(userService.updateFromDto(Mockito.any(UserEditDTO.class), Mockito.any(User.class)))
                            .thenReturn(user);
            // Then
            mockMvc.perform(put("/api/v1/users/1/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                            "{\"userId\": 1, \"email\": \"test2@mail.com\", \"firstName\": \"Admin1\", \"lastName\": \"Admin1\"}"))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.userId").value(1))
                            .andExpect(jsonPath("$.email").value("test2@mail.com"))
                            .andExpect(jsonPath("$.firstName").value("Admin1"))
                            .andExpect(jsonPath("$.lastName").value("Admin1"));
    }

    @Test
    void testUpdateNotFound() throws Exception {
            // Given
            // When
            Mockito.when(userService.findById(1)).thenThrow(new RuntimeException("Not Found"));
            // Then
            mockMvc.perform(put("/api/v1/users/1/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                            "{\"userId\": 1, \"email\": \"test@mail.com\", \"firstName\": \"Admin1\", \"lastName\": \"Admin1\"}"))
                            .andExpect(status().isNotFound());
    }

    @Test
    void testDeactivate() throws Exception {
            // Given
            User user = new User(1, "test2@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true,
                            null);
            // When
            Mockito.when(userService.findById(1)).thenReturn(user);
            Mockito.when(userService.updateStatusDto(Mockito.any(StatusEditDTO.class), Mockito.any(User.class)))
                            .thenReturn(user);
            // Then
            mockMvc.perform(put("/api/v1/users/1/deactivate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                            "{\"status\": false}"))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.status").value(true));
    }

    @Test
    void testDeactivateNotFound() throws Exception {
            // Given
            // When
            Mockito.when(userService.findById(1)).thenThrow(new RuntimeException("Not Found"));
            // Then
            mockMvc.perform(put("/api/v1/users/1/deactivate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                            "{\"status\": false}"))
                            .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateRole() throws Exception {
            // Given
            Role role = new Role(1, "Administrador");
            User user = new User(1, "test", "password123", "11.111.111-1", "Admin1", "Admin1", true, role);
            // When
            Mockito.when(userService.findById(1)).thenReturn(user);
            Mockito.when(userService.updateRoleDto(Mockito.anyInt(), Mockito.any(User.class)))
                            .thenReturn(user);
            // Then
            mockMvc.perform(put("/api/v1/users/1/role/edit")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"roleId\": 1, \"roleName\": \"Administrador\"}"))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.role.roleId").value(1))
                            .andExpect(jsonPath("$.role.roleName").value("Administrador"));
    }

    @Test
    void testUpdateRoleNotFound() throws Exception {
            // Given
            // When
            Mockito.when(userService.findById(1)).thenThrow(new RuntimeException("Not Found"));
            // Then
            mockMvc.perform(put("/api/v1/users/1/role/edit")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"roleId\": 1, \"roleName\": \"Administrador\"}"))
                            .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUser() throws Exception {
            // Given
            Integer userId = 1;
            // When
            Mockito.doNothing().when(userService).delete(userId);
            // Then
            mockMvc.perform(delete("/api/v1/users/1/delete"))
                            .andExpect(status().isNoContent());
        }

    @Test
    void testDeleteUserNotFound() throws Exception {
            // Given
            Integer userId = 1;
            // When
            Mockito.doThrow(new RuntimeException("User not found")).when(userService).delete(userId);
            // Then
            mockMvc.perform(delete("/api/v1/users/1/delete"))
                            .andExpect(status().isNotFound());
    }

    @Test
    void testFindInactives() throws Exception {
            // Given
            User user = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", false, null);
            // When
            Mockito.when(userService.findByStatusFalse()).thenReturn(List.of(user));
            // Then
            mockMvc.perform(get("/api/v1/users/inactives/admin")
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$", hasSize(1)))
                            .andExpect(jsonPath("$[0].userId").value(1))
                            .andExpect(jsonPath("$[0].email").value("test@mail.com"))
                            .andExpect(jsonPath("$[0].rut").value("11.111.111-1"))
                            .andExpect(jsonPath("$[0].firstName").value("Admin1"))
                            .andExpect(jsonPath("$[0].lastName").value("Admin1"))
                            .andExpect(jsonPath("$[0].status").value(false));
    }

    @Test
    void testFindInactivesNoContent() throws Exception {
        // Given
        // When
        Mockito.when(userService.findByStatusFalse()).thenReturn(Collections.emptyList());
        // Then
        mockMvc.perform(get("/api/v1/users/inactives/admin")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent());
    }

    @Test
    void testFindInactivesNotFound() throws Exception {
        // Given
        // When
        Mockito.when(userService.findByStatusFalse()).thenThrow(new RuntimeException("Not found"));
        // Then
        mockMvc.perform(get("/api/v1/users/inactives/admin")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
    }

    @Test
    void testFindInactivesDto() throws Exception {
        // Given
        UserDTO userDTO = new UserDTO(1, "test@mail.com", "11.111.111-1", "Admin1", "Admin1", false, null);
        // When
        Mockito.when(userService.findInactivesDto()).thenReturn(List.of(userDTO));
        // Then
        mockMvc.perform(get("/api/v1/users/inactives")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(1)))
                        .andExpect(jsonPath("$[0].userId").value(1))
                        .andExpect(jsonPath("$[0].email").value("test@mail.com"))
                        .andExpect(jsonPath("$[0].rut").value("11.111.111-1"))
                        .andExpect(jsonPath("$[0].firstName").value("Admin1"))
                        .andExpect(jsonPath("$[0].lastName").value("Admin1"))
                        .andExpect(jsonPath("$[0].status").value(false));
    }

    @Test
    void testFindInactivesDtoNoContent() throws Exception {
        // Given
        // When
        Mockito.when(userService.findInactivesDto()).thenReturn(Collections.emptyList());
        // Then
        mockMvc.perform(get("/api/v1/users/inactives")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent());
    }

    @Test
    void testFindInactivesDtoNotFound() throws Exception {
        // Given
        // When
        Mockito.when(userService.findInactivesDto()).thenThrow(new RuntimeException("Not found"));
        // Then
        mockMvc.perform(get("/api/v1/users/inactives")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteStatusFalse() throws Exception {
        // Given
        // When
        Mockito.doNothing().when(userService).deleteStatusFalse();
        // Then
        mockMvc.perform(delete("/api/v1/users/inactives/delete"))
                        .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteStatusFalseNotFound() throws Exception {
        // Given
        // When
        Mockito.doThrow(new RuntimeException("Not found")).when(userService).deleteStatusFalse();
        // Then
        mockMvc.perform(delete("/api/v1/users/inactives/delete"))
                        .andExpect(status().isNotFound());
    }

    @Test
    void testLogin() throws Exception {
        // Given
        // When
        Mockito.when(userService.login("test@mail.com", "password123")).thenReturn(true);
        // Then
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":  \"test@mail.com\", \"password\": \"password123\"}"))
                        .andExpect(status().isOk())
                        .andExpect(content().string("Logged-In"));
    }

    @Test
    void testLoginUnauthorized() throws Exception {
        // Given
        // When
        Mockito.when(userService.login("test@mail.com", "wrongpassword")).thenReturn(false);
        // Then
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":  \"test@mail.com\", \"password\": \"wrongpassword\"}"))
                        .andExpect(status().isUnauthorized())
                        .andExpect(content().string("Wrong Credentials"));
    }
    
    @Test
    void testLoginNotFound() throws Exception {
        // Given
        // When
        Mockito.when(userService.login("test@mail.com", "password123")).thenThrow(new RuntimeException("Not Found"));
        // Then
        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":  \"test@mail.com\", \"password\": \"password123\"}"))
                        .andExpect(status().isNotFound());
    }
}