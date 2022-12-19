package CRM.repository;

import CRM.entity.Comment;
import CRM.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Transactional
    @Modifying
    void deleteAllByParentItem(Item item);
}
