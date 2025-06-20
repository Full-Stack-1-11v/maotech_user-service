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

import cl.maotech.user_service.dto.UserDTO;
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
}