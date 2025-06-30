package cl.maotech.user_service.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import cl.maotech.user_service.assemblers.RoleModelAssembler;
import cl.maotech.user_service.model.Role;
import cl.maotech.user_service.service.RoleService;

@WebMvcTest(RoleControllerV2.class)
public class RoleControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoleService roleService;

    @MockitoBean
    private RoleModelAssembler assembler;

    @Test
    void testCreateRole() throws Exception {
        // Given
        Role role = new Role();
        role.setRoleId(1);
        role.setRoleName("Administrador");
        EntityModel<Role> entityModel = EntityModel.of(role, Link.of("/api/v2/roles/1/details").withSelfRel());
        // When
        Mockito.when(roleService.save(Mockito.any(Role.class))).thenReturn(role);
        Mockito.when(assembler.toModel(Mockito.any(Role.class))).thenReturn(entityModel);
        // Then
        mockMvc.perform(post("/api/v2/roles/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"roleId\": 1, \"roleName\": \"Administrador\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roleId").value(1))
                .andExpect(jsonPath("$.roleName").value("Administrador"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testCreateRoleBadRequest() throws Exception {
        // Given
        Role role = new Role();
        role.setRoleId(1);
        role.setRoleName("Administrador");
        EntityModel<Role> entityModel = EntityModel.of(role, Link.of("/api/v2/roles/1/details").withSelfRel());
        // When
        Mockito.when(roleService.save(Mockito.any(Role.class))).thenThrow(new RuntimeException("Bad Request"));
        Mockito.when(assembler.toModel(Mockito.any(Role.class))).thenReturn(entityModel);
        // Then
        mockMvc.perform(post("/api/v2/roles/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"roleId\": 1, \"roleName\": \"Administrador\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testListRoles() throws Exception {
        // Given
        Role role = new Role();
        role.setRoleId(1);
        role.setRoleName("Administrador");
        EntityModel<Role> entityModel = EntityModel.of(role, Link.of("/api/v2/roles/1/details").withSelfRel());
        List<EntityModel<Role>> rolesModel = List.of(entityModel);
        // When
        Mockito.when(roleService.findAll()).thenReturn(List.of(role));
        Mockito.when(assembler.toModel(role)).thenReturn(entityModel);
        Mockito.when(assembler.toCollectionModel(Mockito.anyList())).thenReturn(CollectionModel.of(rolesModel, Link.of("/api/v2/roles/list").withSelfRel()));
        // Then
        mockMvc.perform(get("/api/v2/roles/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.roleList[0].roleId").value(1))
                .andExpect(jsonPath("$._embedded.roleList[0].roleName").value("Administrador"))
                .andExpect(jsonPath("$._embedded.roleList[0]._links.self.href").exists())
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testListRolesNoContent() throws Exception {
        // Given
        // When
        Mockito.when(roleService.findAll()).thenReturn(Collections.emptyList());
        // Then
        mockMvc.perform(get("/api/v2/roles/list"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testListRolesNotFound() throws Exception {
        // Given
        // When
        Mockito.when(roleService.findAll()).thenThrow(new RuntimeException("Not Found"));
        // Then
        mockMvc.perform(get("/api/v2/roles/list"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindById() throws Exception {
        // Given
        Role role = new Role(1, "Administrador");
        EntityModel<Role> entityModel = EntityModel.of(role, Link.of("/api/v2/roles/1/details").withSelfRel());
        // When
        Mockito.when(roleService.findById(1)).thenReturn(role);
        Mockito.when(assembler.toModel(role)).thenReturn(entityModel);
        // Then
        mockMvc.perform(get("/api/v2/roles/1/details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleId").value(1))
                .andExpect(jsonPath("$.roleName").value("Administrador"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testFindByIdNotFound() throws Exception {
        // Given
        Mockito.when(roleService.findById(1)).thenThrow(new RuntimeException("Not Found"));
        // Then
        mockMvc.perform(get("/api/v2/roles/1/details"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteRole() throws Exception {
        // Given
        Mockito.doNothing().when(roleService).delete(1);
        // Then
        mockMvc.perform(delete("/api/v2/roles/1/delete"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteRoleNotFound() throws Exception {
        // Given
        // When
        Mockito.doThrow(new RuntimeException("Not Found")).when(roleService).delete(1);
        // Then
        mockMvc.perform(delete("/api/v2/roles/1/delete"))
                .andExpect(status().isNotFound());
    }
}
