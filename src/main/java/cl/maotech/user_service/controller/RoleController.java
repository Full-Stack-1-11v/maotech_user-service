package cl.maotech.user_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.maotech.user_service.model.Role;
import cl.maotech.user_service.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/v1/roles")
@Tag(name = "Roles", description = "Operationes relacionadas a los roles de usuario")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/create")
    @Operation(summary = "Crear un nuevo rol", description = "Crea un nuevo rol en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Rol creado exitosamente", 
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Role.class))),
        @ApiResponse(responseCode = "404", description = "No se pudo crear el rol")
    })
    public ResponseEntity<Role> create(@RequestBody Role role) {
        try {
            Role newRole = roleService.save(role);
            return ResponseEntity.status(HttpStatus.CREATED).body(newRole);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/list")
    @Operation(summary = "Listar roles", description = "Obtiene una lista de todos los roles disponibles.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de roles obtenida exitosamente", 
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Role.class))),
        @ApiResponse(responseCode = "204", description = "No hay roles disponibles"),
        @ApiResponse(responseCode = "404", description = "No se pudo obtener la lista de roles")
    })
    public ResponseEntity<List<Role>> list() {
        try {
            List<Role> roles = roleService.findAll();
            if (roles.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(roles);
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/details")
    @Operation(summary = "Obtener detalles de un rol", description = "Obtiene los detalles de un rol específico por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Detalles del rol obtenidos exitosamente", 
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Role.class))),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    public ResponseEntity<Role> findById(@PathVariable Integer id) {
        try {
            Role roles = roleService.findById(id);
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}/delete")
    @Operation(summary = "Eliminar un rol", description = "Elimina un rol específico por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Rol eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            roleService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
