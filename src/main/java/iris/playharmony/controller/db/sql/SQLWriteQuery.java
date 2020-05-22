package iris.playharmony.controller.db.sql;

import iris.playharmony.util.Pair;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

public abstract class SQLWriteQuery {

    private final Map<String, Integer> params;
    private final String sqlQuery;

    public SQLWriteQuery(String sqlQuery, String... params) {
        this.sqlQuery = sqlQuery;
        this.params = createParams(params);
    }

    public SQLStatement prepareStatement(Connection dbConnection) {
        try {
            return new SQLStatement(this, dbConnection.prepareStatement(getSQLQuery()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSQLQuery() {
        return sqlQuery;
    }

    public Map<String, Integer> getParams() {
        return Collections.unmodifiableMap(params);
    }

    public int getParamIndex(String name) {

        final String normalizedName = name.toUpperCase();

        if(!params.containsKey(normalizedName)) {
            throw new NoSuchElementException("There is no param called " + normalizedName
                    + ". This could be a spelling error, or maybe you wanted to set a key?");
        }

        return params.get(normalizedName);
    }

    private Map<String, Integer> createParams(String[] params) {
        return IntStream.range(0, params.length)
                .mapToObj(index -> new Pair<>(params[index].toUpperCase(), index + 1))
                .collect(toMap(Pair::getKey, Pair::getValue));
    }
}
