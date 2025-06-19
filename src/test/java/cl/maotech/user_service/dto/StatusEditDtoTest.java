package cl.maotech.user_service.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class StatusEditDtoTest {

    @Test
    public void testGettersAndSetters() {
        // Instanciar
        StatusEditDTO statusEditDto = new StatusEditDTO();
        // Verificar setters
        statusEditDto.setStatus(true);
        // Verificar que los setters funcionan correctamente
        assert statusEditDto != null;
        assert statusEditDto.getStatus() != null;
        // Verificar getters
        assert statusEditDto.getStatus() == true;
    }

    @Test
    public void testToString() {
        // Instanciar
        StatusEditDTO statusEditDto = new StatusEditDTO(true);
        // Verificar toString
        String expectedString = "StatusEditDTO(status=true)";
        assert statusEditDto.toString().equals(expectedString);
    }

    @Test
    public void testEqualsAndHashCode() {
        // Instanciar
        StatusEditDTO statusEditDto1 = new StatusEditDTO(true);
        StatusEditDTO statusEditDto2 = new StatusEditDTO(true);
        // Verificar equals
        assert statusEditDto1.equals(statusEditDto1);
        assert statusEditDto1.equals(statusEditDto2);
        assert !statusEditDto1.equals(null);
        // Verificar hashCode
        assert statusEditDto1.hashCode() == statusEditDto1.hashCode();
        assert statusEditDto1.hashCode() == statusEditDto2.hashCode();
    }

    @Test
    public void testNotEqualsAndHashCode() {
        // Instanciar
        StatusEditDTO statusEditDto1 = new StatusEditDTO(true);
        StatusEditDTO statusEditDto2 = new StatusEditDTO(false);
        // Verificar not equals
        assert !statusEditDto1.equals(statusEditDto2);
        assert !statusEditDto1.equals(null);
        // Verificar hashCode
        assert statusEditDto1.hashCode() != statusEditDto2.hashCode();
    }
    
    @Test
    public void testEqualsAndHashCodeNullValues() {
        // Instanciar
        StatusEditDTO statusEditDto1 = new StatusEditDTO(null);
        StatusEditDTO statusEditDto2 = new StatusEditDTO(null);
        // Verificar equals
        assert statusEditDto1.equals(statusEditDto1);
        assert statusEditDto1.equals(statusEditDto2);
        assert !statusEditDto1.equals(null);
        // Verificar hashCode
        assert statusEditDto1.hashCode() == statusEditDto1.hashCode();
        assert statusEditDto1.hashCode() == statusEditDto2.hashCode();
    }
}
