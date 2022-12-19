package CRM.repository;

import CRM.entity.Board;
import CRM.entity.Comment;
import CRM.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByParentItem(Item item);
}
