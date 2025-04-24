package controller;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.Optional;

import application.Main;
import data.CourseDAO;
import data.DBConnection;
import data.EnrollmentDAO;
import data.SessionManager;
import data.StudentDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Course;
import model.Enrollment;
import model.Student;

public class testController {
    @FXML private TextField studentId;
    @FXML private TextField courseCode;
    @FXML private ComboBox<String> studentComboBox;
    @FXML private ComboBox<String> courseComboBox;
    @FXML private TableView<Enrollment> enrollmentTable;
    @FXML private TableColumn<Enrollment, String> studentIdColumn;
    @FXML private TableColumn<Enrollment, String> courseCodeColumn;
    @FXML private TableColumn<Enrollment, LocalDate> dateColumn;
    
	@FXML private Text nickname;
	@FXML private Pane paneCenter;
	@FXML private BorderPane rootPane;
	
    private Connection connection = DBConnection.getInstance().getConnection();
    private EnrollmentDAO enrollmentDAO = new EnrollmentDAO(connection);
    private StudentDAO studentDAO = new StudentDAO(connection);
    private CourseDAO courseDAO = new CourseDAO(connection);
	private SessionManager sessionManager = SessionManager.getInstance();
	
    @FXML public void initialize() {
    	rootPane.setOnMouseClicked(event -> rootPane.requestFocus());
    	String Nickname = sessionManager.getUsername();
    	Nickname = Nickname.substring(0, 1).toUpperCase() + Nickname.substring(1).toLowerCase();

    	nickname.setText(Nickname);
    	
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        courseCodeColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("enrollmentDate"));

        loadComboBoxes();
        loadEnrollments();
        //studentComboBox.getSelectionModel().selectFirst();
        //courseComboBox.getSelectionModel().selectFirst();
    }
    
    @FXML private void handleComboBoxStudent() {
    	String response = studentComboBox.getSelectionModel().getSelectedItem();
    	if (response == null) return;
    	
    	for (Student student : studentDAO.fetch()) {
    		if (response.equals(student.getId())) {
    			studentId.setText(student.getName());
    		}
    	}
    }
    
    @FXML private void handleComboBoxCourse() {
    	String response = courseComboBox.getSelectionModel().getSelectedItem();
    	if (response == null) return;
    	
    	for (Course course : courseDAO.fetch()) {
    		if (response.equals(course.getCode())) {
    			courseCode.setText(course.getName());
    		}
    	}
    }
    
    @FXML void handleSave() {
    	String responseStudent = studentComboBox.getSelectionModel().getSelectedItem();
    	String responseCourse = courseComboBox.getSelectionModel().getSelectedItem();
    	
        if (responseStudent == null || responseCourse == null) {
        	this.AlertWindow(null, "All fields must be filled out.", AlertType.ERROR);
        	return;
        }
        
        if (!enrollmentDAO.authenticate(responseStudent, responseCourse)) {
        	this.AlertWindow(null, "The student ID is already in use.", AlertType.ERROR);
        	return;
        }
        
        enrollmentDAO.save(new Enrollment(responseStudent, responseCourse, LocalDate.now()));
    	

    	this.AlertWindow(null, 
    			"Student "+studentId.getText()+" has been enrolled in course "+courseCode.getText(),
    			AlertType.INFORMATION);
    	this.clearFields();
    	initialize();
    }
    
    private void clearFields() {
        studentId.clear();
        courseCode.clear();
    }
    
    @FXML void handleDelete(ActionEvent event) {
		Enrollment enrollment = enrollmentTable.getSelectionModel().getSelectedItem();
    	if (enrollment == null) {
			this.AlertWindow(null, "Select a piece of data first.", AlertType.ERROR);
			return;
    	}
		Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Do you want to delete this student?\n\n"+
        "- Student ID: "+enrollment.getStudentId()+"\n"+
        "- Course Code: "+enrollment.getCourseCode()+"\n"+
        "- Enrollment Date: "+enrollment.getEnrollmentDate());
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
    		enrollmentDAO.delete(enrollment.getStudentId(), enrollment.getCourseCode());
    		initialize();
    		this.AlertWindow(null, "Data Successfully Deleted", AlertType.INFORMATION);
        }
    }
    
    private void loadComboBoxes() {
        studentComboBox.getItems().setAll(
            studentDAO.fetch().stream().map(Student::getId).toList()
        );
        courseComboBox.getItems().setAll(
            courseDAO.fetch().stream().map(Course::getCode).toList()
        );
    }
    
    private void loadEnrollments() {
        enrollmentTable.getItems().setAll(enrollmentDAO.fetch());
    }
	@FXML void goToLogin() {
    	// CIERRA LA VENTANA ACTUAL
        Stage currentStage = (Stage) rootPane.getScene().getWindow();
        currentStage.close();
        // CIERRA LA VENTANA ACTUAL
        Main.loadView("/view/Login.fxml");
    }
	
    public void goToStudents() {
    	// CIERRA LA VENTANA ACTUAL
        Stage currentStage = (Stage) rootPane.getScene().getWindow();
        currentStage.close();
        // CIERRA LA VENTANA ACTUAL
        Main.loadView("/view/Students.fxml");
    }

    public void goToCourses() {
    	// CIERRA LA VENTANA ACTUAL
        Stage currentStage = (Stage) rootPane.getScene().getWindow();
        currentStage.close();
        // CIERRA LA VENTANA ACTUAL
        Main.loadView("/view/Courses.fxml");
    }

    public void goToEnrollment() {
    	// CIERRA LA VENTANA ACTUAL
        Stage currentStage = (Stage) rootPane.getScene().getWindow();
        currentStage.close();
        // CIERRA LA VENTANA ACTUAL
        Main.loadView("/view/Enrollments.fxml");
    }
    
    private void AlertWindow(String text, String content, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(null);
        alert.setHeaderText(text);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
