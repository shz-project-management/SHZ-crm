package CRM.entity.DTO;

import CRM.entity.*;
import lombok.*;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BoardDTO {

    private Long id;
    private UserDTO creatorUser;
    private String name;
    private String description;
    private List<SectionDTO> sections;
    private List<AttributeDTO> types;
    private List<AttributeDTO> statuses;

    public static BoardDTO getBoardFromDB(Board board) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(board.getId());
        boardDTO.setCreatorUser(UserDTO.createUserDTO(board.getCreatorUser()));
        boardDTO.setName(board.getName());
        boardDTO.setDescription(board.getDescription());
        boardDTO.setSections(SectionDTO.getSectionsDTOList(board.getSections()));
        boardDTO.setStatuses(AttributeDTO.getListOfAttributesFromDB(board.getStatuses()));
        boardDTO.setTypes(AttributeDTO.getListOfAttributesFromDB(board.getTypes()));
        return boardDTO;
    }

    public static List<BoardDTO> getListOfBoardsFromDB(List<Board> boards) {
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (Board board : boards) {
            boardDTOList.add(getBoardFromDB(board));
        }
        return boardDTOList;
    }

    public static Map<String,List<BoardDTO>> getMapWithAllBoardsForUser(Map<String,List<Board>> boards) {
        Map<String,List<BoardDTO>> mapBoardListDTO = new HashMap<>();
        boards.forEach((key, value) -> {
            List<BoardDTO> boardDTOList = BoardDTO.getListOfBoardsFromDB(value);
            mapBoardListDTO.put(key, boardDTOList);
        });
        return mapBoardListDTO;
    }
}
