package CRM.service;

import CRM.entity.*;
import CRM.entity.requests.AttributeRequest;
import CRM.repository.BoardRepository;
import CRM.utils.Validations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SectionService {

    @Autowired
    private BoardRepository boardRepository;


    public Section create(AttributeRequest sectionRequest) {
        Board board = Validations.doesIdExists(sectionRequest.getBoardId(), boardRepository);
        Section section = Section.createSection(sectionRequest);
        board.addSectionToBoard(section);
        boardRepository.save(board);
        return section;
    }


    public void delete(long boardId, long sectionId) {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        board.removeSectionFromBoard(sectionId);
        boardRepository.save(board);
    }


    public Section get(long sectionId, long boardId) {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        return board.getSectionFromBoard(sectionId);
    }


//    public Status update(UpdateObjectRequest statusRequest, long statusId) throws NoSuchFieldException {
//        //get in method signature the board id, get the attributeRequest and attributeId and Class clz (type,section,status)
//        //find the board in db
//        //check if attribute exists, if it exists get the attribute and update it
//        //use update method in the board entity
//    }

//    /**
//     * This method is used to retrieve all the statuses.
//     *
//     * @return A list containing all the statuses.
//     */
//    @Override
//    public List<Status> getAll() {
//        get Class clz and boardId in method signature
//        findAll method in board entity.
//
//        return statusRepository.findAll();
//    }

    public List<Section> getAllSectionsInBoard(long boardId) {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        return board.getSections().stream().collect(Collectors.toList());
    }
}
