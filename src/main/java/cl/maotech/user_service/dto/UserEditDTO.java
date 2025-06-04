package cl.maotech.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEditDTO {

    private Integer userId;

    private String email;

    private String firstName;

    private String lastName;

}
