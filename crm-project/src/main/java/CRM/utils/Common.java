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

    /**
     * Returns the section with the specified search ID, if it exists in the list of sections for the given board.
     *
     * @param board     the board whose sections should be searched
     * @param sectionId the sectionId of the desired section
     * @return the section with the specified search ID
     * @throws IllegalArgumentException if no such section exists in the list of sections for the given board
     */
    public static Section getSection(Board board, long sectionId) {
        return getOptional(board.getSections(), sectionId, Section.class).orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.NO_SUCH_ID.toString()));
    }

    /**
     * Returns the item with the specified search ID, if it exists in the list of items for the given section.
     *
     * @param section  the section whose items should be searched
     * @param searchId the search ID of the desired item
     * @return the item with the specified search ID
     * @throws IllegalArgumentException if no such item exists in the list of items for the given section
     */
    public static Item getItem(Section section, long searchId) {
        return getOptional(section.getItems(), searchId, Item.class).orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.NO_SUCH_ID.toString()));
    }

    /**
     * Returns the comment with the specified search ID, if it exists in the list of comments for the given item.
     *
     * @param item     the item whose comments should be searched
     * @param searchId the search ID of the desired comment
     * @return the comment with the specified search ID
     * @throws IllegalArgumentException if no such comment exists in the list of comments for the given item
     */
    public static SharedContent getComment(Item item, long searchId) {
        return getOptional(item.getComments(), searchId, Comment.class).orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.NO_SUCH_ID.toString()));
    }

    /**
     * Gets an object from a set by ID.
     *
     * @param list the set of objects to search
     * @param id   the ID of the object to search for
     * @param cls  the class of the objects in the set
     * @return an Optional containing the object if it is found, or an empty Optional if it is not found
     * @throws RuntimeException if the getId method of the class is not found
     */
    public static <T> Optional<T> getOptional(Set<T> list, long id, Class<T> cls) {
        try {
            Method getIdMethod = cls.getMethod("getId");
            return list.stream().filter(item -> invokeGetIdMethod(getIdMethod, item).equals(id)).findFirst();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(ExceptionMessage.UNPROCESSABLE_ENTITY.toString());
        }
    }

    /**
     * Invokes a getId method on an object.
     *
     * @param getIdMethod the getId method to invoke
     * @param obj         the object to invoke the method on
     * @return the result of invoking the method
     * @throws RuntimeException if the method cannot be accessed or an exception is thrown during the invocation
     */
    public static Object invokeGetIdMethod(Method getIdMethod, Object obj) {
        try {
            return getIdMethod.invoke(obj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(ExceptionMessage.UNPROCESSABLE_ENTITY.toString());
        }
    }

    /**
     * Calculates the distance between two words using the Levenshtein distance algorithm.
     *
     * @param givenWord      the first word
     * @param wordInDatabase the second word
     * @return the distance between the two words
     */
    public static int getDistanceBetweenWords(String givenWord, String wordInDatabase) {
        // uses levenshtein distance algorithm
        String word1 = givenWord.toLowerCase();
        String word2 = wordInDatabase.toLowerCase();

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

                    int min = Math.min(replace, insert);
                    min = Math.min(delete, min);
                    dp[i + 1][j + 1] = min;
                }
            }
        }

        return dp[len1][len2];
    }

    /**
     * Creates default notification settings for a new user in a board.
     *
     * @param user          the user to create the settings for
     * @param board         the board the user is being added to
     * @param settingsRepo  the repository for accessing NotificationSetting objects
     * @param entityManager the entity manager for managing the UserSetting objects
     */
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

    /**
     * Returns the Class object of the specified field.
     *
     * @param fieldName the field to get the Class object for
     * @return the Class object for the field
     */
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
     * @param obj          the object being updated
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

    /**
     * Generates a query for retrieving items from a database.
     *
     * @param filters a map where the keys are the names of the columns to filter on and the values
     *                are lists of the values to filter for
     * @return a string representation of the generated query
     * @throws IllegalArgumentException if the filters map is null
     */
    public static String generateQuery(Map<String, List<String>> filters) {
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