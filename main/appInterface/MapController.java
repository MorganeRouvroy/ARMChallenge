package main.appInterface;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.shapes.Circle;
import com.lynden.gmapsfx.shapes.CircleOptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.sqlUtils.FindNearestHospitalRequest;
import main.sqlUtils.NationalCoverageRequest;
import main.sqlUtils.SchoolsInRadiusRequest;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

//    private static LatLong bogota = new LatLong(4.657865, -74.100264);
    private List<Marker> schoolMarkers = new ArrayList<>();
    private List<Marker> hospitalMarkers = new ArrayList<>();
    private GoogleMap map;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mapView.addMapInializedListener(this);
        findBtn.setDefaultButton(true);
        // This beautiful line won't allow any other input than integers
        rangeField.textProperty().addListener(new appInterface.IntegerOnlyTextListener(rangeField));
    }

    @Override
    public void mapInitialized() {
        //Set the initial properties of the map.
        MapOptions mapOptions = new MapOptions();
        mapOptions.center(new LatLong(4.657865, -74.100264))
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

        new NationalCoverageRequest(map);

        double radius = 2000;
        //Example display of result sets
        FindNearestHospitalRequest request = new FindNearestHospitalRequest(map.getCenter());


        //Example displaying of result set
        displayResultSet(request.getRequestResult(), true, true);
        //Example draw radius
        drawRadius(map.getCenter(), radius);
        request.closeRequest();

        //Add schools in the radius
        SchoolsInRadiusRequest schoolRequest = new SchoolsInRadiusRequest(map.getCenter(), radius);
        //Do not recentre the map - keep it centred at the hospital
        displayResultSet(schoolRequest.getRequestResult(), false, false);
        schoolRequest.closeRequest();


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

    /**
     * Creates a school marker at desired latlong
     * @param latlong: Coordinates of marker
     * @param name: Name of marker
     */
    protected void schoolMarker(LatLong latlong, String name){
        //TODO(Basil): Make school/hospital markers look different using MarkerOptions.Icon
        //TODO(Basil): Include some kind of animation/user interaction using MarkerOptions.Animation
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latlong)
                .visible(true)
                .title(name);
        schoolMarkers.add(new Marker(markerOptions));
        map.addMarker(schoolMarkers.get(schoolMarkers.size() - 1));
    }

    /**
     * Creates a hospital marker at desired LatLong
     * @param latlong
     * @param name
     */
    protected void hospitalMarker(LatLong latlong, String name){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latlong)
                .visible(true)
                .title(name);
        Marker marker = new Marker(markerOptions);
//        hospitalMarkers.add(new Marker(markerOptions));
//        map.addMarker(hospitalMarkers.get(hospitalMarkers.size() - 1));
        map.addMarker(marker);
    }

    /**
     * Draws radius centred at latlong
     * @param latlong
     * @param radius
     */
    protected void drawRadius(LatLong latlong, double radius){
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latlong)
                .radius(radius)
                .strokeColor("blue")
                .strokeWeight(1)
                .fillColor("blue")
                .fillOpacity(0.2);

        Circle c = new Circle(circleOptions);

        map.addMapShape(c);
    }

    protected void displayResultSet(ResultSet res, Boolean hospital, Boolean recentre){
        double avgLong = 0;
        double avgLat = 0;
        try {
            res.beforeFirst();
            while (res.next()) {
                LatLong coords = new LatLong(res.getDouble(3), res.getDouble(4));

                //If recentering, compute average lat/long for new centre
                if(recentre){
                    avgLat += coords.getLatitude();
                    avgLong += coords.getLongitude();
                }
                if (hospital) {
                    hospitalMarker(coords, res.getString(2));
                } else {
                    schoolMarker(coords, res.getString(2));
                }
            }
            if(recentre){
                int rowCount = res.getRow() + 1;
                map.setCenter(new LatLong(avgLat/rowCount, avgLong/rowCount));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
