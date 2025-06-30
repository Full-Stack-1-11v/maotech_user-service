package cl.maotech.user_service.assemblers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.context.ActiveProfiles;

import cl.maotech.user_service.dto.UserDTO;

@SpringBootTest
@ActiveProfiles("test")
public class UserDtoModelAssemblerTest {

    @Test
    public void toModelUserDTO() {
        // Instanciar
        UserDtoModelAssembler assembler = new UserDtoModelAssembler();
        UserDTO user = new UserDTO(1, "test@mail.com", "11.111.111-1", "Admin1", "Admin1", true, null);
        EntityModel<UserDTO> model = assembler.toModel(user);
        // Verificar que el modelo no sea nulo
        assert model != null;
        // Verificar que el modelo contenga los enlaces esperados
        assertThat(model.getContent()).isEqualTo(user);
        assertThat(model.getLinks()).anyMatch(selfLink -> selfLink.getRel().value().equals("self"))
                .anyMatch(allLink -> allLink.getRel().value().equals("users"))
                .anyMatch(inactivesLink -> inactivesLink.getRel().value().equals("inactives"));
    }
}
