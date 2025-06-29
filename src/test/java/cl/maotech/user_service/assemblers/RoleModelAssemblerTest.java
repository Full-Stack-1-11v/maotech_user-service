package cl.maotech.user_service.assemblers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.context.ActiveProfiles;

import cl.maotech.user_service.model.Role;

@SpringBootTest
@ActiveProfiles("test")
public class RoleModelAssemblerTest {

    @Test
    public void toModel() {
        // Instanciar
        RoleModelAssembler assembler = new RoleModelAssembler();
        Role role = new Role();
        role.setRoleId(1);
        role.setRoleName("Administrador");
        EntityModel<Role> model = assembler.toModel(role);
        // Verificar que el modelo no sea nulo
        assert model != null;
        // Verificar que el modelo contenga los enlaces esperados
        assertThat(model.getContent()).isEqualTo(role);
        assertThat(model.getLinks()).anyMatch(createLink -> createLink.getRel().value().equals("create"))
                .anyMatch(selfLink -> selfLink.getRel().value().equals("self"))
                .anyMatch(allLink -> allLink.getRel().value().equals("roles"))
                .anyMatch(deleteLink -> deleteLink.getRel().value().equals("delete"));    
    }
    
}
