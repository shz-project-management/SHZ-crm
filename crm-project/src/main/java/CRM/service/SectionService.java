package CRM.service;

import CRM.entity.*;
import CRM.entity.requests.AttributeRequest;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.repository.BoardRepository;
import CRM.utils.Common;
import CRM.utils.Validations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.*;

import static CRM.utils.Util.sectionId;

@Service
public class SectionService {

    @Autowired
    private BoardRepository boardRepository;

    //FIXME - maybe to put it outside
//    @Autowired
//    private EntityManager entityManager;
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    //TODO documentation
    public Set<Section> create(AttributeRequest sectionRequest) {
        Board board = Validations.doesIdExists(sectionRequest.getBoardId(), boardRepository);
        Section section = Section.createSection(sectionRequest);
        board.addSectionToBoard(section);
        boardRepository.save(board);
        return board.getSections();
    }


    //TODO documentation
    public void delete(long boardId, long sectionId) {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        board.removeSectionFromBoard(sectionId);
        boardRepository.save(board);
    }


    //TODO documentation
    public Section get(long sectionId, long boardId) {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        return board.getSectionFromBoard(sectionId);
    }

    //TODO + documentation
    public Section update(UpdateObjectRequest updateObject) throws NoSuchFieldException {
        Board board = Validations.doesIdExists(updateObject.getObjectsIdsRequest().getBoardId(), boardRepository);
        Section section = board.getSectionFromBoard(updateObject.getObjectsIdsRequest().getSectionId());

        Common.fieldIsPrimitiveOrKnownObjectHelper(updateObject, section);

        boardRepository.save(board);
        return board.getSectionFromBoard(updateObject.getObjectsIdsRequest().getSectionId());
    }

    //TODO documentation
    public List<Section> getAllSectionsInBoard(long boardId) {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        return new ArrayList<>(board.getSections());
    }

    //TODO Documentation
    public Set<Section> getQuery(Map<String, List<String>> filters, long boardId) {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        EntityManager em = entityManagerFactory.createEntityManager();
        Set<Section> sections = board.getSections();

        // Use an iterator to remove sections with no items that match the filters
        Iterator<Section> iterator = sections.iterator();
        while (iterator.hasNext()) {
            Section section = iterator.next();

            // Set the filter for the section id
            filters.put(sectionId, List.of(section.getId().toString()));

            // Create a query using the filters
            Query query = em.createQuery(Common.generateQuery(filters));

            // Get the items for the section
            Set<Item> items = new HashSet<>();
            for (Object o : query.getResultList()) {
                items.add((Item) o);
            }

            // If the section has no items, remove it from the list
            if (items.size() == 0) {
                iterator.remove();
            } else {
                // Set the items for the section
                section.setItems(items);
            }
        }
        return sections;
    }
}
