package CRM.repository;

import CRM.entity.Board;
import CRM.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {
    List<Type> findByBoard(Board board);
    boolean existsByBoardAndNameLike(@NonNull Board board, @NonNull String name);
}

