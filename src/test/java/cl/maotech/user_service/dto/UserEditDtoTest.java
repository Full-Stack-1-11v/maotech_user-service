package cl.maotech.user_service.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class UserEditDtoTest {

    @Test
    public void testGettersAndSetters() {
        // Instanciar
        UserEditDTO userEditDto = new UserEditDTO();
        // Verificar setters
        userEditDto.setUserId(1);
        userEditDto.setEmail("test@mail.com");
        userEditDto.setFirstName("Admin1");
        userEditDto.setLastName("Admin1");
        // Verificar que los setters funcionan correctamente
        assertEquals(1, userEditDto.getUserId());
        assertEquals("test@mail.com", userEditDto.getEmail());
        assertEquals("Admin1", userEditDto.getFirstName());
        assertEquals("Admin1", userEditDto.getLastName());
    }

    @Test
    public void testToString() {
        // Instanciar
        UserEditDTO userEditDto = new UserEditDTO(1, "test@mail.com", "Admin1", "Admin1");
        // Verificar toString
        String expectedString = "UserEditDTO(userId=1, email=test@mail.com, firstName=Admin1, lastName=Admin1)";
        assertEquals(expectedString, userEditDto.toString());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Instanciar
        UserEditDTO userEditDto1 = new UserEditDTO(1, "test@mail.com", "Admin1", "Admin1");
        UserEditDTO userEditDto2 = new UserEditDTO(1, "test@mail.com", "Admin1", "Admin1");
        // Verificar equals
        assertEquals(userEditDto1, userEditDto1);
        assertEquals(userEditDto1, userEditDto2);
        assertNotEquals(userEditDto1, null);
        // Verificar hashCode
        assertEquals(userEditDto1.hashCode(), userEditDto1.hashCode());
        assertEquals(userEditDto1.hashCode(), userEditDto2.hashCode());
    }
    
    @Test
    public void testNotEqualsAndHashCode() {
        // Instanciar
        UserEditDTO userEditDto1 = new UserEditDTO(1, "test@mail.com", "Admin1", "Admin1");
        UserEditDTO userEditDto2 = new UserEditDTO(2, "test2@mail.com", "Admin2", "Admin2");
        // Verificar not equals
        assertEquals(userEditDto1, userEditDto1);
        assertNotEquals(userEditDto1, userEditDto2);
        assertNotEquals(userEditDto1, null);
        // Verificar hashCode
        assertEquals(userEditDto1.hashCode(), userEditDto1.hashCode());
        assertNotEquals(userEditDto1.hashCode(), userEditDto2.hashCode());
    }

    @Test
    public void testEqualsAndHashCodeNullValues() {
        // Instanciar
        UserEditDTO userEditDto1 = new UserEditDTO(null, null, null, null);
        UserEditDTO userEditDto2 = new UserEditDTO(null, null, null, null);
        // Verificar equals
        assertEquals(userEditDto1, userEditDto1);
        assertEquals(userEditDto1, userEditDto2);
        assertNotEquals(userEditDto1, null);
        // Verificar hashCode
        assertEquals(userEditDto1.hashCode(), userEditDto1.hashCode());
        assertEquals(userEditDto1.hashCode(), userEditDto2.hashCode());
    }

    @Test
    public void testNotEqualsAndHashCodeOneNullValue() {
        // Instanciar
        UserEditDTO userEditDto1 = new UserEditDTO(1, "test@mail.com", "Admin1", "Admin1");
        UserEditDTO userEditDto2 = new UserEditDTO(1, null, "Admin1", "Admin1");
        // Verificar equals
        assertEquals(userEditDto1, userEditDto1);
        assertNotEquals(userEditDto1, userEditDto2);
        assertNotEquals(userEditDto1, null);
        // Verificar hashCode
        assertEquals(userEditDto1.hashCode(), userEditDto1.hashCode());
        assertNotEquals(userEditDto1.hashCode(), userEditDto2.hashCode());
    }

    @Test
    public void testNotEqualsAndHashCodeDifferentMails() {
        // Instanciar
        UserEditDTO userEditDto1 = new UserEditDTO(1, "test@mail.com", "Admin1", "Admin1");
        UserEditDTO userEditDto2 = new UserEditDTO(1, "test2@mail.com", "Admin1", "Admin1");
        // Verificar equals
        assertEquals(userEditDto1, userEditDto1);
        assertNotEquals(userEditDto1, userEditDto2);
        assertNotEquals(userEditDto1, null);
        // Verificar hashCode
        assertEquals(userEditDto1.hashCode(), userEditDto1.hashCode());
        assertNotEquals(userEditDto1.hashCode(), userEditDto2.hashCode());
    }
}
