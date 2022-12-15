package CRM.entity.DTO;

import CRM.entity.*;
import lombok.*;

import javax.persistence.*;
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
        boardDTO.setCreatorUser(board.getCreatorUser());
        boardDTO.setName(board.getName());
        boardDTO.setDescription(board.getDescription());
        return boardDTO;
    }

}

