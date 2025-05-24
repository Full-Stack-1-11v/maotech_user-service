package cl.maotech.user_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        User newUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> list() {
        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(users);
        }
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<User> findById(@PathVariable Integer id) {
        try {
            User user = userService.findById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<User> update(@PathVariable Integer id, @RequestBody User user) {
        try {
            User up_user = userService.findById(id);
            up_user.setUser_id(id);
            up_user.setRut(user.getRut());
            up_user.setFirst_name(user.getFirst_name());
            up_user.setLast_name(user.getLast_name());
            up_user.setEmail(user.getEmail());
            up_user.setStatus(user.getStatus());

            userService.save(up_user);
            return ResponseEntity.ok(up_user);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
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
    
    @GetMapping("/inactives")
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
    
    @DeleteMapping("/inactives/delete")
    public ResponseEntity<?> deleteAllInactives() {
        try {
            userService.deleteStatusFalse();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
