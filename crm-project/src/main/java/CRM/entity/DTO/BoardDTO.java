package CRM.entity.DTO;

import CRM.entity.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public static BoardDTO createPlainBoard(Board board) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(board.getId());
        boardDTO.setCreatorUser(UserDTO.createUserDTO(board.getCreatorUser()));
        boardDTO.setName(board.getName());
        boardDTO.setDescription(board.getDescription());
        return boardDTO;
    }

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

    public static BoardDTO getBoardPreviewFromDB(Board board) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(board.getId());
        boardDTO.setName(board.getName());
        boardDTO.setDescription(Objects.toString(board.getDescription(), ""));
        return boardDTO;
    }

    public static List<BoardDTO> getListOfBoardsFromDB(List<Board> boards) {
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (Board board : boards) {
            boardDTOList.add(getBoardFromDB(board));
        }
        return boardDTOList;
    }

    public static List<BoardDTO> getListOfBoardPreviewsFromDB(List<Board> boards) {
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (Board board : boards) {
            boardDTOList.add(getBoardPreviewFromDB(board));
        }
        return boardDTOList;
    }
}
