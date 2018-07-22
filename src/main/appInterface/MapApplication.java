package src.main.appInterface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MapApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("map_view.fxml"));
        primaryStage.setTitle("BBA Innovation Challenge Application");
        primaryStage.setScene(new Scene(root, 800, 500));

        primaryStage.show();
    }
}
