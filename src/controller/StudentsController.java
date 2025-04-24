package controller;

import java.sql.Connection;
import java.util.Optional;

import application.Main;
import data.DBConnection;
import data.SessionManager;
import data.StudentDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Student;

public class StudentsController {
    @FXML private TableColumn<Student, String> emailColumn;
    @FXML private TextField emailField;
    @FXML private TableColumn<Student, String> idCol;
    @FXML private TextField idField;
    @FXML private TableColumn<Student, String> nameColumn;
    @FXML private TextField nameField;
    @FXML private TableView<Student> studentTable;
    
    
	@FXML private Text nickname;
	@FXML private Pane paneCenter;
	@FXML private BorderPane rootPane;
	
    private Connection connection = DBConnection.getInstance().getConnection();
    private StudentDAO studentDAO = new StudentDAO(connection);
	private SessionManager sessionManager = SessionManager.getInstance();
	
    @FXML public void initialize() {
    	rootPane.setOnMouseClicked(event -> rootPane.requestFocus());
    	String Nickname = sessionManager.getUsername();
    	Nickname = Nickname.substring(0, 1).toUpperCase() + Nickname.substring(1).toLowerCase();

    	nickname.setText(Nickname);
    	
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        loadStudents();
    }
    
    public void loadStudents() {
        studentTable.getItems().setAll(studentDAO.fetch());
    }
    
    @FXML void handleDeleteStudent(ActionEvent event) {
		Student student = studentTable.getSelectionModel().getSelectedItem();
    	if (student == null) {
			this.AlertWindow(null, "Select a piece of data first.", AlertType.ERROR);
			return;
    	}
		Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Do you want to delete this student?\n\n"+
        "- ID: "+student.getId()+"\n"+
        "- Name: "+student.getName()+"\n"+
        "- Email: "+student.getEmail());
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
    		studentDAO.delete(student.getId());
    		initialize();
    		this.AlertWindow(null, "Data Successfully Deleted", AlertType.INFORMATION);
        }
    }

    @FXML void handleSaveStudent(ActionEvent event) {
       	String id = idField.getText().trim().toUpperCase();
    	String name = nameField.getText().trim();
    	String email = emailField.getText().trim();
    	
        if (id.isBlank() || name.isBlank() || email.isBlank()) {
        	this.AlertWindow(null, "All fields must be filled out.", AlertType.ERROR);
        	return;
        }
        
        if (!id.matches("[A-Za-z]{1}[0-9]{3}")) {
        	this.AlertWindow(null, "The id must meet the following conditions\n"
        			+"- The id must have 1 letters and 3 digits\n"
        			+"- First you must place the letters and then the digits", AlertType.ERROR);
        	return;
        }

        if (!studentDAO.authenticate(id)) {
        	this.AlertWindow(null, "The student ID is already in use.", AlertType.ERROR);
        	return;
        }
        
        studentDAO.save(new Student(id, name, email));
    	
    	this.clearFields();
    	initialize();
    	this.AlertWindow(null, 
    			"The student\n"+
    			"- ID: "+id+"\n"+
    			"- Name: "+name+"\n"+
    			"- Email: "+email+"\n\n"+
    			"It has been created successfully.",
    			AlertType.INFORMATION);
    }

    @FXML void handleUpdateStudent(ActionEvent event) {
    	Student student = studentTable.getSelectionModel().getSelectedItem();
    	if (student == null) {
			this.AlertWindow(null, "Select a piece of data first.", AlertType.ERROR);
			return;
    	}
    	
        Dialog<Student> dialog = new Dialog<>();
        
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/img/icon.png")));
        
        GridPane grid = new GridPane();
        
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));

        TextField nameField = new TextField(student.getName());
        TextField emailField = new TextField(student.getEmail());

        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        
        ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                return new Student(student.getId(), name, email);
            }
            return null;
        });
        
        Optional<Student> result = dialog.showAndWait();
        result.ifPresent(updatedCourse -> {
            studentDAO.update(updatedCourse);
            loadStudents(); 
            this.AlertWindow(null, "Course successfully updated.", AlertType.INFORMATION);
        });
    }
    
    private void clearFields() {
        idField.clear();
        nameField.clear();
        emailField.clear();
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
