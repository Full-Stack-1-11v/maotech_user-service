package cl.maotech.user_service.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class RoleTest {

    @Test
    public void testGettersAndSetters(){
        // Instanciar
        Role role = new Role();
        // Verificar setters
        role.setRoleId(1);
        role.setRoleName("Administrador");
        // Verificar getters
        assertEquals(1, role.getRoleId());
        assertEquals("Administrador", role.getRoleName());
    }

    @Test
    public void testToString(){
        // Instanciar
        Role role = new Role(1, "Administrador");
        // Verificar toString
        String expectedString = "Role(roleId=1, roleName=Administrador)";
        assertEquals(expectedString, role.toString());
    }

    @Test
    public void testEqualsAndHashCode(){
        // Instanciar
        Role role1 = new Role(1, "Administrador");
        Role role2 = new Role(1, "Administrador");
        // Verificar equals
        assertEquals(role1, role1);
        assertEquals(role1, role2);
        assertNotEquals(role1, null);
        // Verificar hashCode
        assertEquals(role1.hashCode(), role1.hashCode());
        assertEquals(role1.hashCode(), role2.hashCode());
    }

    @Test
    public void testNotEqualsAndHashCode(){
        // Instanciar
        Role role1 = new Role(1, "Administrador");
        Role role2 = new Role(2, "Instructor");
        // Verificar equals
        assertEquals(role1, role1);
        assertNotEquals(role1, role2);
        assertNotEquals(role1, null);
        // Verificar hashCode
        assertEquals(role1.hashCode(), role1.hashCode());
        assertNotEquals(role1.hashCode(), role2.hashCode());
    }

    @Test
    public void testEqualsAndHashCodeNullValues(){
        // Instanciar
        Role role1 = new Role(null, null);
        Role role2 = new Role(null, null);
        // Verificar equals
        assertEquals(role1, role1);
        assertEquals(role1, role2);
        assertNotEquals(role1, null);
        // Verificar hashCode
        assertEquals(role1.hashCode(), role1.hashCode());
        assertEquals(role1.hashCode(), role2.hashCode());
    }

    @Test
    public void testNotEqualsAndHashCodeOneNullValue(){
        // Instanciar
        Role role1 = new Role(1, "Administrador");
        Role role2 = new Role(1, null);
        // Verificar equals
        assertEquals(role1, role1);
        assertNotEquals(role1, role2);
        assertNotEquals(role1, null);
        // Verificar hashCode
        assertEquals(role1.hashCode(), role1.hashCode());
        assertNotEquals(role1.hashCode(), role2.hashCode());
    }

    @Test
    public void testNotEqualsAndHashCodeDifferentName(){
        // Instanciar
        Role role1 = new Role(1, "Administrador");
        Role role2 = new Role(1, "Instructor");
        // Verificar equals
        assertEquals(role1, role1);
        assertNotEquals(role1, role2);
        assertNotEquals(role1, null);
        // Verificar hashCode
        assertEquals(role1.hashCode(), role1.hashCode());
        assertNotEquals(role1.hashCode(), role2.hashCode());
    }

    @Test
    public void testCanEqual(){
        // Instanciar
        Role role = new Role();
        // Verificar canEqual
        assertTrue(role.canEqual(new Role()));
        assertFalse(role.canEqual("Otro tipo de objeto"));
    }
}
