package CRM.entity.DTO;

import CRM.entity.*;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BoardDTO {

    //FIXME: change User, Item, Type, Status to DTO's
    private Long id;
    private UserDTO creatorUser;
    private String name;
    private String description;
    // FIXME: put all these stats in DTO also
//    private Set<Item> sections;
//    private Set<Type> types;
//    private Set<Status> statuses;

    public static BoardDTO createPlainBoard(Board board){
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(board.getId());
        boardDTO.setCreatorUser(UserDTO.getUserFromDB(board.getCreatorUser()));
        boardDTO.setName(board.getName());
        boardDTO.setDescription(board.getDescription());
        return boardDTO;
    }

    public static BoardDTO getBoardFromDB(Board board){
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(board.getId());
        boardDTO.setCreatorUser(UserDTO.getUserFromDB(board.getCreatorUser()));
        boardDTO.setName(board.getName());
        boardDTO.setDescription(board.getDescription());
//        boardDTO.setItems(board.getItems());
//        boardDTO.setStatuses(board.getStatuses());
//        boardDTO.setTypes(board.getTypes());
        return boardDTO;
    }

    public static List<BoardDTO> getListOfBoardsFromDB(List<Board> boards){
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (Board board: boards) {
            boardDTOList.add(getBoardFromDB(board));
        }
        return boardDTOList;
    }
}
