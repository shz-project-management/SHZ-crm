package CRM.repository;

import CRM.entity.Board;
import CRM.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    List<Status> findByBoard(Board board);
    /**
     * This function checks if a Status with the name and the same board exists in the database.
     * @param board The board object that the status belongs to.
     * @param name the Status name we are trying to check if exists
     * @return true if exists as said, otherwise false.
     */
    boolean existsByBoardAndNameLike(@NonNull Board board, @NonNull String name);
}

