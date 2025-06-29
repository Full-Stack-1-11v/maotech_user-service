package cl.maotech.user_service.assemblers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.context.ActiveProfiles;

import cl.maotech.user_service.dto.UserDTO;
import cl.maotech.user_service.model.User;

@SpringBootTest
@ActiveProfiles("test")
public class UserModelAssemblerTest {

    @Test
    public void toModel() {
        // Instanciar
        UserModelAssembler assembler = new UserModelAssembler();
        User user = new User(1, "test@mail.com", "password123", "11.111.111-1", "Admin1", "Admin1", true, null);
        EntityModel<User> model = assembler.toModel(user);
        // Verificar que el modelo no sea nulo
        assert model != null;
        // Verificar que el modelo contenga los enlaces esperados
        assertThat(model.getContent()).isEqualTo(user);
        assertThat(model.getLinks()).anyMatch(createLink -> createLink.getRel().value().equals("create"))
                .anyMatch(selfLink -> selfLink.getRel().value().equals("self"))
                .anyMatch(allLink -> allLink.getRel().value().equals("users"))
                .anyMatch(deleteLink -> deleteLink.getRel().value().equals("delete"))
                .anyMatch(updateLink -> updateLink.getRel().value().equals("update"))
                .anyMatch(inactivesLink -> inactivesLink.getRel().value().equals("inactives"))
                .anyMatch(deleteAllInactivesLink -> deleteAllInactivesLink.getRel().value().equals("deleteAllInactives"))
                .anyMatch(loginLink -> loginLink.getRel().value().equals("login"));
    }

    @Test
    public void toModelUserDTO() {
        // Instanciar
        UserModelAssembler assembler = new UserModelAssembler();
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
