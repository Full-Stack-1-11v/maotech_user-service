package cl.maotech.user_service.dto;

import cl.maotech.user_service.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Integer userId;

    private String email;

    private String rut;

    private String firstName;

    private String lastName;

    private Boolean status;

    private Role role;

}
