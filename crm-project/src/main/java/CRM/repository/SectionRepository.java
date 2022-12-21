package CRM.repository;

import CRM.entity.Board;
import CRM.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    boolean existsByBoardAndNameLike(@NonNull Board board, @NonNull String name);

    List<Section> findByBoard(Board board);

}
