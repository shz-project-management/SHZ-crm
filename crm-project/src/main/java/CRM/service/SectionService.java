package CRM.service;

import CRM.entity.Attribute;
import CRM.entity.Board;
import CRM.entity.Section;
import CRM.entity.Type;
import CRM.repository.BoardRepository;
import CRM.repository.SectionRepository;
import CRM.repository.TypeRepository;
import CRM.utils.Validations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SectionService implements AttributeService{

    private static Logger logger = LogManager.getLogger(SectionService.class.getName());

    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private BoardRepository boardRepository;

    @Override
    public int update(long id, Attribute object) {
        return 0;
    }

    /**
     * This function persists a new Type to the database by calling the save function in the TypeRepository class.
     * @param section The section object to be persisted.
     * @return The persisted type object.
     */
    @Override
    public Section create(Attribute section) {
        if(sectionRepository.existsByBoardAndNameLike(section.getBoard(), section.getName()))
            Validations.throwAttributeAlreadyExistsForBoard(section, "Section");
        return sectionRepository.save(Section.createSection(section));
    }

    /**
     * Deletes the given type from the repository.
     * @param sectionId the type ID to delete
     */
    @Override
    public boolean delete(Long sectionId) {
        Section section = Validations.doesIdExists(sectionId, sectionRepository);
        sectionRepository.delete(section);
        return true;
    }

    /**
     * This method is used to retrieve a type with the specified id.
     * @param id The id of the type to be retrieved.
     * @return The retrieved type.
     * @throws NoSuchElementException   if the type with the specified id is not found.
     * @throws IllegalArgumentException if the specified id is invalid.
     * @throws NullPointerException     if the specified id is null.
     */
    @Override
    public Type get(Long id) {
        return Validations.doesIdExists(id, sectionRepository);
    }

    /**
     * This method is used to retrieve all the types.
     *
     * @return A list containing all the types.
     */
    @Override
    public List<Section> getAll() {
        return sectionRepository.findAll();
    }

    /**
     * This method is used to retrieve all the types that belong to the board with the specified id.
     *
     * @param boardId The id of the user whose types are to be retrieved.
     * @return A list containing all the types that belong to the board with the specified id.
     * @throws NoSuchElementException   if the board with the specified id is not found.
     * @throws IllegalArgumentException if the specified board id is invalid.
     * @throws NullPointerException     if the specified board id is null.
     */
    @Override
    public List<Section> getAllInBoard(Long boardId) {
        Board board = Validations.doesIdExists(boardId, boardRepository);
        return sectionRepository.findByBoard(board);
    }
}
