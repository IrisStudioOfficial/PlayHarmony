package iris.playharmony.controller.db;

import iris.playharmony.controller.handler.PathHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class AbstractDatabaseController {

    private final Connection dbConnection;

    public AbstractDatabaseController() {
        this.dbConnection = openMySQLConnection();
    }

    protected Connection getDBConnection() {
        return dbConnection;
    }

    private Connection openMySQLConnection() {

        Connection connection = null;

        try {

            ClassLoader.getSystemClassLoader().loadClass("org.sqlite.JDBC");

            String url = "jdbc:sqlite:" + PathHandler.DATABASE_PATH;

            connection = DriverManager.getConnection(url);

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        return connection;
    }
}
