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
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    /**
     * Create a new section in a board.
     *
     * @param sectionRequest the request object containing the board and section information to create
     * @return the set of sections in the board after the new section is added
     */
    public Set<Section> create(AttributeRequest sectionRequest, Long boardId) {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        Section section = Section.createSection(sectionRequest);
        board.addSectionToBoard(section);
        boardRepository.save(board);
        return board.getSections();
    }


    /**
     * Delete a section from a board.
     *
     * @param boardId   the ID of the board containing the section to delete
     * @param sectionId the ID of the section to delete
     */
    public void delete(long boardId, long sectionId) {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        board.removeSectionFromBoard(sectionId);
        boardRepository.save(board);
    }


    /**
     * Get a section from a board.
     *
     * @param sectionId the ID of the section to retrieve
     * @param boardId   the ID of the board containing the section
     * @return the section with the specified ID
     */
    public Section get(long sectionId, long boardId) {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        return board.getSectionFromBoard(sectionId);
    }

    /**
     * Update a section in a board.
     *
     * @param updateObject the request object containing the board and section IDs and the updated section information
     * @return the updated section
     * @throws NoSuchFieldException if the update request contains an invalid field
     */
    public Section update(UpdateObjectRequest updateObject) throws NoSuchFieldException {
        Board board = Validations.doesIdExists(updateObject.getObjectsIdsRequest().getBoardId(), boardRepository);
        Section section = board.getSectionFromBoard(updateObject.getObjectsIdsRequest().getSectionId());

        Common.fieldIsPrimitiveOrKnownObjectHelper(updateObject, section);

        boardRepository.save(board);
        return board.getSectionFromBoard(updateObject.getObjectsIdsRequest().getSectionId());
    }

    /**
     * Get a list of all sections in a board.
     *
     * @param boardId the ID of the board to retrieve the sections from
     * @return the list of sections in the board
     */
    public List<Section> getAllSectionsInBoard(long boardId) {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        return new ArrayList<>(board.getSections());
    }

    /**
     * Get a set of sections in a board that match a set of filters.
     *
     * @param filters the filters to apply to the sections
     * @param boardId the ID of the board to retrieve the sections from
     * @return the set of sections in the board that match the filters
     */
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
