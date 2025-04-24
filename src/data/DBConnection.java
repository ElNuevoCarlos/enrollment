package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance; //Singleton
    private Connection connection;
	private final String username="AJEREZ";
	private final String password="AJEREZ"; // 4J3R3Z
	//private final String host = "192.168.254.215";
	private final String port = "1521";
	private final String host = "localhost";
	private final String service = "xe";
	//private final String service = "orcl";

    private DBConnection() {
        try {
            connection = DriverManager.getConnection(getConnectionString(), username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error connecting to the database.");
        }
    }

    public static DBConnection getInstance() {
        if (instance == null) instance = new DBConnection();
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
	public String getConnectionString() {
		return String.format("jdbc:oracle:thin:@%s:%s:%s", this.host, this.port, this.service);
	}
}
