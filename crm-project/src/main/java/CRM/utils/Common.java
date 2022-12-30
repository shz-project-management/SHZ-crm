package CRM.utils;

import CRM.entity.*;
import CRM.entity.requests.UpdateObjectRequest;
import CRM.utils.enums.ExceptionMessage;
import CRM.utils.enums.UpdateField;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
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


//    // 1-1,2-1,3-1 -> 0
//    0/3 = 0
//    1/3 = 0
//    2/3 = 1
//
//    // 4,5,6 -> 1
//    3/3 =1
//    4/3 = 1
//    5/3 = 1
//
//    // 7,8,9 -> 2
//    6/3 = 2

//    public static int getDistanceBetweenWords(String firstWord, String secondWord) {
//        // uses levenshtein distance algorithm
//        String newWord = firstWord.toLowerCase();
//        String dbBoard = secondWord.toLowerCase();
//
//        int len1 = newWord.length();
//        int len2 = dbBoard.length();
//        int threshHold = len1/3;
//
//        // len1+1, len2+1, because finally return dp[len1][len2]
//        int[][] dp = new int[len1 + 1][len2 + 1];
//
//        for (int i = 0; i <= len1; i++) {
//            dp[i][0] = i;
//        }
//
//        for (int j = 0; j <= len2; j++) {
//            dp[0][j] = j;
//        }
//
//        //iterate though, and check last char
//        for (int i = 0; i < len1; i++) {
//            char c1 = word1.charAt(i);
//            for (int j = 0; j < len2; j++) {
//                char c2 = word2.charAt(j);
//
//                //if last two chars equal
//                if (c1 == c2) {
//                    //update dp value for +1 length
//                    dp[i + 1][j + 1] = dp[i][j];
//                } else {
//                    int replace = dp[i][j] + 1;
//                    int insert = dp[i][j + 1] + 1;
//                    int delete = dp[i + 1][j] + 1;
//
//                    int min = replace > insert ? insert : replace;
//                    min = delete > min ? min : delete;
//                    dp[i + 1][j + 1] = min;
//                }
//            }
//        }
//
//        return dp[len1][len2];
//    }


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

    public static Class getObjectOfField(UpdateField fieldName) {
        switch (fieldName) {
            case STATUS:
                return Status.class;
            case TYPE:
                return Type.class;
            case PARENT_ITEM:
                return Item.class;
            case SECTION:
                return Section.class;
            default:
                return null;
        }
    }

    /**
     * Helper function for updating a primitive or known object field.
     *
     * @param updateObject the request object containing the updates to be made
     * @param obj         the object being updated
     * @throws NoSuchFieldException if the field does not exist in the item object
     */
    public static void fieldIsPrimitiveOrKnownObjectHelper(UpdateObjectRequest updateObject, Object obj) throws NoSuchFieldException {
        if (Validations.checkIfFieldIsNonPrimitive(updateObject.getFieldName())) {
            LocalDateTime dueDate = LocalDateTime.now().plusDays(Long.valueOf((Integer) updateObject.getContent()));
            Validations.setContentToFieldIfFieldExists(obj, updateObject.getFieldName(), dueDate);
        } else {
            Validations.setContentToFieldIfFieldExists(obj, updateObject.getFieldName(), updateObject.getContent());
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
//                throw new IllegalArgumentException("column and values cannot be null or empty");
                continue;
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