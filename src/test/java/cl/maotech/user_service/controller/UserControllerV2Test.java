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

import cl.maotech.user_service.assemblers.UserDtoModelAssembler;
import cl.maotech.user_service.assemblers.UserModelAssembler;
import cl.maotech.user_service.dto.StatusEditDTO;
import cl.maotech.user_service.dto.UserDTO;
import cl.maotech.user_service.dto.UserEditDTO;
import cl.maotech.user_service.model.Role;
import cl.maotech.user_service.model.User;
import cl.maotech.user_service.service.UserService;

@WebMvcTest(UserControllerV2.class)
public class UserControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserModelAssembler assembler;

    @MockitoBean
    private UserDtoModelAssembler userDtoAssembler;
    
    @Test
    void testCreateUser() throws Exception {
        // Given
        User user = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
        EntityModel<User> entityModel = EntityModel.of(user, Link.of("/api/v2/users/1/details").withSelfRel());
        // When
        Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(assembler.toModel(Mockito.any(User.class))).thenReturn(entityModel);
        // Then
        mockMvc.perform(post("/api/v2/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\": 1, \"email\": \"test@mail.com\", \"password\": \"password123\", \"rut\": \"11.111.111-1\", \"firstName\": \"Admin1\", \"lastName\": \"Admin1\", \"active\": true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.email").value("test@mail.com"))
                .andExpect(jsonPath("$.firstName").value("Admin1"))
                .andExpect(jsonPath("$.lastName").value("Admin1"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testCreateUserBadRequest() throws Exception {
        // Given
        User user = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
        EntityModel<User> entityModel = EntityModel.of(user, Link.of("/api/v2/users/1/details").withSelfRel());
        // When
        Mockito.when(userService.save(Mockito.any(User.class))).thenThrow(new RuntimeException("Bad Request"));
        Mockito.when(assembler.toModel(Mockito.any(User.class))).thenReturn(entityModel);
        // Then
        mockMvc.perform(post("/api/v2/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\": 1, \"email\": \"\", \"password\": \"password123\", \"rut\": \"11.111.111-1\", \"firstName\": \"Admin1\", \"lastName\": \"Admin1\", \"active\": true}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testListUsers() throws Exception {
        // Given
        UserDTO userDTO = new UserDTO(1, "test@mail.com", "11.111.111-1", "Admin1", "Admin1", true, null);
        EntityModel<UserDTO> entityModel = EntityModel.of(userDTO, Link.of("/api/v2/users/1/details").withSelfRel());
        List<EntityModel<UserDTO>> userModels = List.of(entityModel);
        // When
        Mockito.when(userService.getAllAsDto()).thenReturn(List.of(userDTO));
        Mockito.when(userDtoAssembler.toModel(userDTO)).thenReturn(entityModel);
        Mockito.when(userDtoAssembler.toCollectionModel(Mockito.anyList())).thenReturn(CollectionModel.of(userModels, Link.of("/api/v2/users/list").withSelfRel()));
        // Then
        mockMvc.perform(get("/api/v2/users/list")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.userDTOList[0].userId").value(1))
                .andExpect(jsonPath("$._embedded.userDTOList[0].email").value("test@mail.com"))
                .andExpect(jsonPath("$._embedded.userDTOList[0].firstName").value("Admin1"))
                .andExpect(jsonPath("$._embedded.userDTOList[0].lastName").value("Admin1"))
                .andExpect(jsonPath("$._embedded.userDTOList[0]._links.self.href").exists());
    }

    @Test
    void testListUsersNoContent() throws Exception {
        // Given
        // When
        Mockito.when(userService.getAllAsDto()).thenReturn(Collections.emptyList());
        // Then
        mockMvc.perform(get("/api/v2/users/list"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testListUsersNotFound() throws Exception {
        // Given
        // When
        Mockito.when(userService.getAllAsDto()).thenThrow(new RuntimeException("Not Found"));
        // Then
        mockMvc.perform(get("/api/v2/users/list"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testListUsersAdmin() throws Exception {
        // Given
        User user = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
        EntityModel<User> entityModel = EntityModel.of(user, Link.of("/api/v2/users/1/details/admin").withSelfRel());
        List<EntityModel<User>> userModels = List.of(entityModel);
        // When
        Mockito.when(userService.findAll()).thenReturn(List.of(user));
        Mockito.when(assembler.toModel(user)).thenReturn(entityModel);
        Mockito.when(assembler.toCollectionModel(Mockito.anyList())).thenReturn(CollectionModel.of(userModels, Link.of("/api/v2/users/list/admin").withSelfRel()));
        // Then
        mockMvc.perform(get("/api/v2/users/list/admin")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.userList[0].userId").value(1))
                .andExpect(jsonPath("$._embedded.userList[0].email").value("test@mail.com"))
                .andExpect(jsonPath("$._embedded.userList[0].password").value("password123"))
                .andExpect(jsonPath("$._embedded.userList[0].rut").value("11.111.111-1"))
                .andExpect(jsonPath("$._embedded.userList[0].firstName").value("Admin1"))
                .andExpect(jsonPath("$._embedded.userList[0].lastName").value("Admin1"))
                .andExpect(jsonPath("$._embedded.userList[0]._links.self.href").exists());
    }

    @Test
    void testListUsersAdminNoContent() throws Exception {
        // Given
        // When
        Mockito.when(userService.findAll()).thenReturn(Collections.emptyList());
        // Then
        mockMvc.perform(get("/api/v2/users/list/admin"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testListUsersAdminNotFound() throws Exception {
        // Given
        // When
        Mockito.when(userService.findAll()).thenThrow(new RuntimeException("Not Found"));
        // Then
        mockMvc.perform(get("/api/v2/users/list/admin"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindById() throws Exception {
        // Given
        User user = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
        UserDTO userDTO = new UserDTO(1, "test@mail.com", "11.111.111-1", "Admin1", "Admin1", true, null);
        EntityModel<UserDTO> entityModel = EntityModel.of(userDTO, Link.of("/api/v2/users/1/details").withSelfRel());
        // When
        Mockito.when(userService.findById(1)).thenReturn(user);
        Mockito.when(userService.toDto(user)).thenReturn(userDTO);
        Mockito.when(userDtoAssembler.toModel(userDTO)).thenReturn(entityModel);
        // Then
        mockMvc.perform(get("/api/v2/users/1/details")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.email").value("test@mail.com"))
                .andExpect(jsonPath("$.rut").value("11.111.111-1"))
                .andExpect(jsonPath("$.firstName").value("Admin1"))
                .andExpect(jsonPath("$.lastName").value("Admin1"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testFindByIdNotFound() throws Exception {
        // Given
        // When
        Mockito.when(userService.findById(1)).thenThrow(new RuntimeException("Not Found"));
        // Then
        mockMvc.perform(get("/api/v2/users/1/details"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindByIdAdmin() throws Exception {
        // Given
        User user = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
        EntityModel<User> entityModel = EntityModel.of(user, Link.of("/api/v2/users/1/details/admin").withSelfRel());
        // When
        Mockito.when(userService.findById(1)).thenReturn(user);
        Mockito.when(assembler.toModel(user)).thenReturn(entityModel);
        // Then
        mockMvc.perform(get("/api/v2/users/1/details/admin")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.email").value("test@mail.com"))
                .andExpect(jsonPath("$.password").value("password123"))
                .andExpect(jsonPath("$.rut").value("11.111.111-1"))
                .andExpect(jsonPath("$.firstName").value("Admin1"))
                .andExpect(jsonPath("$.lastName").value("Admin1"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testFindByIdAdminNotFound() throws Exception {
        // Given
        // When
        Mockito.when(userService.findById(1)).thenThrow(new RuntimeException("Not Found"));
        // Then
        mockMvc.perform(get("/api/v2/users/1/details/admin"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFullUpdate() throws Exception {
        // Given
        User user = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
        EntityModel<User> entityModel = EntityModel.of(user, Link.of("/api/v2/users/1/details/admin").withSelfRel());
        // When
        Mockito.when(userService.findById(1)).thenReturn(user);
        Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(assembler.toModel(Mockito.any(User.class))).thenReturn(entityModel);
        // Then
        mockMvc.perform(put("/api/v2/users/1/full-update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\": 1, \"email\": \"test@mail.com\", \"password\": \"password123\", \"rut\": \"11.111.111-1\", \"firstName\": \"Admin1\", \"lastName\": \"Admin1\", \"active\": true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.email").value("test@mail.com"))
                .andExpect(jsonPath("$.firstName").value("Admin1"))
                .andExpect(jsonPath("$.lastName").value("Admin1"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testFullUpdateNotFound() throws Exception {
        // Given
        Mockito.when(userService.save(Mockito.any(User.class))).thenThrow(new RuntimeException("Not Found"));
        // Then
        mockMvc.perform(put("/api/v2/users/1/full-update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\": 1, \"email\": \"test@mail.com\", \"password\": \"password123\", \"rut\": \"11.111.111-1\", \"firstName\": \"Admin1\", \"lastName\": \"Admin1\", \"active\": true}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateUser() throws Exception {
        // Given
        User user = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
        EntityModel<User> entityModel = EntityModel.of(user, Link.of("/api/v2/users/1/details").withSelfRel());
        UserEditDTO userEditDTO = new UserEditDTO(1, "test@mail.com", "Admin1", "Admin1");
        // When
        Mockito.when(userService.findById(1)).thenReturn(user);
        Mockito.when(userService.toEditDto(user)).thenReturn(userEditDTO);
        Mockito.when(userService.updateFromDto(Mockito.any(UserEditDTO.class), Mockito.any(User.class))).thenReturn(user);
        Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(assembler.toModel(Mockito.any(User.class))).thenReturn(entityModel);
        // Then
        mockMvc.perform(put("/api/v2/users/1/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\": 1, \"email\": \"test@mail.com\", \"firstName\": \"Admin1\", \"lastName\": \"Admin1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.email").value("test@mail.com"))
                .andExpect(jsonPath("$.firstName").value("Admin1"))
                .andExpect(jsonPath("$.lastName").value("Admin1"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testUpdateUserNotFound() throws Exception {
        // Given
        // When
        Mockito.when(userService.findById(1)).thenThrow(new RuntimeException("Not Found"));
        // Then
        mockMvc.perform(put("/api/v2/users/1/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\": 1, \"email\": \"test@mail.com\", \"firstName\": \"Admin1\", \"lastName\": \"Admin1\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeactivate() throws Exception {
        // Given
        User user = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
        EntityModel<User> entityModel = EntityModel.of(user, Link.of("/api/v2/users/1/details").withSelfRel());
        // When
        Mockito.when(userService.findById(1)).thenReturn(user);
        Mockito.when(userService.updateStatusDto(Mockito.any(StatusEditDTO.class), Mockito.any(User.class))).thenReturn(user);
        Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(assembler.toModel(Mockito.any(User.class))).thenReturn(entityModel);
        // Then
        mockMvc.perform(put("/api/v2/users/1/deactivate")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"status\": false}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId").value(1))
            .andExpect(jsonPath("$.email").value("test@mail.com"))
            .andExpect(jsonPath("$.password").value("password123"))
            .andExpect(jsonPath("$.rut").value("11.111.111-1"))
            .andExpect(jsonPath("$.firstName").value("Admin1"))
            .andExpect(jsonPath("$.lastName").value("Admin1"))
            .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testDeactivateNotFound() throws Exception {
        // Given
        // When
        Mockito.when(userService.findById(1)).thenThrow(new RuntimeException("Not Found"));
        // Then
        mockMvc.perform(put("/api/v2/users/1/deactivate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\": false}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateRole() throws Exception {
        // Given
        Role role = new Role(1, "Administrador");
        User user = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, role);
        EntityModel<User> entityModel = EntityModel.of(user, Link.of("/api/v2/users/1/details").withSelfRel());
        // When
        Mockito.when(userService.findById(1)).thenReturn(user);
        Mockito.when(userService.updateRoleDto(Mockito.eq(role.getRoleId()), Mockito.any(User.class))).thenReturn(user);
        Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(assembler.toModel(Mockito.any(User.class))).thenReturn(entityModel);
        // Then
        mockMvc.perform(put("/api/v2/users/1/role/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"roleId\": 1, \"roleName\": \"Administrador\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.email").value("test@mail.com"))
                .andExpect(jsonPath("$.password").value("password123"))
                .andExpect(jsonPath("$.rut").value("11.111.111-1"))
                .andExpect(jsonPath("$.firstName").value("Admin1"))
                .andExpect(jsonPath("$.lastName").value("Admin1"))
                .andExpect(jsonPath("$.role.roleId").value(1))
                .andExpect(jsonPath("$.role.roleName").value("Administrador"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void testUpdateRoleNotFound() throws Exception {
        // Given
        // When
        Mockito.when(userService.findById(1)).thenThrow(new RuntimeException("Not Found"));
        // Then
        mockMvc.perform(put("/api/v2/users/1/role/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"roleId\": 1, \"roleName\": \"Administrador\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUser() throws Exception {
        // Given
        // When
        Mockito.doNothing().when(userService).delete(1);
        // Then
        mockMvc.perform(delete("/api/v2/users/1/delete"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteUserNotFound() throws Exception {
        // Given
        // When
        Mockito.doThrow(new RuntimeException("Not Found")).when(userService).delete(1);
        // Then
        mockMvc.perform(delete("/api/v2/users/1/delete"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testListInactivesAdmin() throws Exception {
        // Given
        User user = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", false, null);
        EntityModel<User> entityModel = EntityModel.of(user, Link.of("/api/v2/users/1/details/admin").withSelfRel());
        List<EntityModel<User>> userModels = List.of(entityModel);
        // When
        Mockito.when(userService.findByStatusFalse()).thenReturn(List.of(user));
        Mockito.when(assembler.toModel(user)).thenReturn(entityModel);
        Mockito.when(assembler.toCollectionModel(Mockito.anyList())).thenReturn(CollectionModel.of(userModels, Link.of("/api/v2/users/inactives/list").withSelfRel()));
        // Then
        mockMvc.perform(get("/api/v2/users/inactives/admin")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.userList[0].userId").value(1))
                .andExpect(jsonPath("$._embedded.userList[0].email").value("test@mail.com"))
                .andExpect(jsonPath("$._embedded.userList[0].password").value("password123"))
                .andExpect(jsonPath("$._embedded.userList[0].rut").value("11.111.111-1"))
                .andExpect(jsonPath("$._embedded.userList[0].firstName").value("Admin1"))
                .andExpect(jsonPath("$._embedded.userList[0].lastName").value("Admin1"))
                .andExpect(jsonPath("$._embedded.userList[0]._links.self.href").exists());
    }

    @Test
    void testListInactivesAdminNoContent() throws Exception {
        // Given
        // When
        Mockito.when(userService.findByStatusFalse()).thenReturn(Collections.emptyList());
        // Then
        mockMvc.perform(get("/api/v2/users/inactives/admin"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testListInactivesAdminNotFound() throws Exception {
        // Given
        // When
        Mockito.when(userService.findByStatusFalse()).thenThrow(new RuntimeException("Not Found"));
        // Then
        mockMvc.perform(get("/api/v2/users/inactives/admin"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testListInactives() throws Exception {
        // Given
        UserDTO userDTO = new UserDTO(1, "test@mail.com", "11.111.111-1", "Admin1", "Admin1", false, null);
        EntityModel<UserDTO> entityModel = EntityModel.of(userDTO, Link.of("/api/v2/users/1/details").withSelfRel());
        List<EntityModel<UserDTO>> userModels = List.of(entityModel);
        // When
        Mockito.when(userService.findInactivesDto()).thenReturn(List.of(userDTO));
        Mockito.when(userDtoAssembler.toModel(userDTO)).thenReturn(entityModel);
        Mockito.when(userDtoAssembler.toCollectionModel(Mockito.anyList())).thenReturn(CollectionModel.of(userModels, Link.of("/api/v2/users/inactives").withSelfRel()));
        // Then
        mockMvc.perform(get("/api/v2/users/inactives")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.userDTOList[0].userId").value(1))
                .andExpect(jsonPath("$._embedded.userDTOList[0].email").value("test@mail.com"))
                .andExpect(jsonPath("$._embedded.userDTOList[0].firstName").value("Admin1"))
                .andExpect(jsonPath("$._embedded.userDTOList[0].lastName").value("Admin1"))
                .andExpect(jsonPath("$._embedded.userDTOList[0]._links.self.href").exists());
    }

    @Test
    void testListInactivesNoContent() throws Exception {
        // Given
        // When
        Mockito.when(userService.findInactivesDto()).thenReturn(Collections.emptyList());
        // Then
        mockMvc.perform(get("/api/v2/users/inactives"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testListInactivesNotFound() throws Exception {
        // Given
        // When
        Mockito.when(userService.findInactivesDto()).thenThrow(new RuntimeException("Not Found"));
        // Then
        mockMvc.perform(get("/api/v2/users/inactives"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteInactives() throws Exception {
        // Given
        // When
        Mockito.doNothing().when(userService).deleteStatusFalse();
        // Then
        mockMvc.perform(delete("/api/v2/users/inactives/delete"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteInactivesNotFound() throws Exception {
        // Given
        // When
        Mockito.doThrow(new RuntimeException("Not Found")).when(userService).deleteStatusFalse();
        // Then
        mockMvc.perform(delete("/api/v2/users/inactives/delete"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testLogin() throws Exception {
        // Given
        String email = "test@mail.com";
        String password = "password123";
        Mockito.when(userService.login(email, password)).thenReturn(true);
        // Then
        mockMvc.perform(post("/api/v2/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"test@mail.com\", \"password\": \"password123\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testLoginBadRequest() throws Exception {
        // Given
        String email = "test@mail.com";
        String password = "wrongpassword";
        Mockito.when(userService.login(email, password)).thenReturn(false);
        // Then
        mockMvc.perform(post("/api/v2/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"test@mail.com\", \"password\": \"wrongpassword\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLoginNotFound() throws Exception {
        // Given
        String email = "test@mail.com";
        String password = "password123";
        Mockito.when(userService.login(email, password)).thenThrow(new RuntimeException("Not Found"));
        // Then
        mockMvc.perform(post("/api/v2/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"test@mail.com\", \"password\": \"password123\"}"))
                .andExpect(status().isNotFound());
    }
}
