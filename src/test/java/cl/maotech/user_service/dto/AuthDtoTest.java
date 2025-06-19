package cl.maotech.user_service.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class AuthDtoTest {

    @Test
    public void testGettersAndSetters() {
        // Instanciar
        AuthDTO authDto = new AuthDTO();
        // Verificar setters
        authDto.setEmail("test@mail.com");
        authDto.setPassword("password123");
        // Verificar que los setters funcionan correctamente
        assertEquals(authDto, authDto);
        assertEquals("test@mail.com", authDto.getEmail());
        assertEquals("password123", authDto.getPassword());
    }
     
    @Test
    public void testToString() {
        // Instanciar
        AuthDTO authDto = new AuthDTO("test@mail.com", "password123");
        // Verificar toString
        String expectedString = "AuthDTO(email=test@mail.com, password=password123)";
        assertEquals(expectedString, authDto.toString());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Instanciar
        AuthDTO authDto1 = new AuthDTO("test@mail.com", "password123");
        AuthDTO authDto2 = new AuthDTO("test@mail.com", "password123");
        // Verificar equals
        assertEquals(authDto1, authDto1);
        assertEquals(authDto1, authDto2);
        assertNotEquals(authDto1, null);
        // Verificar hashCode
        assertEquals(authDto1.hashCode(), authDto1.hashCode());
        assertEquals(authDto1.hashCode(), authDto2.hashCode());
    }

    @Test
    public void testNotEqualsAndHashCode() {
        // Instanciar
        AuthDTO authDto1 = new AuthDTO("test@mail.com", "password123");
        AuthDTO authDto2 = new AuthDTO("test2@mail.com", "password456");
        // Verificar not equals
        assertEquals(authDto1, authDto1);
        assertNotEquals(authDto1, authDto2);
        assertNotEquals(authDto1, null);
        // Verificar hashCode
        assertEquals(authDto1.hashCode(), authDto1.hashCode());
        assertNotEquals(authDto1.hashCode(), authDto2.hashCode());
    }

    @Test
    public void testEqualsAndHashCodeNullValues() {
        // Instanciar
        AuthDTO authDto1 = new AuthDTO(null, null);
        AuthDTO authDto2 = new AuthDTO(null, null);
        // Verificar equals
        assertEquals(authDto1, authDto1);
        assertEquals(authDto1, authDto2);
        assertNotEquals(authDto1, null);
        // Verificar hashCode
        assertEquals(authDto1.hashCode(), authDto1.hashCode());
        assertEquals(authDto1.hashCode(), authDto2.hashCode());
    }   

    @Test
    public void testNotEqualsAndHashCodeOneNullValue() {
        // Instanciar
        AuthDTO authDto1 = new AuthDTO("test@mail.com", "password123");
        AuthDTO authDto2 = new AuthDTO("test@mail.com", null);
        // Verificar equals
        assertEquals(authDto1, authDto1);
        assertNotEquals(authDto1, authDto2);
        assertNotEquals(authDto1, null);
        // Verificar hashCode
        assertEquals(authDto1.hashCode(), authDto1.hashCode());
        assertNotEquals(authDto1.hashCode(), authDto2.hashCode());
    }

    @Test
    public void testCanEqual() {
        // Instanciar
        AuthDTO authDto = new AuthDTO();
        // Verificar canEqual
        assertEquals(true, authDto.canEqual(new AuthDTO()));
        assertEquals(false, authDto.canEqual(new Object()));
    }
}
