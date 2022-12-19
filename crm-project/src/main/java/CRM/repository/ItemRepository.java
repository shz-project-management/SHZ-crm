package CRM.repository;

import CRM.entity.Board;
import CRM.entity.Item;
import CRM.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByBoard(Board board);

    Set<Item> findAllByStatus(Status status);
}
