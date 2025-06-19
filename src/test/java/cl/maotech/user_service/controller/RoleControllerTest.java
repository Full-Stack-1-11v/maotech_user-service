package cl.maotech.user_service.controller;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
}
