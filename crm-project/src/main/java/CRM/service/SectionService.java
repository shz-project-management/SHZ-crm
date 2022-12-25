package CRM.service;

import CRM.entity.*;
import CRM.entity.requests.AttributeRequest;
import CRM.repository.BoardRepository;
import CRM.utils.Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SectionService {

    @Autowired
    private BoardRepository boardRepository;


    //TODO documentation
    public Set<Section> create(AttributeRequest sectionRequest) {
        Board board = Common.getBoard(sectionRequest.getBoardId(), boardRepository);
        Section section = Section.createSection(sectionRequest);
        board.addSectionToBoard(section);
        boardRepository.save(board);
        return board.getSections();
    }


    //TODO documentation
    public void delete(long boardId, long sectionId) {
        Board board = Common.getBoard(boardId, boardRepository);
        board.removeSectionFromBoard(sectionId);
        boardRepository.save(board);
    }


    //TODO documentation
    public Section get(long sectionId, long boardId) {
        Board board = Common.getBoard(boardId, boardRepository);
        return board.getSectionFromBoard(sectionId);
    }

    //TODO + documentation
//    public Status update(UpdateObjectRequest statusRequest, long statusId) throws NoSuchFieldException {
//        //get in method signature the board id, get the attributeRequest and attributeId and Class clz (type,section,status)
//        //find the board in db
//        //check if attribute exists, if it exists get the attribute and update it
//        //use update method in the board entity
//    }

    //TODO documentation
    public List<Section> getAllSectionsInBoard(long boardId) {
        Board board = Common.getBoard(boardId, boardRepository);
        return new ArrayList<>(board.getSections());
    }
}
