package cl.maotech.user_service.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class RoleDtoTest {

    @Test
    public void testGettersAndSetters() {
        // Instanciar
        RoleDTO roleDto = new RoleDTO();
        // Verificar setters
        roleDto.setRoleId(1);
        // Verificar que los setters funcionan correctamente
        assertNotNull(roleDto);
        assertNotNull(roleDto.getRoleId());
        // Verificar getters
        assertEquals(1, roleDto.getRoleId());        
    }

    @Test
    public void testEqualsAndHashCode() {
        // Instanciar
        RoleDTO roleDto1 = new RoleDTO(1);
        RoleDTO roleDto2 = new RoleDTO(1);
        // Verificar equals
        assertEquals(roleDto1, roleDto1);
        assertEquals(roleDto1, roleDto2);
        assertNotEquals(roleDto1, null);
        // Verificar hashCode
        assertEquals(roleDto1.hashCode(), roleDto1.hashCode());
        assertEquals(roleDto1.hashCode(), roleDto2.hashCode());
    }
    
    @Test
    public void testNotEqualsAndHashCode() {
        // Instanciar
        RoleDTO roleDto1 = new RoleDTO(1);
        RoleDTO roleDto2 = new RoleDTO(2);
        // Verificar equals
        assertEquals(roleDto1, roleDto1);
        assertNotEquals(roleDto1, roleDto2);
        assertNotEquals(roleDto1, null);
        // Verificar hashCode
        assertNotEquals(roleDto1.hashCode(), roleDto2.hashCode());
    }

    @Test
    public void testCanEqual() {
        // Instanciar
        RoleDTO roleDto = new RoleDTO();
        // Verificar canEqual
        assertTrue(roleDto.canEqual(new RoleDTO()));
        assertFalse(roleDto.canEqual("Otro tipo de objeto"));
    }
}
