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

import cl.maotech.user_service.assemblers.RoleModelAssembler;
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
@RequestMapping("/api/v2/roles")
@Tag(name = "Roles", description = "Operationes relacionadas a los roles de usuario")
public class RoleControllerV2 {

    private static final Logger logger = LoggerFactory.getLogger(RoleControllerV2.class);

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleModelAssembler assembler;

    @PostMapping("/create")
    @Operation(summary = "Crear un nuevo rol", description = "Crea un nuevo rol en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Rol creado exitosamente", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Role.class))),
        @ApiResponse(responseCode = "404", description = "No se pudo crear el rol")
    })
    public ResponseEntity<EntityModel<Role>> create(@RequestBody Role role) {
        logger.info("[crearRol] Inicio");
        logger.debug("[crearRol] Datos del rol: {}", role);
        try {
            Role newRole = roleService.save(role);
            EntityModel<Role> entityModel = assembler.toModel(newRole);
            logger.info("[crearRol] Rol creado exitosamente: {}", newRole);
            logger.debug("[crearRol] Rol creado: {}", entityModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
        } catch (Exception e) {
            logger.error("[crearRol] Error al crear el rol: {}", e.getMessage(), e);
            logger.debug("[crearRol] Datos del error: {}", e);
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
    public ResponseEntity<CollectionModel<EntityModel<Role>>> list() {
        logger.info("[listarRoles] Inicio");
        logger.debug("[listarRoles] Obteniendo lista de roles");
        try {
            List<Role> roles = roleService.findAll();
            List<EntityModel<Role>> rolesModel = roles.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
            if (roles.isEmpty()) {
                logger.info("[listarRoles] No hay roles disponibles");
                logger.debug("[listarRoles] Lista de roles vacía");
                return ResponseEntity.noContent().build();
            } else {
                logger.info("[listarRoles] Lista de roles obtenida exitosamente");
                logger.debug("[listarRoles] Roles: {}", rolesModel);
                return ResponseEntity.ok(CollectionModel.of(rolesModel, Link.of("/api/v2/roles/list").withSelfRel()));
            }
        } catch (Exception e) {
            logger.error("[listarRoles] Error al obtener la lista de roles: {}", e.getMessage(), e);
            logger.debug("[listarRoles] Datos del error: {}", e);
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
    public ResponseEntity<EntityModel<Role>> findById(@PathVariable Integer id) {
        logger.info("[obtenerDetallesRol] Inicio");
        logger.debug("[obtenerDetallesRol] Buscando rol con ID: {}", id);
        try {
            Role role = roleService.findById(id);
            EntityModel<Role> rolesModel = assembler.toModel(role);
            logger.info("[obtenerDetallesRol] Rol encontrado: {}", role);
            logger.debug("[obtenerDetallesRol] Detalles del rol: {}", rolesModel);
            return ResponseEntity.ok(rolesModel);
        } catch (Exception e) {
            logger.error("[obtenerDetallesRol] Error al obtener el rol: {}", e.getMessage(), e);
            logger.debug("[obtenerDetallesRol] Datos del error: {}", e);
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
            logger.info("[eliminarRol] Inicio");
            logger.debug("[eliminarRol] Eliminando rol con ID: {}", id);
            roleService.delete(id);
            logger.info("[eliminarRol] Rol eliminado exitosamente");
            logger.debug("[eliminarRol] Rol con ID {} eliminado", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("[eliminarRol] Error al eliminar el rol: {}", e.getMessage(), e);
            logger.debug("[eliminarRol] Datos del error: {}", e);
            return ResponseEntity.notFound().build();
        }
    }
}
