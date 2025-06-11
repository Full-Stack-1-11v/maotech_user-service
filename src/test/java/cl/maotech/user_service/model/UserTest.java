package cl.maotech.user_service.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    void testGettersAndSetters(){
        User user = new User();
        Role role1 = new Role();
        Role role2 = new Role();
        Role role3 = new Role();

        user.setUserId(1);
        user.setEmail("test@mail.com");
        user.setPassword("password123");
        user.setRut("11.111.111-1");
        user.setFirstName("Lionel");
        user.setLastName("Messi");
        user.setStatus(true);

        user.setRole(role1);
        assertEquals(role1, user.getRole());

        user.setRole(role2);
        assertEquals(role2, user.getRole());
        
        user.setRole(role3);
        assertEquals(role3, user.getRole());

        assertEquals(1, user.getUserId());
        assertEquals("test@mail.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("11.111.111-1", user.getRut());
        assertEquals("Lionel", user.getFirstName());
        assertEquals("Messi", user.getLastName());
        assertTrue(user.getStatus());
    }
}
