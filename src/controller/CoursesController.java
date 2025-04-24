package controller;

import java.sql.Connection;
import java.util.Optional;

import application.Main;
import data.CourseDAO;
import data.DBConnection;
import data.SessionManager;
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
import model.Course;

public class CoursesController {
    @FXML private TextField codeField;
    @FXML private TextField nameField;
    @FXML private TextField creditsField;
    @FXML private TableView<Course> courseTable;
    @FXML private TableColumn<Course, String> codeColumn;
    @FXML private TableColumn<Course, String> titleColumn;
    @FXML private TableColumn<Course, Integer> creditsColumn;
    
	@FXML private Text nickname;
	@FXML private Pane paneCenter;
	@FXML private BorderPane rootPane;
	
    private Connection connection = DBConnection.getInstance().getConnection();
    private CourseDAO courseDAO = new CourseDAO(connection);
	private SessionManager sessionManager = SessionManager.getInstance();
	
    @FXML public void initialize() {
    	rootPane.setOnMouseClicked(event -> rootPane.requestFocus());
    	String Nickname = sessionManager.getUsername();
    	Nickname = Nickname.substring(0, 1).toUpperCase() + Nickname.substring(1).toLowerCase();

    	nickname.setText(Nickname);
    	
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        creditsColumn.setCellValueFactory(new PropertyValueFactory<>("credits"));
        loadCourses();
    }
    
    public void loadCourses() {
        courseTable.getItems().setAll(courseDAO.fetch());
    }
    
    @FXML public void handleSaveCourse() {
    	String code = codeField.getText().trim().toUpperCase();
    	String name = nameField.getText().trim();
    	String credits = creditsField.getText().trim();
    	
        if (code.isBlank() || name.isBlank() || credits.isBlank()) {
        	this.AlertWindow(null, "All fields must be filled out.", AlertType.ERROR);
        	return;
        }
        if (!code.matches("[A-Za-z]{3}[0-9]{3}")) {
        	this.AlertWindow(null, "The code must meet the following conditions\n"
        			+"- The code must have 3 letters and 3 digits\n"
        			+"- First you must place the letters and then the digits", AlertType.ERROR);
        	return;
        }
        if (!courseDAO.authenticate(code)) {
        	this.AlertWindow(null, "The course code is already in use.", AlertType.ERROR);
        	return;
        }
        int numCredits;
        try {
        	numCredits = Integer.parseInt(credits);
        } catch (NumberFormatException e) {
        	this.AlertWindow(null, "The credits field must have a number.", AlertType.ERROR);
        	return;
        }
        
        courseDAO.save(new Course(code, name, numCredits));
    	
    	this.clearFields();
    	initialize();
    	this.AlertWindow(null, 
    			"The course\n"+
    			"- Code: "+code+"\n"+
    			"- Name: "+name+"\n"+
    			"- Credits: "+credits+"\n\n"+
    			"It has been created successfully.",
    			AlertType.INFORMATION);
    	
    }
    @FXML public void handleUpdateCourse() {
    	Course course = courseTable.getSelectionModel().getSelectedItem();
    	if (course == null) {
			this.AlertWindow(null, "Select a piece of data first.", AlertType.ERROR);
			return;
    	}
    	
        Dialog<Course> dialog = new Dialog<>();
        
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/img/icon.png")));
        
        GridPane grid = new GridPane();

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));

        TextField nameField = new TextField(course.getName());
        TextField creditsField = new TextField(String.valueOf(course.getCredits()));

        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Credits:"), 0, 2);
        grid.add(creditsField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        
        ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String name = nameField.getText().trim();
                int credits;
                try {
                    credits = Integer.parseInt(creditsField.getText().trim());
                } catch (NumberFormatException e) {
                    this.AlertWindow(null, "The credits field must be a number.", AlertType.ERROR);
                    return null;
                }
                return new Course(course.getCode(), name, credits);
            }
            return null;
        });
        
        Optional<Course> result = dialog.showAndWait();
        result.ifPresent(updatedCourse -> {
            courseDAO.update(updatedCourse);
            loadCourses(); 
            this.AlertWindow(null, "Course successfully updated.", AlertType.INFORMATION);
        });

    }
    @FXML public void handleDeleteCourse() {
		Course course = courseTable.getSelectionModel().getSelectedItem();
    	if (course == null) {
			this.AlertWindow(null, "Select a piece of data first.", AlertType.ERROR);
			return;
    	}
		Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Do you want to delete this course?\n\n"+
        "- Code: "+course.getCode()+"\n"+
        "- Name: "+course.getName()+"\n"+
        "- Credits: "+course.getCredits());
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
    		courseDAO.delete(course.getCode());
    		initialize();
    		this.AlertWindow(null, "Data Successfully Deleted", AlertType.INFORMATION);
        }
    }
    
    private void clearFields() {
        codeField.clear();
        nameField.clear();
        creditsField.clear();
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
