package application;

import java.sql.SQLException;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
    private static BorderPane rootLayout;
	@Override
    public void start(Stage primaryStage) throws SQLException {
        try {
            FXMLLoader root = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            rootLayout = root.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

    		Image icon = new Image(getClass().getResourceAsStream("/resources/img/icon.png"));
    		primaryStage.getIcons().add(icon);
            
            // PANTALLA COMPLETA
            //primaryStage.setFullScreen(true);
            
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public static void loadView(String fxmlFile) {
		Stage secondStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlFile));
            rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            secondStage.setScene(scene);
    		Image icon = new Image(Main.class.getResourceAsStream("/resources/img/icon.png"));
    		secondStage.getIcons().add(icon);
            secondStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}

