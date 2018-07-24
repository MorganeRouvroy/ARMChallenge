package main.sqlUtils;

import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MVCArray;
import com.lynden.gmapsfx.shapes.Polygon;
import com.lynden.gmapsfx.shapes.PolygonOptions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class NationalCoverageRequest {

    public NationalCoverageRequest(GoogleMap map) {
        String[] StateNames = {"Arauca"};/*, "Sucre", "Caquetá", "Magdalena", "Risaralda", "Chocó", "Cauca",
                "San Andrés y Providencia", "Caldas", "Santander", "Vaupés", "Meta", "Cesar", "Putumayo", "Nariño",
                "Valle del Cauca", "La Guajira", "Guainía", "Bolívar", "Tolima", "Huila", "Bogotá D.C.", "Boyacá",
                "Quindío", "Casanare", "Cundinamarca", "Vichada", "Atlántico", "Norte de Santander",
                "Córdoba", "Guaviare", "Amazonas", "Antioquia"};*/
        for(String name : StateNames) {
           SQLRequest request = new SQLRequest("SELECT ST_X((ST_DumpPoints(geom)).geom), " +
                                                "ST_Y((ST_DumpPoints(geom)).geom) FROM departments " +
                                                "WHERE admin1name = ?;",
                                     new String[]{name});
           //Get color somehow
            String color = "#FF0000"; //red for now
            ResultSet res = request.getRequestResult();
            Object[] path = convertToLatLong(res);
            drawPolygon(map, path, color);
            request.closeRequest();
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
                .strokeColor("red")
                .strokeWeight(0.5)
                .fillColor(color)
                .fillOpacity(0.8)
                .visible(true)
                .paths(new MVCArray(path));

        Polygon polygon = new Polygon(options);
        map.addMapShape(polygon);
    }
}
