package cl.maotech.user_service.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.maotech.user_service.assemblers.UserDtoModelAssembler;
import cl.maotech.user_service.assemblers.UserModelAssembler;
import cl.maotech.user_service.dto.RoleDTO;
import cl.maotech.user_service.dto.StatusEditDTO;
import cl.maotech.user_service.dto.UserDTO;
import cl.maotech.user_service.dto.UserEditDTO;
import cl.maotech.user_service.model.User;
import cl.maotech.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v2/users")
@Tag(name = "Users", description = "Operationes relacionadas a los usuarios del sistema")
public class UserControllerV2 {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserModelAssembler assembler;

    @Autowired
    private UserDtoModelAssembler userDtoAssembler;

    @PostMapping("/create")
    @Operation(summary = "Crear un nuevo usuario", description = "Crea un nuevo usuario en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente", 
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "No se pudo crear el usuario")
    })
    public ResponseEntity<EntityModel<User>> create(@RequestBody User user) {
        logger.info("[crearUsuario] Inicio");
        logger.debug("[crearUsuario] Datos del usuario: {}", user);
        try {
            User newUser = userService.save(user);
            EntityModel<User> entityModel = assembler.toModel(newUser);
            logger.info("[crearUsuario] Usuario creado exitosamente: {}", newUser);
            logger.debug("[crearUsuario] Detalles del usuario creado: {}", entityModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
        } catch (Exception e) {
            logger.error("[crearUsuario] Error al crear el usuario: {}", e.getMessage(), e);
            logger.debug("[crearUsuario] Datos del error: {}", e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/list")
    @Operation(summary = "Listar usuarios", description = "Obtiene una lista de todos los usuarios del sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente", 
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "204", description = "No hay usuarios disponibles"),
        @ApiResponse(responseCode = "404", description = "No se encontraron usuarios")
    })
    public ResponseEntity<CollectionModel<EntityModel<UserDTO>>> listAsDto() {
        logger.info("[listarUsuarios] Inicio");
        logger.debug("[listarUsuarios] Obteniendo lista de usuarios");
        try {
            List<UserDTO> users = userService.getAllAsDto();
            List<EntityModel<UserDTO>> usersModel = users.stream()
            .map(userDtoAssembler::toModel)
            .collect(Collectors.toList());
            if (users.isEmpty()) {
                logger.info("[listarUsuarios] No hay usuarios disponibles");
                logger.debug("[listarUsuarios] Lista de usuarios vacía");
                return ResponseEntity.noContent().build();
            } else {
                logger.info("[listarUsuarios] Lista de usuarios obtenida exitosamente");
                logger.debug("[listarUsuarios] Detalles de los usuarios: {}", usersModel);
                return ResponseEntity.ok(CollectionModel.of(usersModel, Link.of("/api/v2/users/list").withSelfRel()));
            }
        } catch (Exception e) {
            logger.error("[listarUsuarios] Error al obtener la lista de usuarios: {}", e.getMessage(), e);
            logger.debug("[listarUsuarios] Datos del error: {}", e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/list/admin")
    @Operation(summary = "Listar usuarios (Admin)", description = "Obtiene una lista de todos los usuarios del sistema para administradores.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente", 
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "204", description = "No hay usuarios disponibles"),
        @ApiResponse(responseCode = "404", description = "No se encontraron usuarios")
    })
    public ResponseEntity<CollectionModel<EntityModel<User>>> list() {
        logger.info("[listarUsuariosAdmin] Inicio");
        logger.debug("[listarUsuariosAdmin] Obteniendo lista de usuarios");
        try {
            List<User> users = userService.findAll();
            List<EntityModel<User>> usersModel = users.stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());
            if (users.isEmpty()) {
                logger.info("[listarUsuariosAdmin] No hay usuarios disponibles");
                logger.debug("[listarUsuariosAdmin] Lista de usuarios vacía");
                return ResponseEntity.noContent().build();
            } else {
                logger.info("[listarUsuariosAdmin] Lista de usuarios obtenida exitosamente");
                logger.debug("[listarUsuariosAdmin] Detalles de los usuarios: {}", usersModel);
                return ResponseEntity.ok(CollectionModel.of(usersModel, Link.of("/api/v2/users/list/admin").withSelfRel()));
            }
        } catch (Exception e) {
            logger.error("[listarUsuariosAdmin] Error al obtener la lista de usuarios: {}", e.getMessage(), e);
            logger.debug("[listarUsuariosAdmin] Datos del error: {}", e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/details")
    @Operation(summary = "Obtener usuario por ID", description = "Obtiene los detalles de un usuario específico por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente", 
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<EntityModel<UserDTO>> findByUserDTO(@PathVariable Integer id) {
        logger.info("[obtenerUsuarioPorId] Inicio");
        logger.debug("[obtenerUsuarioPorId] Buscando usuario con ID: {}", id);
        try {
            User user = userService.findById(id);
            UserDTO userDTO = userService.toDto(user);
            EntityModel<UserDTO> entityModel = userDtoAssembler.toModel(userDTO);
            logger.info("[obtenerUsuarioPorId] Usuario encontrado: {}", userDTO);
            logger.debug("[obtenerUsuarioPorId] Detalles del usuario: {}", entityModel);
            return ResponseEntity.ok(entityModel);
        } catch (Exception e) {
            logger.error("[obtenerUsuarioPorId] Error al obtener el usuario: {}", e.getMessage(), e);
            logger.debug("[obtenerUsuarioPorId] Datos del error: {}", e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/details/admin")
    @Operation(summary = "Obtener usuario por ID (Admin)", description = "Obtiene los detalles de un usuario específico por su ID para administradores.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente", 
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<EntityModel<User>> findById(@PathVariable Integer id) {
        logger.info("[obtenerUsuarioPorIdAdmin] Inicio");
        logger.debug("[obtenerUsuarioPorIdAdmin] Buscando usuario con ID: {}", id);
        try {
            User user = userService.findById(id);
            EntityModel<User> entityModel = assembler.toModel(user);
            logger.info("[obtenerUsuarioPorIdAdmin] Usuario encontrado: {}", user);
            logger.debug("[obtenerUsuarioPorIdAdmin] Detalles del usuario: {}", entityModel);
            return ResponseEntity.ok(entityModel);
        } catch (Exception e) {
            logger.error("[obtenerUsuarioPorIdAdmin] Error al obtener el usuario: {}", e.getMessage(), e);
            logger.debug("[obtenerUsuarioPorIdAdmin] Datos del error: {}", e);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/full-update")
    @Operation(summary = "Actualizar usuario completamente", description = "Actualiza todos los campos de un usuario existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente", 
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<EntityModel<User>> fullUpdate(@PathVariable Integer id, @RequestBody User user) {
        logger.info("[actualizarUsuarioCompleto] Inicio");
        logger.debug("[actualizarUsuarioCompleto] Actualizando usuario con ID: {}", id);
        try {
            User up_user = userService.findById(id);
            up_user.setUserId(id);
            up_user.setRut(user.getRut());
            up_user.setFirstName(user.getFirstName());
            up_user.setLastName(user.getLastName());
            up_user.setEmail(user.getEmail());
            up_user.setStatus(user.getStatus());

            userService.save(up_user);
            EntityModel<User> entityModel = assembler.toModel(up_user);
            logger.info("[actualizarUsuarioCompleto] Usuario actualizado exitosamente: {}", up_user);
            logger.debug("[actualizarUsuarioCompleto] Detalles del usuario actualizado: {}", entityModel);
            return ResponseEntity.status(HttpStatus.OK).body(entityModel);

        } catch (Exception e) {
            logger.error("[actualizarUsuarioCompleto] Error al actualizar el usuario: {}", e.getMessage(), e);
            logger.debug("[actualizarUsuarioCompleto] Datos del error: {}", e);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/update")
    @Operation(summary = "Actualizar usuario", description = "Actualiza algunos campos de un usuario existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente", 
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<EntityModel<User>> update(@PathVariable Integer id, @RequestBody UserEditDTO editDTO) {
        logger.info("[actualizarUsuario] Inicio");
        logger.debug("[actualizarUsuario] Actualizando usuario con ID: {}", id);
        try {
            User user = userService.findById(id);
            User up_user = userService.updateFromDto(editDTO, user);

            userService.save(up_user);
            EntityModel<User> entityModel = assembler.toModel(up_user);
            logger.info("[actualizarUsuario] Usuario actualizado exitosamente: {}", up_user);
            logger.debug("[actualizarUsuario] Detalles del usuario actualizado: {}", entityModel);
            return ResponseEntity.ok(entityModel);

        } catch (Exception e) {
            logger.error("[actualizarUsuario] Error al actualizar el usuario: {}", e.getMessage(), e);
            logger.debug("[actualizarUsuario] Datos del error: {}", e);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Desactivar usuario", description = "Desactiva un usuario existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario desactivado exitosamente", 
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<EntityModel<User>> deactivateUser(@PathVariable Integer id, @RequestBody StatusEditDTO statusDTO) {
        logger.info("[desactivarUsuario] Inicio");
        logger.debug("[desactivarUsuario] Desactivando usuario con ID: {}", id);
        try {
            User user = userService.findById(id);
            User d_user = userService.updateStatusDto(statusDTO, user);

            userService.save(d_user);
            EntityModel<User> entityModel = assembler.toModel(d_user);
            logger.info("[desactivarUsuario] Usuario desactivado exitosamente: {}", d_user);
            logger.debug("[desactivarUsuario] Detalles del usuario desactivado: {}", entityModel);
            return ResponseEntity.ok(entityModel);

        } catch (Exception e) {
            logger.error("[desactivarUsuario] Error al desactivar el usuario: {}", e.getMessage(), e);
            logger.debug("[desactivarUsuario] Datos del error: {}", e);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/role/edit")
    @Operation(summary = "Actualizar rol de usuario", description = "Actualiza el rol de un usuario existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol de usuario actualizado exitosamente", 
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<EntityModel<User>> updateRole(@PathVariable Integer id, @RequestBody RoleDTO roleDTO) {
        logger.info("[actualizarRolUsuario] Inicio");
        logger.debug("[actualizarRolUsuario] Actualizando rol de usuario con ID: {}", id);
        try {
            User user = userService.findById(id);
            User updatedUser = userService.updateRoleDto(roleDTO.getRoleId(), user);
            
            userService.save(updatedUser);
            EntityModel<User> entityModel = assembler.toModel(updatedUser);
            logger.info("[actualizarRolUsuario] Rol de usuario actualizado exitosamente: {}", updatedUser);
            logger.debug("[actualizarRolUsuario] Detalles del usuario actualizado: {}", entityModel);
            return ResponseEntity.ok(entityModel);
            
        } catch (Exception e) {
            logger.error("[actualizarRolUsuario] Error al actualizar el rol del usuario: {}", e.getMessage(), e);
            logger.debug("[actualizarRolUsuario] Datos del error: {}", e);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/delete")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario existente del sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        logger.info("[eliminarUsuario] Inicio");
        logger.debug("[eliminarUsuario] Eliminando usuario con ID: {}", id);
        try {
            userService.delete(id);
            logger.info("[eliminarUsuario] Usuario eliminado exitosamente");
            logger.debug("[eliminarUsuario] Usuario con ID {} eliminado", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("[eliminarUsuario] Error al eliminar el usuario: {}", e.getMessage(), e);
            logger.debug("[eliminarUsuario] Datos del error: {}", e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/inactives/admin")
    @Operation(summary = "Listar usuarios inactivos (Admin)", description = "Obtiene una lista de todos los usuarios inactivos del sistema para administradores.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios inactivos obtenida exitosamente", 
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "204", description = "No hay usuarios inactivos disponibles"),
        @ApiResponse(responseCode = "404", description = "No se encontraron usuarios inactivos")
    })
    public ResponseEntity<CollectionModel<EntityModel<User>>> findInactives() {
        logger.info("[listarUsuariosInactivosAdmin] Inicio");
        logger.debug("[listarUsuariosInactivosAdmin] Obteniendo lista de usuarios inactivos");
        try {
            List<User> inactives = userService.findByStatusFalse();
            List<EntityModel<User>> inactivesModel = inactives.stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());
            if (inactives.isEmpty()) {
                logger.info("[listarUsuariosInactivosAdmin] No hay usuarios inactivos disponibles");
                logger.debug("[listarUsuariosInactivosAdmin] Lista de usuarios inactivos vacía");
                return ResponseEntity.noContent().build();
            } else {
                logger.info("[listarUsuariosInactivosAdmin] Lista de usuarios inactivos obtenida exitosamente");
                logger.debug("[listarUsuariosInactivosAdmin] Detalles de los usuarios inactivos: {}", inactivesModel);
                return ResponseEntity.ok(CollectionModel.of(inactivesModel));
            }
        } catch (Exception e) {
            logger.error("[listarUsuariosInactivosAdmin] Error al obtener la lista de usuarios inactivos: {}", e.getMessage(), e);
            logger.debug("[listarUsuariosInactivosAdmin] Datos del error: {}", e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/inactives")
    @Operation(summary = "Listar usuarios inactivos", description = "Obtiene una lista de todos los usuarios inactivos del sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios inactivos obtenida exitosamente", 
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "204", description = "No hay usuarios inactivos disponibles"),
        @ApiResponse(responseCode = "404", description = "No se encontraron usuarios inactivos")
    })
    public ResponseEntity<CollectionModel<EntityModel<UserDTO>>> findInactivesDto() {
        logger.info("[listarUsuariosInactivos] Inicio");
        logger.debug("[listarUsuariosInactivos] Obteniendo lista de usuarios inactivos");
        try {
            List<UserDTO> inactives = userService.findInactivesDto();
            List<EntityModel<UserDTO>> inactivesModel = inactives.stream()
            .map(userDtoAssembler::toModel)
            .collect(Collectors.toList());
            if (inactives.isEmpty()) {
                logger.info("[listarUsuariosInactivos] No hay usuarios inactivos disponibles");
                logger.debug("[listarUsuariosInactivos] Lista de usuarios inactivos vacía");
                return ResponseEntity.noContent().build();
            } else {
                logger.info("[listarUsuariosInactivos] Lista de usuarios inactivos obtenida exitosamente");
                logger.debug("[listarUsuariosInactivos] Detalles de los usuarios inactivos: {}", inactivesModel);
                return ResponseEntity.ok(CollectionModel.of(inactivesModel));
            }
        } catch (Exception e) {
            logger.error("[listarUsuariosInactivos] Error al obtener la lista de usuarios inactivos: {}", e.getMessage(), e);
            logger.debug("[listarUsuariosInactivos] Datos del error: {}", e);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/inactives/delete")
    @Operation(summary = "Eliminar usuarios inactivos", description = "Elimina todos los usuarios inactivos del sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuarios inactivos eliminados exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron usuarios inactivos para eliminar")
    })
    public ResponseEntity<?> deleteAllInactives() {
        logger.info("[eliminarUsuariosInactivos] Inicio");
        logger.debug("[eliminarUsuariosInactivos] Eliminando usuarios inactivos");
        try {
            userService.deleteStatusFalse();
            logger.info("[eliminarUsuariosInactivos] Usuarios inactivos eliminados exitosamente");
            logger.debug("[eliminarUsuariosInactivos] Todos los usuarios inactivos han sido eliminados");
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("[eliminarUsuariosInactivos] Error al eliminar los usuarios inactivos: {}", e.getMessage(), e);
            logger.debug("[eliminarUsuariosInactivos] Datos del error: {}", e);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Permite a un usuario iniciar sesión en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso"),
        @ApiResponse(responseCode = "401", description = "Credenciales incorrectas"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<String> login(@RequestBody User user) {
        logger.info("[iniciarSesion] Inicio");
        logger.debug("[iniciarSesion] Intentando iniciar sesión con email: {}", user.getEmail());
        try {
            boolean login = userService.login(user.getEmail(), user.getPassword());
            if (login) {
                logger.info("[iniciarSesion] Inicio de sesión exitoso para el usuario: {}", user.getEmail());
                logger.debug("[iniciarSesion] Usuario autenticado: {}", user);
                return ResponseEntity.ok("Logged-In");
            } else {
                logger.warn("[iniciarSesion] Credenciales incorrectas para el usuario: {}", user.getEmail());
                logger.debug("[iniciarSesion] Intento de inicio de sesión fallido para el usuario: {}", user.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong Credentials");
            }
        } catch (Exception e) {
            logger.error("[iniciarSesion] Error al iniciar sesión: {}", e.getMessage(), e);
            logger.debug("[iniciarSesion] Datos del error: {}", e);
            return ResponseEntity.notFound().build();
        }
    }
}
