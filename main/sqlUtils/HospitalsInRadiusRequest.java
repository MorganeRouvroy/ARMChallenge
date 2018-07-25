package main.sqlUtils;

import com.lynden.gmapsfx.javascript.object.LatLong;

public class HospitalsInRadiusRequest extends SQLRequest{
    /**
     * Get hospitals in radius of given position
     * @param clat center latitude
     * @param clon center longitude
     * @param radius radius IN METER
     */
    public HospitalsInRadiusRequest(double clat, double clon, double radius) {
        super("SELECT DISTINCT ON (gid) gid, name, ST_Y(geom), ST_X(geom) FROM healthsites " +
                        "WHERE ST_DWithin(geom::geography, ST_GeogFromText(?),?);",
                new Object[] {"POINT("+clon + " " + clat+")", radius});
    }

    public HospitalsInRadiusRequest(LatLong latlong, double radius) {
        super("SELECT DISTINCT ON (gid) gid, name, ST_Y(geom), ST_X(geom) FROM healthsites " +
                        "WHERE ST_DWithin(geom::geography, ST_GeogFromText(?),?);",
                new Object[] {"POINT("+latlong.getLongitude()+ " " + latlong.getLatitude()+")", radius});
    }
}
