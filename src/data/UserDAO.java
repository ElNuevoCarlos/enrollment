package data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.User;

public class UserDAO {
	
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

	public ArrayList<User> fetch() {
        ArrayList<User> users = new ArrayList<>();
        String query = "SELECT * FROM USERADMIN";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                String username = rs.getString("NICKNAME");
                String password = rs.getString("PASSWORD");
                
                User user = new User(username, password);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
	}

	public boolean authenticate(String username, String password) {
		for (User user : fetch()) {
			if (username.equals(user.getUsername())) {
				if (password.equals(user.getPassword())) {
					return true;
				}
			}
		}
		return false;
	}

}
