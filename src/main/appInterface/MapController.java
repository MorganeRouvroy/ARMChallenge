package src.main.appInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Window;

public class MapController {
    @FXML
    private ComboBox<String> targetCombo;
    @FXML
    private TextField rangeField;
    @FXML
    private Button findBtn;

    /* Listener for the search button. */
    @FXML
    protected void searchAction(ActionEvent event) {
        Window owner = findBtn.getScene().getWindow();
        if (targetCombo.getItems().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Search error!",
                    "Please select a unit to search for");
            return;
        }
        System.out.println("Where's my map :(");
    }

    /* Listener for the comboBox button. */
    @FXML
    protected void comboAction(ActionEvent event) {
        System.out.println("Where's my map :(");
    }

    /* Listener for the comboBox button. */
    @FXML
    protected void setRangeField(ActionEvent event) {
        Window owner = rangeField.getScene().getWindow();

        // This beautiful line won't allow any other input than integers
        rangeField.textProperty().addListener(new IntegerOnlyTextListener(rangeField));

        int number = Integer.parseInt(rangeField.getText());

        if ( number < 0 || number > 50000 )
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Wrong input!",
                    "Please select a number for the range");
        else
            System.out.println("Where's my map :(");
    }
}
