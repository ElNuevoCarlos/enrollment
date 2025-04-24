package controller;

import java.sql.Connection;

import application.Main;
import data.DBConnection;
import data.SessionManager;
import data.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class LoginController {
    @FXML private BorderPane rootPane;
    @FXML private PasswordField password;
    @FXML private TextField passwordView;
    @FXML private TextField username;
    
    private Connection connection = DBConnection.getInstance().getConnection();
    private UserDAO userDao = new UserDAO(connection);
	private SessionManager sessionManager = SessionManager.getInstance();
    
    @FXML public void initialize() {
    	rootPane.setOnMouseClicked(event -> rootPane.requestFocus());
    }
    
    @FXML void handleViewPassword() {
    	if (passwordView.isVisible()) {
    		password.setVisible(true);
    		passwordView.setVisible(false);
    	} else {
    		password.setVisible(false);
    		passwordView.setDisable(true);
        	passwordView.setVisible(true);
        	passwordView.setPromptText(password.getText().trim());
    	}
    }
    
    @FXML void handleNext() {
    	String Username = username.getText().trim();
    	String Password = password.getText().trim();
    	
    	if (Username.isBlank() || Password.isBlank()) {
    		this.AlertWindow(null, "There are empty fields\n- You must fill in all fields.", AlertType.ERROR);
    		return;
    	}
    	
    	if (userDao.authenticate(Username, Password)) {
    		sessionManager.setUser(Username, Password);
        	// CIERRA LA VENTANA ACTUAL
            Stage currentStage = (Stage) rootPane.getScene().getWindow();
            currentStage.close();
            // CIERRA LA VENTANA ACTUAL
            Main.loadView("/view/Students.fxml");
    		//Main.loadView("/view/MainMenu.fxml");
    	} else {
    		this.AlertWindow(null, "Incorrect username or password.", AlertType.ERROR);
    	}
    }
 
    private void AlertWindow(String text, String content, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(null);
        alert.setHeaderText(text);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
