package iris.playharmony.controller.db.sql;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLStatement implements AutoCloseable {

    public static final int ERROR_CODE = -1;


    private final SQLWriteQuery query;
    private final PreparedStatement preparedStatement;
    private final Map<String, Boolean> paramsSetTracker;

    public SQLStatement(SQLWriteQuery query, PreparedStatement preparedStatement) {
        this.query = query;
        this.preparedStatement = preparedStatement;
        paramsSetTracker = createParamsSetTracker();
    }

    public int execute() {
        try {
            checkThatAllParametersHaveBeenSet();
            return preparedStatement.executeUpdate();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return ERROR_CODE;
    }

    public SQLStatement setKey(String name, String value) {
        final String keyName = (name + "_KEY").toUpperCase();
        setStatementParameter(name, value);
        paramsSetTracker.put(keyName, true);
        return this;
    }

    public SQLStatement set(String name, String value) {
        setStatementParameter(name, value);
        paramsSetTracker.put(name.toUpperCase(), true);
        return this;
    }

    public SQLStatement set(String name, InputStream inputStream) {
        setStatementParameter(name, inputStream);
        paramsSetTracker.put(name.toUpperCase(), true);
        return this;
    }

    public SQLStatement set(String name, InputStream inputStream, int bytes) {
        setStatementParameter(name, inputStream, bytes);
        paramsSetTracker.put(name.toUpperCase(), true);
        return this;
    }

    public SQLWriteQuery getQuery() {
        return query;
    }

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    @Override
    public void close() throws Exception {
        preparedStatement.close();
    }

    private boolean notAllParamsAreSet() {
        return paramsSetTracker.values().stream().anyMatch(isSet -> !isSet);
    }

    private Map<String, Boolean> createParamsSetTracker() {
        Map<String, Boolean> tracker = new HashMap<>();
        query.getParams().forEach((param, index) -> tracker.put(param, false));
        return tracker;
    }

    private void checkThatAllParametersHaveBeenSet() {
        for(Map.Entry<String, Boolean> param : paramsSetTracker.entrySet()) {
            if(!param.getValue()) {
                throw new RuntimeException("Param " + param.getKey() + " has not been set on query " + getQuery().getSQLQuery());
            }
        }
    }

    private void setStatementParameter(String name, String value) {
        try {
            preparedStatement.setString(query.getParamIndex(name), value);
        } catch (SQLException e) {
            Logger.getGlobal().log(Level.SEVERE, "Error while setting parameter " + name, e);
        }
    }

    private void setStatementParameter(String name, InputStream inputStream) {
        try {
            preparedStatement.setBinaryStream(query.getParamIndex(name), inputStream);
        } catch(SQLException e) {
            Logger.getGlobal().log(Level.SEVERE, "Error while setting parameter " + name, e);
        }
    }

    private void setStatementParameter(String name, InputStream inputStream, int bytes) {
        try {
            preparedStatement.setBinaryStream(query.getParamIndex(name), inputStream, bytes);
        } catch(SQLException e) {
            Logger.getGlobal().log(Level.SEVERE, "Error while setting parameter " + name, e);
        }
    }
}
