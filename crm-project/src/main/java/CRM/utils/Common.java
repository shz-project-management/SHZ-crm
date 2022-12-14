package CRM.utils;

import java.util.List;
import java.util.Map;

public class Common {

    public static String generateQuery(Map<String, List<String>> filters) {
        String query = "SELECT item FROM items WHERE ";

        for (Map.Entry<String, List<String>> entry : filters.entrySet()) {
            String column = entry.getKey();

            for (String value : entry.getValue()) {
                query += column + "='" + value + "' OR ";
            }
            query = query.substring(0, query.length() - 4);
            query += " AND ";
        }

        query = query.substring(0, query.length() - 5);
        return query;
    }
}