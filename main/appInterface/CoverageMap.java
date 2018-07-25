package main.appInterface;

import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MVCArray;
import com.lynden.gmapsfx.shapes.Polygon;
import com.lynden.gmapsfx.shapes.PolygonOptions;
import main.sqlUtils.NationalCoverageRequest;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CoverageMap {

    private ArrayList<Polygon> polygons = new ArrayList<>();

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
            Object[] path = convertToLatLong(res);
            drawPolygon(map, path, color);
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

    private Object[] convertToLatLong(ResultSet res) {
        ArrayList<LatLong> list = new ArrayList<>();
        try {
            while (res.next()) {
                double lon = res.getDouble(1);
                double lat = res.getDouble(2);
                list.add(new LatLong(lat, lon));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list.toArray();
    }

    private void drawPolygon(GoogleMap map, Object[] path, String color) {
        PolygonOptions options = new PolygonOptions();
        options.clickable(false)
                .draggable(false)
                .editable(false)
                .strokeColor(color)
                .strokeWeight(0.4)
                .fillColor(color)
                .fillOpacity(0.2)
                .visible(true)
                .paths(new MVCArray(path));

        Polygon polygon = new Polygon(options);
        map.addMapShape(polygon);
        polygons.add(polygon);
    }

    public void changeVisibility() {
        for(Polygon poly : polygons) {
            poly.setVisible(! poly.getVisible());
        }
    }
}
