package cl.maotech.user_service.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class UserDtoTest {

    @Test
    public void testGettersAndSetters() {
        // Instanciar
        UserDTO userDto = new UserDTO();
        // Verificar setters
        userDto.setUserId(1);
        userDto.setEmail("test@mail.com");
        userDto.setRut("11.111.111-1");
        userDto.setFirstName("Admin1");
        userDto.setLastName("Admin1");
        userDto.setStatus(true);
        // Verificar que los setters funcionan correctamente
        assertEquals(1, userDto.getUserId());
        assertEquals("test@mail.com", userDto.getEmail());
        assertEquals("11.111.111-1", userDto.getRut());
        assertEquals("Admin1", userDto.getFirstName());
        assertEquals("Admin1", userDto.getLastName());
        assertTrue(userDto.getStatus());        
    }

    @Test
    public void testToString() {
        // Instanciar
        UserDTO userDto = new UserDTO(1, "test@mail.com", "11.111.111-1", "Admin1", "Admin1", true, null);
        // Verificar toString
        String expectedString = "UserDTO(userId=1, email=test@mail.com, rut=11.111.111-1, firstName=Admin1, lastName=Admin1, status=true, role=null)";
        assertEquals(expectedString, userDto.toString());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Instanciar
        UserDTO userDto1 = new UserDTO(1, "test@mail.com", "11.111.111-1", "Admin1", "Admin1", true, null);
        UserDTO userDto2 = new UserDTO(1, "test@mail.com", "11.111.111-1", "Admin1", "Admin1", true, null);
        // Verificar equals
        assertEquals(userDto1, userDto1);
        assertEquals(userDto1, userDto2);
        assertNotEquals(userDto1, null);
        // Verificar hashCode
        assertEquals(userDto1.hashCode(), userDto1.hashCode());
        assertEquals(userDto1.hashCode(), userDto2.hashCode());
    }

    @Test
    public void testNotEqualsAndHashCode() {
        // Instanciar
        UserDTO userDto1 = new UserDTO(1, "test@mail.com", "11.111.111-1", "Admin1", "Admin1", true, null);
        UserDTO userDto2 = new UserDTO(2, "test2@mail.com", "22.222.222-2", "Admin2", "Admin2", false, null);
        // Verificar not equals
        assertEquals(userDto1, userDto1);
        assertNotEquals(userDto1, userDto2);
        assertNotEquals(userDto1, null);
        // Verificar hashCode
        assertEquals(userDto1.hashCode(), userDto1.hashCode());
        assertNotEquals(userDto1.hashCode(), userDto2.hashCode());
    }

    @Test
    public void testEqualsAndHashCodeNullValues() {
        // Instanciar
        UserDTO userDto1 = new UserDTO(null, null, null, null, null, null, null);
        UserDTO userDto2 = new UserDTO(null, null, null, null, null, null, null);
        // Verificar equals
        assertEquals(userDto1, userDto1);
        assertEquals(userDto1, userDto2);
        assertNotEquals(userDto1, null);
        // Verificar hashCode
        assertEquals(userDto1.hashCode(), userDto1.hashCode());
        assertEquals(userDto1.hashCode(), userDto2.hashCode());
    }

    @Test
    public void testNotEqualsAndHashCodeOneNullValue() {
        // Instanciar
        UserDTO userDto1 = new UserDTO(1, "test@mail.com", "11.111.111-1", "Admin1", "Admin1", true, null);
        UserDTO userDto2 = new UserDTO(1, null, "11.111.111-1", "Admin1", "Admin1", true, null);
        // Verificar equals
        assertEquals(userDto1, userDto1);
        assertNotEquals(userDto1, userDto2);
        assertNotEquals(userDto1, null);
        // Verificar hashCode
        assertEquals(userDto1.hashCode(), userDto1.hashCode());
        assertNotEquals(userDto1.hashCode(), userDto2.hashCode());
    }

    @Test
    public void testNotEqualsAndHashCodeDifferentMails() {
        // Instanciar
        UserDTO userDto1 = new UserDTO(1, "test@mail.com", "11.111.111-1", "Admin1", "Admin1", true, null);
        UserDTO userDto2 = new UserDTO(1, "test2@mail.com", "11.111.111-1", "Admin1", "Admin1", true, null);
        // Verificar equals
        assertEquals(userDto1, userDto1);
        assertNotEquals(userDto1, userDto2);
        assertNotEquals(userDto1, null);
        // Verificar hashCode
        assertEquals(userDto1.hashCode(), userDto1.hashCode());
        assertNotEquals(userDto1.hashCode(), userDto2.hashCode());
    }
}
