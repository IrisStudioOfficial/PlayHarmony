package iris.playharmony.controller.db.sql;

public class SQLDeleteByKeyQuery extends SQLWriteQuery {

    public SQLDeleteByKeyQuery(String tableName, String key) {
        super(String.format("DELETE FROM %s WHERE %s = ?", tableName, key), key + "_KEY");
    }


}
