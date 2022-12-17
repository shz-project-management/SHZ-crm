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

    private Long id;
    private User creatorUser;
    private String name;
    private String description;
    private Set<Item> items;
    private Set<Type> types;
    private Set<Status> statuses;

    public static BoardDTO createPlainBoard(Board board){
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(board.getId());
        boardDTO.setCreatorUser(board.getCreatorUser());
        boardDTO.setName(board.getName());
        boardDTO.setDescription(board.getDescription());
        return boardDTO;
    }

    public static BoardDTO getBoardFromDB(Board board){
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(board.getId());
        boardDTO.setCreatorUser(board.getCreatorUser());
        boardDTO.setName(board.getName());
        boardDTO.setDescription(board.getDescription());
        board.setItems(board.getItems());
        board.setStatuses(board.getStatuses());
        board.setTypes(board.getTypes());
        return boardDTO;
    }

    public static List<BoardDTO> getListOfBoardsFromDB(List<Board> boards){
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (Board board: boards) {
            BoardDTO boardDTO = new BoardDTO();
            boardDTO.setId(board.getId());
            boardDTO.setCreatorUser(board.getCreatorUser());
            boardDTO.setName(board.getName());
            boardDTO.setDescription(board.getDescription());
            board.setItems(board.getItems());
            board.setStatuses(board.getStatuses());
            board.setTypes(board.getTypes());
            boardDTOList.add(boardDTO);
        }
        return boardDTOList;
    }

}

