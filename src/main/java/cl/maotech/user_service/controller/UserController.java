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

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<User> create(@RequestBody User user) {
        try {
            User newUser = userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/list")
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
    public ResponseEntity<User> findById(@PathVariable Integer id) {
        try {
            User user = userService.findById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/full-update")
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
    public ResponseEntity<User> update(@PathVariable Integer id, @RequestBody UserEditDTO editDTO) {
        try {
            User user = userService.findById(id);
            User up_user = userService.updateFromDto(editDTO, user);

            userService.save(up_user);
            return ResponseEntity.ok(up_user);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }        
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<User> deactivateUser(@PathVariable Integer id, @RequestBody StatusEditDTO statusDTO) {
        try {
            User user = userService.findById(id);
            User d_user = userService.updateStatusDto(statusDTO, user);

            userService.save(d_user);
            return ResponseEntity.ok(d_user);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }        
    }

    @PutMapping("/{id}/role/edit")
    public ResponseEntity<User> updateRole(@PathVariable Integer id, @RequestBody RoleDTO roleDTO) {
        try {
            User user = userService.findById(id);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            User updatedUser = userService.updateRoleDto(roleDTO.getRoleId(), user);
            userService.save(updatedUser);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            userService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/inactives/admin")
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
    public ResponseEntity<?> deleteAllInactives() {
        try {
            userService.deleteStatusFalse();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
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
