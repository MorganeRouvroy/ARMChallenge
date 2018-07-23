package appInterface;

import javafx.scene.control.Alert;
import javafx.stage.Window;

/**
 * AlertHelper class pops up a new window containing alert messages for special cases.
 * */

public class AlertHelper {

    public static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
}