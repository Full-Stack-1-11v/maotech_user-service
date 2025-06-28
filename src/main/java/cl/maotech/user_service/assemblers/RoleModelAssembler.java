package cl.maotech.user_service.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import cl.maotech.user_service.controller.RoleControllerV2;
import cl.maotech.user_service.model.Role;


@Component
public class RoleModelAssembler implements RepresentationModelAssembler<Role, EntityModel<Role>> {

    @Override
    @NonNull
    public EntityModel<Role> toModel(@NonNull Role role) {
        
        Link createLink = linkTo(methodOn(RoleControllerV2.class).create(role))
                .withRel("create")
                .withType("POST");

        Link selfLink = linkTo(methodOn(RoleControllerV2.class).findById(role.getRoleId()))
                .withSelfRel()
                .withType("GET");

        Link allLink = linkTo(methodOn(RoleControllerV2.class).list())
                .withRel("roles")
                .withType("GET");

        Link deleteLink = linkTo(methodOn(RoleControllerV2.class).delete(role.getRoleId()))
                .withRel("delete")
                .withType("DELETE");

        return EntityModel.of(role, createLink, selfLink, allLink, deleteLink);
    }        

}
