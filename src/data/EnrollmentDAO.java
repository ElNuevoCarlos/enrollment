package data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import model.Enrollment;

public class EnrollmentDAO {
    private Connection connection;

    public EnrollmentDAO(Connection connection) {
        this.connection = connection;
    }

	public void save(Enrollment enrollment) {
	    String query = "INSERT INTO ENROLLMENT (STUDENT_ID, COURSE_CODE, ENROLLMENT_DATE)"+
	 			   "VALUES (?, ?, ?)";
	     
			try (PreparedStatement stmt = connection.prepareStatement(query)) {
			    stmt.setString(1, enrollment.getStudentId());
			    stmt.setString(2, enrollment.getCourseCode());
			    stmt.setDate(3, Date.valueOf(enrollment.getEnrollmentDate()));
			    stmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}


	public ArrayList<Enrollment> fetch() {
        ArrayList<Enrollment> enrollments = new ArrayList<>();
        String query = "SELECT * FROM ENROLLMENT";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String studentId = rs.getString("STUDENT_ID");
                String courseCode = rs.getString("COURSE_CODE");
                LocalDate enrollmentDate = rs.getDate("ENROLLMENT_DATE").toLocalDate();
                Enrollment enrollment = new Enrollment(studentId, courseCode, enrollmentDate);
                enrollments.add(enrollment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enrollments;
	}


	public void update(Enrollment enrollment) {
		// TODO Auto-generated method stub
	}


	public void delete(String id, String code) {
		String sql = "DELETE FROM ENROLLMENT WHERE STUDENT_ID = ? AND COURSE_CODE = ?";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, id);
			stmt.setString(2, code);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean authenticate(String id, String code) {
		for (Enrollment enrollment : fetch()) {
			if (id.equals(enrollment.getStudentId())) {
				if (code.equals(enrollment.getCourseCode())) {
					return false;
				}
			}
		}
		return true;
	}

}
