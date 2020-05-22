package iris.playharmony.controller.db.sql;

import java.util.Arrays;

public class SQLInsertQuery extends SQLWriteQuery {

    public SQLInsertQuery(String tableName, String... params) {
        super(String.format("INSERT INTO %s (%s) VALUES (%s)",
                tableName, asSQLParams(params), getSQLInputValues(params.length)), params);
    }

    private static Object asSQLParams(String[] params) {

        if(params.length == 0) {
            throw new IllegalArgumentException();
        }

        StringBuilder result = new StringBuilder(Arrays.toString(params));

        return result.deleteCharAt(result.length() - 1).deleteCharAt(0).toString();
    }

    private static String getSQLInputValues(int count) {

        if(count <= 0) {
            throw new IllegalArgumentException();
        }

        StringBuilder builder = new StringBuilder();

        for(int i = 0;i < count - 1;i++) {
            builder.append('?').append(',');
        }

        builder.append('?');

        return builder.toString();
    }
}
