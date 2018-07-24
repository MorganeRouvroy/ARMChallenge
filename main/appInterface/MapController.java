package main.appInterface;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

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
        findBtn.setDefaultButton(true);
        // This beautiful line won't allow any other input than integers
        rangeField.textProperty().addListener(new IntegerOnlyTextListener(rangeField));
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

        // When adding a range to the field, you have to press the button in order to proceed
        setRangeField(event);
        // If there is not selected unit it will pop an alert
        comboAction(event);
    }

    /* Listener for the comboBox button. */
    @FXML
    protected void comboAction(ActionEvent event) {
        if(targetCombo.getValue().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a unit to search for.", ButtonType.CLOSE);
            alert.setTitle("No unit selected!");
            alert.showAndWait();
        }
        else
            System.out.println("Where's my map :(");
    }

    /* Listener for the comboBox button. */
    @FXML
    protected void setRangeField(ActionEvent event) {
        int number1 = parseInt(rangeField.getText(),-1);

        if (number1 < 0 || number1 > 50000) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to parse values in the range field.\n" +
                    "Please ensure the values are in the range.", ButtonType.CLOSE);
            alert.setTitle("Wrong input!");
            alert.showAndWait();
        }
        else if (number1 == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to parse values in the range field.\n" +
                    "Please ensure the values are not null.", ButtonType.CLOSE);
            alert.setTitle("Failed to parse values");
            alert.showAndWait();
        }
        else if (number1 == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to parse values in the range field.\n" +
                    "Please ensure the values are not null.", ButtonType.CLOSE);
            alert.setTitle("Failed to parse values");
            alert.showAndWait();
        }
        else
            System.out.println("map :(");


    }

    /**
     * Method that catches the null input or 0 input
     *
     * @param s the string converted to number
     * @param valueIfInvalid the returned value if the input is invalid
     * @return the numeric value for the input
     */
    public static int parseInt(final /*@Nullable*/ String s, final int valueIfInvalid) {
        try {
            if (s.equals("")) {
                return valueIfInvalid;
            } else {
                return Integer.parseInt(s);
            }
        } catch (final NumberFormatException ex) {
            return valueIfInvalid;
        }
    }
}
