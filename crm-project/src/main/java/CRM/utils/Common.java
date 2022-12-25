package CRM.utils;

import CRM.entity.*;
import CRM.utils.enums.ExceptionMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Common {

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


    public static void createDefaultSettingForNewUserInBoard(User user, Board board, JpaRepository<NotificationSetting, Long> settingsRepo, EntityManager entityManager) {
        try {
            List<NotificationSetting> notificationSettingList = settingsRepo.findAll();
            for (NotificationSetting notificationSetting : notificationSettingList) {
                UserSetting userSetting = UserSetting.createUserSetting(user, notificationSetting);
                userSetting = entityManager.merge(userSetting);
                board.addUserSettingToBoard(userSetting);
            }
        } finally {
            entityManager.close();
        }
    }

    public static String generateQuery(Map<String,List<String>> filters) {
        StringBuilder queryBuilder = new StringBuilder("SELECT i FROM Item i WHERE ");

        // Use a List to store the different parts of the query
        List<String> conditions = new ArrayList<>();

        // Check for null values in the filters map
        if (filters == null) {
            throw new IllegalArgumentException("filters cannot be null");
        }

        // Iterate over the filters
        for (Map.Entry<String, List<String>> entry : filters.entrySet()) {
            String column = entry.getKey();
            List<String> values = entry.getValue();

            if (column == null || values == null || values.isEmpty()) {
                throw new IllegalArgumentException("column and values cannot be null or empty");
            }

            // Use parameterized queries to avoid SQL injection attacks
            StringBuilder condition = new StringBuilder("i." + column + " IN (");
            for (String value : values) {
                condition.append(value).append(", ");
            }
            condition = new StringBuilder(condition.substring(0, condition.length() - 2));
            condition.append(")");

            conditions.add(condition.toString());
        }

        // Join the different parts of the query using AND
        String query = String.join(" AND ", conditions);

        // Append the completed query to the StringBuilder and return it as a string
        return queryBuilder.append(query).toString();
    }
}