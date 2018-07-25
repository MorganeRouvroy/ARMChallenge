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


import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MapController implements Initializable, MapComponentInitializedListener {
    private ArrayList<Circle> radii = new ArrayList<>();
    private CoverageMap coverageMap;

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

    private static LatLong bogota;
    private GoogleMap map;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mapView.addMapInializedListener(this);
        // This beautiful line won't allow any other input than integers
        radiusSelection.textProperty().addListener(new appInterface.IntegerOnlyTextListener(radiusSelection));
    }

    @Override
    public void mapInitialized() {
        bogota = new LatLong(4.657865, -74.100264);
        //Set the initial properties of the map.
        MapOptions mapOptions = new MapOptions();
        mapOptions.center(bogota)
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
//        // FIND NEAREST HOSPITAL
//        double radius = setRangeField(event)*1000;
//
//        //For clicking schools button
//        FindNearestHospitalRequest request = new FindNearestHospitalRequest(map.getCenter());
//
//        //Display hospital
//        ResultSet res = request.getRequestResult();
//
//        //Get ResultSet info
//        try {
//            res.beforeFirst();
//            res.next();
//            LatLong coords = new LatLong(res.getDouble(3), res.getDouble(4));
//            String name = res.getString(2);
//            System.out.format("Closest hospital was %s at %.3f, %.3f which is %.3fkm away from current location%n", name, coords.getLatitude(), coords.getLongitude(), coords.distanceFrom(map.getCenter())/1000);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        //Display the hospital point
//        displayResultSet(res, true, true, false);
//
//        //Draw radius
//        drawRadius(map.getCenter(), radius, true);
//
//        request.closeRequest();
//
//        SchoolsInRadiusRequest schoolRequest = new SchoolsInRadiusRequest(map.getCenter(), radius);
//        //Do not recentre the map - keep it centred at the hospital
//        displayResultSet(schoolRequest.getRequestResult(), false, false, false);
//
//        System.out.format("Found %d schools within radius of %.2fkm"%n,schoolRequest.resultCount(), radius/1000);
//        schoolRequest.closeRequest();

//        //FIND SCHOOLS WITHIN RADIUS
//        double radius = setRangeField(event)*1000;
//
//        if(radius > 0){
//            SchoolsInRadiusRequest request = new SchoolsInRadiusRequest(map.getCenter(), radius);
//            System.out.format("Found %d schools within %.2fkm of %.3f, %.3f", request.resultCount(), radius/1000, map.getCenter().getLatitude(), map.getCenter().getLongitude());
//            displayResultSet(request.getRequestResult(), false, false, false);
//            drawRadius(map.getCenter(), radius, true);
//            request.closeRequest();
//        }

//        //FIND HOSPITALS WITHIN RADIUS
//        double radius = setRangeField(event);
//
//        if(radius > 0){
//            HospitalsInRadiusRequest request = new HospitalsInRadiusRequest(map.getCenter(), radius);
//            displayResultSet(request.getRequestResult(), false, false, false);
//            System.out.format("Found %d hospitals within %.2fkm of %.3f, %.3f", request.resultCount(), radius/1000, map.getCenter().getLatitude(), map.getCenter().getLongitude());
//            drawRadius(map.getCenter(), radius, true);
//            request.closeRequest();
//        }




    }

    /* Listener for the heatmap checkbox. */
    @FXML
    protected void changeHeatmapVisibility (ActionEvent event) {
        if (coverageMap == null){
            coverageMap = new CoverageMap(map);
        } else {
            coverageMap.changeVisibility();
        }
    }

    /* Listener for the Text field. */
    @FXML
    protected double setRangeField(ActionEvent event) {
        int radius = parseInt(radiusSelection.getText(),-1);
        // get a handle to the stage
        Stage stage = (Stage) heatMap.getScene().getWindow();

        if (radius < 0 || radius > 50000) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to parse values in the range field.\n" +
                    "Please ensure the values are in the range.", ButtonType.CLOSE);
            alert.setTitle("Wrong input!");
            alert.showAndWait();
            stage.close();
            radius = -1;
        }
        else if (radius == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to parse values in the range field.\n" +
                    "Please ensure the values are not null.", ButtonType.CLOSE);
            alert.setTitle("Failed to parse values");
            alert.showAndWait();
            stage.close();
            radius = -1;
        }
        else if (radius == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to parse values in the range field.\n" +
                    "Please ensure the values are not null.", ButtonType.CLOSE);
            alert.setTitle("Failed to parse values");
            alert.showAndWait();
            stage.close();
            radius = -1;

        }
        return radius;
    }

    @FXML
    protected void clearMap(ActionEvent event) {
        map.clearMarkers();

        for (Circle aRadii : radii) {
            map.removeMapShape(aRadii);
        }
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
//        schoolMarkers.add(marker);
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
//        hospitalMarkers.add(marker);
    }

    /**
     * Draws radius centred at latlong
     * @param latlong
     * @param radius
     */
    protected void drawRadius(LatLong latlong, double radius, boolean fit){
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latlong)
                .radius(radius)
                .strokeColor("blue")
                .strokeWeight(1)
                .fillColor("blue")
                .fillOpacity(0.2);

        Circle circle = new Circle(circleOptions);

        map.addMapShape(circle);
        radii.add(circle);

        if(fit){
            //Fit map to bounding box
            LatLongBounds bounds = new LatLongBounds();
            bounds.extend(latlong.getDestinationPoint(90.0, radius));
            bounds.extend(latlong.getDestinationPoint(270.0, radius));
            map.fitBounds(bounds);
        }
    }

    /**
     * Displays coordinates of a result set
     * Expect result set with fields {id}, {name}, {lat}, {long}
     * @param res: The ResultSet
     * @param hospital: True if displaying hospital, false for school
     * @param recentre: If True, recentres to middle of points (without zooming)
     * @param fit: If True, fits map to points (adjust location and zoom
     * if recentre AND fit true, will only fit.
     */
    protected void displayResultSet(ResultSet res, boolean hospital, boolean recentre, boolean fit){
        LatLongBounds bounds = new LatLongBounds();

        try {
            res.beforeFirst();
            if(res.isBeforeFirst()) {
                while (res.next()) {
                    LatLong coords = new LatLong(res.getDouble(3), res.getDouble(4));

                    //Keeps bounding box of coordinates
                    bounds.extend(coords);

                    if (hospital) {
                        hospitalMarker(coords, res.getString(2));
                    } else {
                        schoolMarker(coords, res.getString(2));
                    }
                }
                if(fit) {
                    map.fitBounds(bounds);
                }else if(recentre){
                    map.setCenter(new LatLong(0.5*(bounds.getNorthEast().getLatitude() + bounds.getSouthWest().getLatitude()),
                            0.5*(bounds.getNorthEast().getLongitude() + bounds.getSouthWest().getLongitude())));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
