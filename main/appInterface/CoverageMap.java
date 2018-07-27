package main.appInterface;

import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.shapes.Polygon;
import com.lynden.gmapsfx.shapes.PolygonOptions;
import main.sqlUtils.NationalCoverageRequest;
import netscape.javascript.JSObject;
import org.w3c.dom.events.UIEvent;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CoverageMap {

    private ArrayList<Polygon> polygons = new ArrayList<>();
    private ArrayList<InfoWindow> infs = new ArrayList<>();
    private boolean infsOpen;
    CoverageMap(GoogleMap map) {

        String[] stateList = {"Arauca", "Sucre", "Caquetá", "Magdalena", "Risaralda", "Chocó", "Cauca",
                "San Andrés y Providencia", "Caldas", "Santander", "Vaupés", "Meta", "Cesar", "Putumayo", "Nariño",
                "Valle del Cauca", "La Guajira", "Guainía", "Bolívar", "Tolima", "Huila", "Bogotá D.C.", "Boyacá",
                "Quindío", "Casanare", "Cundinamarca", "Vichada", "Atlántico", "Norte de Santander",
                "Córdoba", "Guaviare", "Amazonas", "Antioquia"};

        for (String name : stateList) {
            NationalCoverageRequest request = new NationalCoverageRequest(name);
            ResultSet res = request.getRequestResult();
            String color = extractColor(res);
            MVCArray path = convertToLatLong(res);
            LatLong centre = new LatLong(0.0, 0.0);
            double avgDist = -1;
            double score = -1;
            try {
                res.beforeFirst();
                res.next();
                centre = new LatLong(res.getDouble(4), res.getDouble(5));
                avgDist = res.getDouble(6);
                score = res.getDouble(3);
                System.out.format("Polygon centre is %.2f, %.2f%n", centre.getLatitude(), centre.getLongitude());
                res.isBeforeFirst();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            drawPolygon(map, path, color, name, centre, avgDist);
            request.closeRequest();
        }
    }

    private String extractColor(ResultSet res) {
        double score = -1;
        try {
            if(res.next()){
                score = res.getDouble(3);
            }
            res.beforeFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("score is " + score);
        if (score == -1) {
            return "#303030"; //no data == grey
        } else {
            String red = Integer.toHexString((int)(255 * (1 -score)));
            red = (red.length() == 2) ? red : ("00".substring(2 - red.length()) + red);
            String green = Integer.toHexString((int) (255 * score));
            green = (green.length() == 2) ? green : ("00".substring(2 - green.length()) + green);
            String color = "#"+red+green+"00";
            System.out.println("color : " +color );
            return color;
        }
    }

    private MVCArray convertToLatLong(ResultSet res) {
        MVCArray list = new MVCArray();
        try {
            while (res.next()) {
                double lon = res.getDouble(1);
                double lat = res.getDouble(2);
                list.push(new LatLong(lat, lon));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void drawPolygon(GoogleMap map, MVCArray path, String color, String name, LatLong centre, double score) {
        PolygonOptions options = new PolygonOptions();
        options.clickable(true)
                .draggable(false)
                .editable(false)
                .strokeColor(color)
                .strokeWeight(1)
                .fillColor(color)
                .fillOpacity(0.2)
                .visible(true)
                .paths(path)
                .zIndex(-10);

        Polygon polygon = new Polygon(options);
        polygon.setDraggable(false);
        map.addMapShape(polygon);

        //INFO WINDOW
        //GET POLYGON CENTRE
//        LatLongBounds bnds = polygon.getBounds();
//        System.out.println(bnds.toString());
//        LatLong SW = bnds.getSouthWest();
//        LatLong NE = bnds.getNorthEast();
//        LatLong centre = new LatLong(0.5*(SW.getLatitude() + NE.getLatitude()), 0.5*(SW.getLongitude() + SW.getLongitude()));
//        LatLong centre = new LatLong(0.0,0.0);
        InfoWindowOptions infOpt = new InfoWindowOptions();
        if(score < 0) {
            infOpt.content(name + " <br/> No schools found")
                    .position(centre)
                    .disableAutoPan(true);
        } else {
            infOpt.content(name + " <br/> Average distance from schools to nearest hospital: " + Math.round(score*100)/100.0 +"km")
                    .position(centre)
                    .disableAutoPan(true);
        }
        InfoWindow inf = new InfoWindow(infOpt);
        map.addUIEventHandler(polygon, UIEventType.click, (JSObject obj) -> inf.open(map));

        polygons.add(polygon);
        infs.add(inf);
        infsOpen = true;
    }

    void changeVisibility() {
        for(Polygon poly : polygons) {
            poly.setVisible(! poly.getVisible());
        }
        changeInfoWindows(!infsOpen);
        infsOpen = !infsOpen;
    }

    public void changeInfoWindows(boolean show){
        if(!show){
            //Hide all info windows
            for (InfoWindow inf : infs) {
                inf.close();
            }
        }
    }
}
