package appInterface;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sqlUtils.Connector;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

public class MapApplication extends Application {

    @Override
    public void init() {

    }

    @Override
    public void start(Stage stage) throws Exception {
        // Stage -> Scene -> Parent

        new Connector();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/map_view.fxml"));

        URL url = new URL("https://i.imgur.com/XJRK3cG.png)");
        BufferedImage c= ImageIO.read(url);
        Image icon = SwingFXUtils.toFXImage(c, null);

        stage.getIcons().add(icon);
        stage.setTitle("BBA Innovation Challenge Application");
        stage.setScene(new Scene(root, 1000, 700));

        stage.show();
    }

    @Override
    public void stop(){
        Connector.closeCon();
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
