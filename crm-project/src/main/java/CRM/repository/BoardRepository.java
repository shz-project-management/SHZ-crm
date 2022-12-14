package CRM.repository;

import CRM.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
//    @Transactional
//    @Modifying
//    @Query("DELETE Log l WHERE l.document = ?1")
//    int deleteByDocument(Document document);
//
//    @Query("?1")
//    List<Document> getAll(String string);
}