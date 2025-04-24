package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.Student;

public class StudentDAO implements CRUD_Operation<Student,String>{
	
    private Connection connection;

    public StudentDAO(Connection connection) {
        this.connection = connection;
    }
	@Override
	public void save(Student student) {
	    String query = "INSERT INTO STUDENT (ID, NAME, EMAIL)"+
 			   "VALUES (?, ?, ?)";
     
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
		    stmt.setString(1, student.getId());
		    stmt.setString(2, student.getName());
		    stmt.setString(3, student.getEmail());
		    stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ArrayList<Student> fetch() {
        ArrayList<Student> students = new ArrayList<>();
        String query = "SELECT * FROM STUDENT";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                String id = rs.getString("ID");
                String name = rs.getString("NAME");
                String email = rs.getString("EMAIL");
                
                Student student = new Student(id, name, email);
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
	}

	@Override
	public void update(Student student) {
	    String sql = "UPDATE STUDENT SET NAME = ?, EMAIL = ? WHERE ID = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setString(1, student.getName());
	        pstmt.setString(2, student.getEmail());
	        pstmt.setString(3, student.getId());
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	@Override
	public void delete(String id) {
		String sql = "DELETE FROM STUDENT WHERE ID = ?";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean authenticate(String id) {
		for (Student student : fetch()) {
			if (id.equals(student.getId())) {
				return false;
			}
		}
		return true;
	}

}
