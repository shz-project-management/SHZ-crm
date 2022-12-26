package CRM.entity.DTO;

import CRM.entity.Board;
import CRM.entity.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserDTO {

    private Long id;
    private String fullName;
    private String email;

    public static UserDTO createUserDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }

    public static List<UserDTO> getListOfUsersDTO(List<User> users){
        List<UserDTO> usersDTOList = new ArrayList<>();
        for (User user: users) {
            usersDTOList.add(createUserDTO(user));
        }
        return usersDTOList;
    }
}
