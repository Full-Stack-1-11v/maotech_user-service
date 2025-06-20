package cl.maotech.user_service.controller;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import cl.maotech.user_service.model.Role;
import cl.maotech.user_service.service.RoleService;

@WebMvcTest(RoleController.class)
public class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoleService roleService;

    @Test
    void testCreateRole() throws Exception {
        // Given
        Role role = new Role();
        role.setRoleId(1);
        role.setRoleName("Administrador");
        // When
        Mockito.when(roleService.save(Mockito.any(Role.class))).thenReturn(role);
        // Then
        mockMvc.perform(post("/api/v1/roles/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"roleId\": 1, \"roleName\": \"Administrador\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roleId").value(1))
                .andExpect(jsonPath("$.roleName").value("Administrador"));
    }

    @Test
    void testCreateRoleBadRequest() throws Exception {
        // Given
        Role role = new Role();
        role.setRoleId(1);
        role.setRoleName("Administrador");
        // When
        Mockito.when(roleService.save(Mockito.any(Role.class))).thenThrow(new RuntimeException("Bad Request"));
        // Then
        mockMvc.perform(post("/api/v1/roles/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"roleId\": 1, \"roleName\": \"Administrador\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testListRoles() throws Exception {
        // Given
        Role role1 = new Role();
        role1.setRoleId(1);
        role1.setRoleName("Administrador");
        Role role2 = new Role();
        role2.setRoleId(2);
        role2.setRoleName("Usuario");
        List<Role> roles = List.of(role1, role2);
        // When
        Mockito.when(roleService.findAll()).thenReturn(roles);
        // Then
        mockMvc.perform(get("/api/v1/roles/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].roleId").value(1))
                .andExpect(jsonPath("$[0].roleName").value("Administrador"))
                .andExpect(jsonPath("$[1].roleId").value(2))
                .andExpect(jsonPath("$[1].roleName").value("Usuario"));
    }

    @Test
    void testListRolesEmpty() throws Exception {
        // Given
        List<Role> roles = List.of();
        // When
        Mockito.when(roleService.findAll()).thenReturn(roles);
        // Then
        mockMvc.perform(get("/api/v1/roles/list"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testListRolesNotFound() throws Exception {
        // Given
        List<Role> roles = List.of();
        // When
        Mockito.when(roleService.findAll()).thenThrow(new RuntimeException("Roles not found"));
        // Then
        mockMvc.perform(get("/api/v1/roles/list"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindRoleById() throws Exception {
        // Given
        Role role = new Role();
        role.setRoleId(1);
        role.setRoleName("Administrador");
        // When
        Mockito.when(roleService.findById(1)).thenReturn(role);
        // Then
        mockMvc.perform(get("/api/v1/roles/1/details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleId").value(1))
                .andExpect(jsonPath("$.roleName").value("Administrador"));
    }

    @Test
    void testFindRoleByIdNotFound() throws Exception {
        // Given
        Integer roleId = 1;
        // When
        Mockito.when(roleService.findById(roleId)).thenThrow(new RuntimeException("Role not found"));
        // Then
        mockMvc.perform(get("/api/v1/roles/1/details"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteRole() throws Exception {
        // Given
        Integer roleId = 1;
        // When
        Mockito.doNothing().when(roleService).delete(roleId);
        // Then
        mockMvc.perform(delete("/api/v1/roles/1/delete"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteRoleNotFound() throws Exception {
        // Given
        Integer roleId = 1;
        // When
        Mockito.doThrow(new RuntimeException("Role not found")).when(roleService).delete(roleId);
        // Then
        mockMvc.perform(delete("/api/v1/roles/1/delete"))
                .andExpect(status().isNotFound());
    }

}
