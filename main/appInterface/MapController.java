package main.appInterface;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Window;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;

public class MapController implements Initializable, MapComponentInitializedListener {
    @FXML
    private ComboBox<String> targetCombo;
    @FXML
    private TextField rangeField;
    @FXML
    private Button findBtn;
    @FXML
    private GoogleMapView mapView;

    private GoogleMap map;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mapView.addMapInializedListener(this);
    }

    @Override
    public void mapInitialized() {
        //Set the initial properties of the map.
        MapOptions mapOptions = new MapOptions();
        mapOptions.center(new LatLong(47.6097, -122.3331))
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .zoom(12);

        map = mapView.createMap(mapOptions);
    }

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
