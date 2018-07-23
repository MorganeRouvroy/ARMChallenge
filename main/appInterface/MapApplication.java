package main.appInterface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MapApplication extends Application {

    @Override
    public void init() {

    }

    @Override
    public void start(Stage stage) throws Exception {
        // Stage -> Scene -> Parent

//        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("map_view.fxml"));
//        loader.setControllerFactory( c -> new MapController() );
//        Parent root = loader.load();

        Parent root = FXMLLoader.load(getClass().getResource("map_view.fxml"));

        stage.setTitle("BBA Innovation Challenge Application");
        stage.setScene(new Scene(root, 800, 500));

        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
