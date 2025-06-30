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

@Component
public class UserDtoModelAssembler implements RepresentationModelAssembler<UserDTO, EntityModel<UserDTO>> {

    @Override
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
