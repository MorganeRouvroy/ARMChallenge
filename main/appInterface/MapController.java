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
import javafx.stage.Stage;
import main.sqlUtils.FindNearestHospitalRequest;
import main.sqlUtils.NationalCoverageRequest;
import main.sqlUtils.SchoolsInRadiusRequest;
import sun.jvm.hotspot.oops.Mark;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MapController implements Initializable, MapComponentInitializedListener {

    private ArrayList<Marker> schoolMarkers = new ArrayList<>();
    private ArrayList<Marker> hospitalMarkers = new ArrayList<>();
    private Circle circle;

    @FXML
    private CheckBox heatMap;
    @FXML
    private CheckBox hospital;
    @FXML
    private CheckBox school;

    @FXML
    private Button nearestHospital;
    @FXML
    private Button clear;

    @FXML TextField radiusSelection;

    @FXML
    private GoogleMapView mapView;

//    private static LatLong bogota = new LatLong(4.657865, -74.100264);
    private GoogleMap map;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mapView.addMapInializedListener(this);
        // This beautiful line won't allow any other input than integers
        radiusSelection.textProperty().addListener(new appInterface.IntegerOnlyTextListener(radiusSelection));
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
        //TODO(*): Exit function if these fail...
        setRangeField(event);

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

    /* Listener for the Text field. */
    @FXML
    protected void setRangeField(ActionEvent event) {
        int number1 = parseInt(radiusSelection.getText(),-1);
        // get a handle to the stage
        Stage stage = (Stage) heatMap.getScene().getWindow();

        if (number1 < 0 || number1 > 50000) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to parse values in the range field.\n" +
                    "Please ensure the values are in the range.", ButtonType.CLOSE);
            alert.setTitle("Wrong input!");
            alert.showAndWait();
            stage.close();
        }
        else if (number1 == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to parse values in the range field.\n" +
                    "Please ensure the values are not null.", ButtonType.CLOSE);
            alert.setTitle("Failed to parse values");
            alert.showAndWait();
            stage.close();
        }
        else if (number1 == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to parse values in the range field.\n" +
                    "Please ensure the values are not null.", ButtonType.CLOSE);
            alert.setTitle("Failed to parse values");
            alert.showAndWait();
            stage.close();

        }
        else
            System.out.println("map :(");
    }

    @FXML
    protected void clearMap(ActionEvent event) {
        map.removeMarkers(schoolMarkers);
        map.removeMarkers(hospitalMarkers);

        if(circle != null)
            map.removeMapShape(circle);
    }

    /**
     * Method that catches the null input or 0 input
     *
     * @param s the string converted to number
     * @param valueIfInvalid the returned value if the input is invalid
     * @return the numeric value for the input
     */
    protected static int parseInt(final /*@Nullable*/ String s, final int valueIfInvalid) {
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
                .icon("https://i.imgur.com/CrvqHKb.png")
                .title(name);
        Marker marker = new Marker(markerOptions);
        map.addMarker(marker);
        schoolMarkers.add(marker);
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
                .animation(Animation.DROP)
                .icon("https://i.imgur.com/D5sCHnA.png")
                .title(name);
        Marker marker = new Marker(markerOptions);
        map.addMarker(marker);
        hospitalMarkers.add(marker);
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

        circle = new Circle(circleOptions);

        map.addMapShape(circle);
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
                //TODO(Set zoom to fit bounding box)
                int rowCount = res.getRow() + 1;
                map.setCenter(new LatLong(avgLat/rowCount, avgLong/rowCount));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
