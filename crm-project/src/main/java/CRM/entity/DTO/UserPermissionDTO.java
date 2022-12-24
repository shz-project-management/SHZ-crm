package CRM.entity.DTO;

import CRM.entity.UserPermission;
import CRM.utils.enums.Permission;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPermissionDTO {

    private Long id;
    private UserDTO user;
    private Permission permission;

    public static UserPermissionDTO createUserPermissionDTO(UserPermission userPermission){
        UserPermissionDTO userPermissionDTO = new UserPermissionDTO();
        userPermissionDTO.setId(userPermission.getId());
        userPermissionDTO.setUser(UserDTO.createUserDTO(userPermission.getUser()));
        userPermissionDTO.setPermission(userPermission.getPermission());
        return userPermissionDTO;
    }

    public static List<UserPermissionDTO> getListOfUserPermissionFromDB(List<UserPermission> userPermission){
        List<UserPermissionDTO> userPermissionDTOList = new ArrayList<>();
        for (UserPermission user: userPermission) {
            userPermissionDTOList.add(createUserPermissionDTO(user));
        }
        return userPermissionDTOList;
    }
}
