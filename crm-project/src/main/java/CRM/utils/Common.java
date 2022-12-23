package CRM.utils;

import CRM.entity.*;
import CRM.utils.enums.ExceptionMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Common {

    public static Board getBoard(long boardId, JpaRepository<Board, Long> repo) {
        return Validations.doesIdExists(boardId, repo);
    }

    public static Section getSection(Board board, long sectionId) {
        return getOptional(board.getSections(), sectionId, Section.class).orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.NO_SUCH_ID.toString()));
    }

    public static Item getItem(Section section, long searchId) {
        return getOptional(section.getItems(), searchId, Item.class).orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.NO_SUCH_ID.toString()));
    }

    public static SharedContent getComment(Item item, long searchId) {
        return getOptional(item.getComments(), searchId, Comment.class).orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.NO_SUCH_ID.toString()));
    }

    public static <T> Optional<T> getOptional(Set<T> list, long id, Class<T> cls) {
        try {
            Method getIdMethod = cls.getMethod("getId");
            return list.stream().filter(item -> invokeGetIdMethod(getIdMethod, item).equals(id)).findFirst();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(ExceptionMessage.UNPROCESSABLE_ENTITY.toString());
        }
    }

    public static Object invokeGetIdMethod(Method getIdMethod, Object obj) {
        try {
            return getIdMethod.invoke(obj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(ExceptionMessage.UNPROCESSABLE_ENTITY.toString());
        }
    }
}