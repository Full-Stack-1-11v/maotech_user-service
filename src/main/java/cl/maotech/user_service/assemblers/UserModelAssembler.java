package cl.maotech.user_service.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import cl.maotech.user_service.controller.UserControllerV2;
import cl.maotech.user_service.dto.UserDTO;
import cl.maotech.user_service.model.User;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

    @Override
    @NonNull
    public EntityModel<User> toModel(@NonNull User user) {
        
        Link createLink = linkTo(methodOn(UserControllerV2.class).create(user))
                .withRel("create")
                .withType("POST");

        Link selfLink = linkTo(methodOn(UserControllerV2.class).findById(user.getUserId()))
                .withSelfRel()
                .withType("GET");

        Link allLink = linkTo(methodOn(UserControllerV2.class).list())
                .withRel("users")
                .withType("GET");

        Link deleteLink = linkTo(methodOn(UserControllerV2.class).delete(user.getUserId()))
                .withRel("delete")
                .withType("DELETE");

        Link updateLink = linkTo(methodOn(UserControllerV2.class).fullUpdate(user.getUserId(), user))
                .withRel("update")
                .withType("PUT");

        Link inactivesLink = linkTo(methodOn(UserControllerV2.class).findInactives())
                .withRel("inactives")
                .withType("GET");
        
        Link deleteAllInactivesLink = linkTo(methodOn(UserControllerV2.class).deleteAllInactives())
                .withRel("deleteAllInactives")
                .withType("DELETE");

		Link loginLink = linkTo(methodOn(UserControllerV2.class).login(null))
				.withRel("login")
				.withType("POST");

        return EntityModel.of(user, createLink, selfLink, allLink, deleteLink, updateLink, inactivesLink, deleteAllInactivesLink, loginLink);
    }

	@NonNull
	public EntityModel<UserDTO> toModel(@NonNull UserDTO userDTO) {
		
		Link allLink = linkTo(methodOn(UserControllerV2.class).listAsDto())
				.withRel("users")
				.withType("GET");
		
		Link selfLink = linkTo(methodOn(UserControllerV2.class).findByUserDTO(userDTO.getUserId()))
				.withSelfRel()
				.withType("GET");

		Link inactivesLink = linkTo(methodOn(UserControllerV2.class).findInactivesDto())
				.withRel("inactives")
				.withType("GET");

		return EntityModel.of(userDTO, allLink, inactivesLink, selfLink);
	}
}
