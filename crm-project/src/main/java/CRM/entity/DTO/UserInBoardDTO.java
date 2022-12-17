package CRM.entity.DTO;

import CRM.entity.UserInBoard;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class UserInBoardDTO {

    private UserDTO user;
    private BoardDTO board;

    public static UserInBoardDTO getUserInBoardFromDB(UserInBoard userInBoard){
        UserInBoardDTO uib = new UserInBoardDTO();
        uib.setUser(UserDTO.getUserFromDB(userInBoard.getUser()));
        uib.setBoard(BoardDTO.getBoardFromDB(userInBoard.getBoard()));
        return uib;
    }
}
