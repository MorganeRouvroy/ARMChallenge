package main.appInterface;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.service.geocoding.GeocoderStatus;
import com.lynden.gmapsfx.service.geocoding.GeocodingResult;
import com.lynden.gmapsfx.service.geocoding.GeocodingService;
import com.lynden.gmapsfx.shapes.Circle;
import com.lynden.gmapsfx.shapes.CircleOptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import main.sqlUtils.FindNearestHospitalRequest;
import main.sqlUtils.HospitalsInRadiusRequest;
import main.sqlUtils.IsInColombiaRequest;
import main.sqlUtils.SchoolsInRadiusRequest;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static java.lang.Float.parseFloat;

public class MapController implements Initializable, MapComponentInitializedListener {

    private ArrayList<Circle> radii = new ArrayList<>();
    private CoverageMap coverageMap;

    @FXML
    private SplitPane SplitPane = new SplitPane();
    @FXML
    private ToggleButton showHide;

    @FXML
    private TextField radiusSelection;
    @FXML
    private TextField location;
    @FXML
    private Label display;

    @FXML
    private GoogleMapView mapView;

    @FXML
    private AnchorPane mapPane1;
    @FXML
    private AnchorPane mapPane2;

    private GoogleMap map;

    private Image one = new Image(getClass().getResourceAsStream("1.png"));
    private Image two = new Image(getClass().getResourceAsStream("2.png"));

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mapView.addMapInializedListener(this);

        //The collapsing button has to be initialised here
        showHide.setGraphic(new ImageView(two));
        showHide();
    }

    @Override
    public void mapInitialized() {
        LatLong bogota = new LatLong(4.657865, -74.100264);
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

    @FXML
    protected void searchForLocation() {
        String addrStr = location.getText();

        if(addrStr.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please input a search string e.g. 'Hospital de San Jose'", ButtonType.CLOSE);
            alert.setTitle("Invalid search");
            alert.showAndWait();
        }else{
            findAddress(addrStr);
        }
    }

    @FXML
    protected void showHide() {
        showHide.setOnAction(event -> {
            if (showHide.isSelected()) {
                SplitPane.setVisible(false);
                mapPane2.setVisible(true);
                mapPane1.getChildren().clear();
                mapPane2.getChildren().setAll(mapView);
                showHide.setGraphic(new ImageView(one));
            } else {
                SplitPane.setVisible(true);
                mapPane2.setVisible(false);
                mapPane2.getChildren().clear();
                mapPane1.getChildren().setAll(mapView);
                showHide.setGraphic(new ImageView(two));
            }
        });
    }

    @FXML
    protected void nearestHospital () {
        //For clicking schools button
        FindNearestHospitalRequest request = new FindNearestHospitalRequest(map.getCenter());

        //Display hospital
        ResultSet res = request.getRequestResult();

        //Get ResultSet info
        try {
            res.beforeFirst();
            res.next();
            LatLong coords = new LatLong(res.getDouble(3), res.getDouble(4));
            String name = res.getString(2);

            display.setText(String.format("Closest health site was %s at %.3f, %.3f which is %.3fkm away from current location%n", name, coords.getLatitude(), coords.getLongitude(), coords.distanceFrom(map.getCenter()) / 1000));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Display the hospital point
        displayResultSet(res, true, true);


    }

    @FXML
    protected void findSchool (ActionEvent event) {
        double radius = getRangeField(event)*1000;

        if(radius > 0){
            SchoolsInRadiusRequest request = new SchoolsInRadiusRequest(map.getCenter(), radius);

            display.setText(String.format("Found %d schools within %.2fkm of %.3f, %.3f%n", request.resultCount(), radius/1000, map.getCenter().getLatitude(), map.getCenter().getLongitude()));

            displayResultSet(request.getRequestResult(), false, false);
            if(request.resultCount() > 0){drawRadius(map.getCenter(), radius);}
            request.closeRequest();
        }
    }

    @FXML
    protected void findHospital (ActionEvent event) {
        double radius = getRangeField(event)*1000;
        if(radius > 0){
            HospitalsInRadiusRequest request = new HospitalsInRadiusRequest(map.getCenter(), radius);
            displayResultSet(request.getRequestResult(), true, false);

            display.setText(String.format("Found %d health sites within %.2fkm of %.3f, %.3f%n", request.resultCount(), radius / 1000, map.getCenter().getLatitude(), map.getCenter().getLongitude()));

            if(request.resultCount() > 0){drawRadius(map.getCenter(), radius);}
            request.closeRequest();
        }
    }

    /* Listener for the heatmap checkbox. */
    @FXML
    protected void changeHeatmapVisibility (ActionEvent event) {
        if (coverageMap == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Loading coverage map for the first time " +
                    "may take up to 10 seconds.", ButtonType.CLOSE);
            alert.setTitle("Please wait");
            alert.setHeaderText("Coverage map loading");
            alert.show();
            coverageMap = new CoverageMap(map);
            display.setText("Coverage map loaded");
        } else {
            coverageMap.changeVisibility();
        }
    }

    /* Listener for the Text field. */
    @FXML
    protected double getRangeField(ActionEvent event) {
        float radius;

        try {
            radius = parseFloat(radiusSelection.getText());

            if (radius <= 0 || radius > 1000) {
                Alert alert = new Alert(Alert.AlertType.ERROR,"Please input a distance in km between 0 and 1000.\n" +
                        "Decimal numbers are also allowed.", ButtonType.CLOSE);
                alert.setTitle("Radius invalid");
                alert.showAndWait();
                radius = -1;
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please input a distance in km between 0 and 1000.\n" +
                    "Decimal numbers are also allowed.", ButtonType.CLOSE);
            alert.setTitle("Radius is not a number");
            alert.showAndWait();

            radius = -1;
        }

        return radius;
    }

    /* Listener for the clear map button. */
    @FXML
    protected void clearMap(ActionEvent event) {
        map.clearMarkers();

        for (Circle aRadii : radii) {
            map.removeMapShape(aRadii);
        }

        //clear the bottom label
        display.setText("");
        if(coverageMap != null){
            coverageMap.changeInfoWindows(false);
        }
    }

    /**
     * Creates a school marker at desired latlong
     * @param latlong: Coordinates of marker
     * @param name: Name of marker
     */
    private void schoolMarker(LatLong latlong, String name){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latlong)
                .visible(true)
                .icon("https://i.imgur.com/CrvqHKb.png")
                .title(name);
        Marker marker = new Marker(markerOptions);
        map.addMarker(marker);
    }

    /**
     * Creates a hospital marker at desired LatLong
     * @param latlong: Position of hospital marker
     * @param name: Name of hospital
     */
    private void hospitalMarker(LatLong latlong, String name){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latlong)
                .visible(true)
                .animation(Animation.DROP)
                .icon("https://i.imgur.com/D5sCHnA.png")
                .title(name);
        Marker marker = new Marker(markerOptions);
        map.addMarker(marker);
    }

    /**
     * Draws radius centred at latlong
     * @param latlong : centre of radius
     * @param radius : radius of circle
     */
    private void drawRadius(LatLong latlong, double radius){

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latlong)
                .radius(radius)
                .strokeColor("blue")
                .strokeWeight(1)
                .fillColor("blue")
                .fillOpacity(0.2);

        Circle circle = new Circle(circleOptions);

        //First check it is not too close to other circles
        //Don't want many radii stacked on top of each other
        LatLong thisNE = circle.getBounds().getNorthEast();
        LatLong thisSW = circle.getBounds().getSouthWest();
        boolean tooClose = false;
        for (Circle aCircle: radii) {
            tooClose = thisNE.distanceFrom(aCircle.getBounds().getNorthEast()) < 5;
            tooClose = tooClose || (thisSW.distanceFrom(aCircle.getBounds().getSouthWest()) < 5);
            if(tooClose){break;}
        }


        if(!tooClose) {
            //Add circle to the map
            map.addMapShape(circle);
            radii.add(circle);

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
     * @param res : The ResultSet
     * @param hospital : True if displaying hospital, false for school
     * @param recentre : If True, recentres to middle of points (without zooming)
     */
    private void displayResultSet(ResultSet res, boolean hospital, boolean recentre){
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
                if(recentre){
                    map.setCenter(new LatLong(0.5*(bounds.getNorthEast().getLatitude() + bounds.getSouthWest().getLatitude()),
                            0.5*(bounds.getNorthEast().getLongitude() + bounds.getSouthWest().getLongitude())));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Given a string, will use Geocoding service to search for an address
     * @param address: User input string
     */
    private void findAddress(String address) {

        GeocodingService geocodingService = new GeocodingService();
        geocodingService.geocode(address, (GeocodingResult[] results, GeocoderStatus status) -> {

            //Get first result which lies in Colombia
            LatLong centre = null;
            LatLongBounds llb = null;
            String res = null;
            boolean foundResult = false;

            for (GeocodingResult result : results) {
                centre = new LatLong(result.getGeometry().getLocation().getLatitude(), result.getGeometry().getLocation().getLongitude());
                if(isInColombia(centre)){
                    res = result.getFormattedAddress();
                    llb = result.getGeometry().getViewPort();
                    foundResult = true;
                    break;
                }
            }
            if(!foundResult){
                display.setText("Could not find results in Colombia corresponding to search '" + address + "'");
            }else{
                if(llb != null) {
                    map.fitBounds(llb);
                }else {
                    map.setCenter(centre);
                }
                display.setText("Best match in Colombia is " + res);
            }


        });
    }

    /**
     * Checks whether a requested LatLong is in Colombia using IsInColombia request
     * @param latlong: Coordinates to check
     *
     * @return: True if latlong is in colombia
     */
    private boolean isInColombia(LatLong latlong){
        IsInColombiaRequest request = new IsInColombiaRequest(latlong);
        ResultSet res = request.getRequestResult();
        boolean result = false;
        try {
            res.next();
            result = res.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        request.closeRequest();
        return result;
    }
}
