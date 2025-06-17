package cl.maotech.user_service.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void testGettersAndSetters(){
        // Instanciar
        User user = new User();
        Role role1 = new Role();
        Role role2 = new Role();
        Role role3 = new Role();

        // Verificar setters
        user.setUserId(1);
        user.setEmail("test@mail.com");
        user.setPassword("password123");
        user.setRut("11.111.111-1");
        user.setFirstName("Admin1");
        user.setLastName("Admin1");
        user.setStatus(true);

        // Verificar getters
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
        assertEquals("Admin1", user.getFirstName());
        assertEquals("Admin1", user.getLastName());
        assertTrue(user.getStatus());
    }

    @Test
    public void testEqualsAndHashCode(){
        // Instanciar
        User user1 = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
        User user2 = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
        User user3 = new User(2, "test2@mail.com", "password123", "22.222.222-2", "Instructor1", "Instructor1", false, null);
        // Verificar equals
        assertEquals(user1, user1);
        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertNotEquals(user1, null);
        assertNotEquals(user1, "Otro tipo de objeto");
        // Verificar hashCode
        assertEquals(user1.hashCode(), user1.hashCode());
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }

    @Test
    public void testEqualsAndHashCodeNullFields(){
        // Instanciar
        User user1 = new User(null, null, null, null, null, null, false, null);
        User user2 = new User(null, null, null, null, null, null, false, null);
        // Verificar equals
        assertEquals(user1, user1);
        assertEquals(user1, user2);
        //assertEquals(user1, null);
        // Verificar hashCode
        assertEquals(user1.hashCode(), user1.hashCode());
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void testEqualsAndHashCodeOneNullField(){
        // Instanciar
        User user1 = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
        User user2 = new User(1, null, "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
        // Verificar equals
        assertEquals(user1, user1);
        assertNotEquals(user1, user2);
        // Verificar hashCode
        assertEquals(user1.hashCode(), user1.hashCode());
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    public void testToString(){
        // Instanciar
        User user = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
        String str = user.toString();
        // Verificar toString
        assertNotNull(str);
        assertTrue(str.contains("Admin1"));
    }

    @Test
    public void testCanEqual(){
        // Instanciar
        User user = new User();
        // Verificar canEqual
        assertTrue(user.canEqual(new User()));
        assertFalse(user.canEqual("Otro tipo de objeto"));
    }
}
