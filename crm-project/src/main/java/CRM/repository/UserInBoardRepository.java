package CRM.repository;

import CRM.entity.Board;
import CRM.entity.User;
import CRM.entity.UserInBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface UserInBoardRepository extends JpaRepository<UserInBoard, Long> {
//    @Query("SELECT u FROM UserDocument u WHERE (u.document = ?1 and u.user = ?2) ")
//    Optional<UserDocument> find(Document doc, User user);
//

    List<UserInBoard> findAllUserByBoard(Board board);

    List<UserInBoard> findAllBoardByUser(User user);
    @Transactional
    @Modifying
    @Query("DELETE UserInBoard uib WHERE uib.board = ?1")
    void deleteAllByBoard(Board board);

//
//
//    @Query("SELECT u FROM UserDocument u WHERE u.document=?1")
//    List<UserDocument> findAllUsersInDocument(Document document);
//
//    @Transactional
//    @Modifying(clearAutomatically = true)
//    @Query("UPDATE UserDocument urd SET urd.permission = ?1 WHERE (urd.document = ?2 and urd.user = ?3)")
//    int updatePermission(Permission permission, Document document, User user);
//
//    @Transactional
//    @Modifying
//    @Query("DELETE UserDocument urd WHERE urd.document = ?1")
//    int deleteDocument(Document document);
//
//
//
//    @Transactional
//    @Modifying
//    @Query("DELETE UserDocument urd WHERE urd.user = ?1 AND urd.document = ?2")
//    int deleteUserFromDocument(User user, Document document);
}
