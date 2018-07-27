package sqlUtils;

import com.lynden.gmapsfx.javascript.object.LatLong;

/**
 * SQL request for getting all the schools in the radius of given position
 */
public class SchoolsInRadiusRequest extends SQLRequest {
    /**
     * Get schools in radius of given position
     * @param clat center latitude
     * @param clon center longitude
     * @param radius radius IN METER
     */
    public SchoolsInRadiusRequest(double clat, double clon, double radius) {
        super("SELECT DISTINCT ON (gid) gid, name, ST_Y(geom), ST_X(geom) FROM filtered_schools " +
              "WHERE ST_DWithin(geom::geography, ST_GeogFromText(?),?);",
              new Object[] {"POINT("+clon + " " + clat+")", radius});
    }

    public SchoolsInRadiusRequest(LatLong latlong, double radius) {
        super("SELECT DISTINCT ON (gid) gid, name, ST_Y(geom), ST_X(geom) FROM filtered_schools " +
                        "WHERE ST_DWithin(geom::geography, ST_GeogFromText(?),?);",
                new Object[] {"POINT("+latlong.getLongitude()+ " " + latlong.getLatitude()+")", radius});
    }
}
