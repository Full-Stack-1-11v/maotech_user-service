package cl.maotech.user_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "Operationes relacionadas a los usuarios del sistema")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    @Operation(summary = "Crear un nuevo usuario", description = "Crea un nuevo usuario en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente", 
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "No se pudo crear el usuario")
    })
    public ResponseEntity<User> create(@RequestBody User user) {
        try {
            User newUser = userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/list")
    @Operation(summary = "Listar usuarios", description = "Obtiene una lista de todos los usuarios del sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente", 
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "204", description = "No hay usuarios disponibles"),
        @ApiResponse(responseCode = "404", description = "No se encontraron usuarios")
    })
    public ResponseEntity<List<UserDTO>> listAsDto() {
        try {
            List<UserDTO> users = userService.getAllAsDto();

            if (users.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(users);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
    public ResponseEntity<List<User>> list() {
        try {
            List<User> users = userService.findAll();
            if (users.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(users);
            }
        } catch (Exception e) {
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
    public ResponseEntity<UserDTO> findByUserDTO(@PathVariable Integer id) {
        try {
            User user = userService.findById(id);
            UserDTO userDTO = userService.toDto(user);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
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
    public ResponseEntity<User> findById(@PathVariable Integer id) {
        try {
            User user = userService.findById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
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
    public ResponseEntity<User> fullUpdate(@PathVariable Integer id, @RequestBody User user) {
        try {
            User up_user = userService.findById(id);
            up_user.setUserId(id);
            up_user.setRut(user.getRut());
            up_user.setFirstName(user.getFirstName());
            up_user.setLastName(user.getLastName());
            up_user.setEmail(user.getEmail());
            up_user.setStatus(user.getStatus());

            userService.save(up_user);
            return ResponseEntity.ok(up_user);

        } catch (Exception e) {
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
    public ResponseEntity<User> update(@PathVariable Integer id, @RequestBody UserEditDTO editDTO) {
        try {
            User user = userService.findById(id);
            User up_user = userService.updateFromDto(editDTO, user);

            userService.save(up_user);
            return ResponseEntity.ok(up_user);

        } catch (Exception e) {
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
    public ResponseEntity<User> deactivateUser(@PathVariable Integer id, @RequestBody StatusEditDTO statusDTO) {
        try {
            User user = userService.findById(id);
            User d_user = userService.updateStatusDto(statusDTO, user);

            userService.save(d_user);
            return ResponseEntity.ok(d_user);

        } catch (Exception e) {
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
    public ResponseEntity<User> updateRole(@PathVariable Integer id, @RequestBody RoleDTO roleDTO) {
        try {
            User user = userService.findById(id);
            User updatedUser = userService.updateRoleDto(roleDTO.getRoleId(), user);
            
            userService.save(updatedUser);
            return ResponseEntity.ok(updatedUser);
            
        } catch (Exception e) {
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
        try {
            userService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
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
    public ResponseEntity<List<User>> findInactives() {
        try {
            List<User> inactives = userService.findByStatusFalse();
            if (inactives.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(inactives);
            }
        } catch (Exception e) {
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
    public ResponseEntity<List<UserDTO>> findInactivesDto() {
        try {
            List<UserDTO> inactives = userService.findInactivesDto();
            if (inactives.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(inactives);
            }
        } catch (Exception e) {
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
        try {
            userService.deleteStatusFalse();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
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
        try {
            boolean login = userService.login(user.getEmail(), user.getPassword());
            if (login) {
                return ResponseEntity.ok("Logged-In");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong Credentials");
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
