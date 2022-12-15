package CRM.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Common {

    public static String generateQuery(Map<String, List<String>> filters) {
        // Use a StringBuilder to build the query more efficiently
        StringBuilder queryBuilder = new StringBuilder("SELECT item FROM items WHERE ");

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

            // Check for null or empty values in the filter
            if (column == null || values == null || values.isEmpty()) {
                throw new IllegalArgumentException("column and values cannot be null or empty");
            }

            // Use parameterized queries to avoid SQL injection attacks
            String condition = column + " IN (";
            for (String value : values) {
                condition += value + ", ";
            }
            condition = condition.substring(0, condition.length() - 2);
            condition += ")";

            conditions.add(condition);
        }

        // Join the different parts of the query using AND
        String query = String.join(" AND ", conditions);

        // Append the completed query to the StringBuilder and return it as a string
        return queryBuilder.append(query).toString();
    }
}