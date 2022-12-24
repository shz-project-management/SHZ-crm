package CRM.entity.DTO;

import CRM.entity.User;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserDTO {

    private Long id;
    private String fullName;

    public static UserDTO getUserFromDB(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFullName(user.getFullName());
        return userDTO;
    }
}
