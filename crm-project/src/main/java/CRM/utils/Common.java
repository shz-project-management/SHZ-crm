package CRM.utils;

import CRM.entity.*;
import CRM.entity.response.Response;
import CRM.utils.enums.ExceptionMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;

public class Common {


    public static User getUser(long userId, JpaRepository<User, Long> repo){
        return Validations.doesIdExists(userId, repo);
    }

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

    public static <T> Response buildSuccessResponse(T data, HttpStatus successStatus, String message) {
        return new Response.Builder()
                .message(message)
                .data(data)
                .status(successStatus)
                .statusCode(successStatus.value())
                .build();
    }

    public static Response buildErrorResponse(Exception e, HttpStatus errorStatus) {
        return new Response.Builder()
                .message(e.getMessage())
                .status(errorStatus)
                .statusCode(errorStatus.value())
                .build();
    }


    public static int getDistanceBetweenWords(String firstWord, String secondWord) {
        // uses levenshtein distance algorithm
        String word1 = firstWord.toLowerCase();
        String word2 = secondWord.toLowerCase();

        int len1 = word1.length();
        int len2 = word2.length();

        // len1+1, len2+1, because finally return dp[len1][len2]
        int[][] dp = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }

        //iterate though, and check last char
        for (int i = 0; i < len1; i++) {
            char c1 = word1.charAt(i);
            for (int j = 0; j < len2; j++) {
                char c2 = word2.charAt(j);

                //if last two chars equal
                if (c1 == c2) {
                    //update dp value for +1 length
                    dp[i + 1][j + 1] = dp[i][j];
                } else {
                    int replace = dp[i][j] + 1;
                    int insert = dp[i][j + 1] + 1;
                    int delete = dp[i + 1][j] + 1;

                    int min = replace > insert ? insert : replace;
                    min = delete > min ? min : delete;
                    dp[i + 1][j + 1] = min;
                }
            }
        }

        return dp[len1][len2];
    }

}