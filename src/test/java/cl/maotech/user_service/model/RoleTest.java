package cl.maotech.user_service.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

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
    public void testEqualsAndHashCode(){
        // Instanciar
        Role role1 = new Role(1, "Administrador");
        Role role2 = new Role(1, "Administrador");
        Role role3 = new Role(2, "Instructor");
        // Verificar equals
        assertEquals(role1, role1);
        assertEquals(role1, role2);
        assertNotEquals(role1, role3);
        assertNotEquals(role1, null);
        assertNotEquals(role1, "Otro tipo de objeto");
        // Verificar hashCode
        assertEquals(role1.hashCode(), role1.hashCode());
        assertEquals(role1.hashCode(), role2.hashCode());
        assertNotEquals(role1.hashCode(), role3.hashCode());
    }

    @Test
    public void testEqualsAndHashCodeNullFields(){
        // Instanciar
        Role role1 = new Role(null, null);
        Role role2 = new Role(null, null);
        // Verificar equals
        assertEquals(role1, role1);
        assertEquals(role1, role2);
        //assertEquals(role1, null);
        // Verificar hashCode
        assertEquals(role1.hashCode(), role1.hashCode());
        assertEquals(role1.hashCode(), role2.hashCode());
    }

    @Test
    public void testEqualsAndHashOneNullField(){
        // Instanciar
        Role role1 = new Role(1, "Administrador");
        Role role2 = new Role(1, null);
        // Verificar equals
        assertEquals(role1, role1);
        //assertEquals(role1, role2);
        // Verificar hashCode
        assertEquals(role1.hashCode(), role1.hashCode());
        //assertEquals(role1.hashCode(), role2.hashCode());
    }

    @Test
    public void testToString(){
        // Instanciar
        Role role = new Role(1, "Administrador");
        String str = role.toString();
        // Verificar toString
        assertNotNull(str);
        assertTrue(str.contains("Administrador"));
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
