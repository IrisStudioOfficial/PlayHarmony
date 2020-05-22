package iris.playharmony.controller.db.sql;

import java.util.Arrays;

public class SQLUpdateQuery extends SQLWriteQuery {

    public SQLUpdateQuery(String tableName, String key, String... params) {
        super(String.format("UPDATE %s SET %s WHERE %s", tableName, buildQueryParams(params), key), append(key, params));
    }

    private static String[] append(String key, String[] params) {
        String[] newParams = Arrays.copyOf(params, params.length + 1);
        newParams[params.length] = key + "_KEY";
        return newParams;
    }

    private static String buildQueryParams(String[] params) {

        StringBuilder builder = new StringBuilder();

        for (String param : params) {
            builder.append(param).append(" = ?,");
        }

        return builder.deleteCharAt(builder.length()-1).toString();
    }
}
