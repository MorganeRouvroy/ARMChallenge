package main.sqlUtils;

import com.lynden.gmapsfx.javascript.object.LatLong;

/**
 * SQL request for getting all the schools in the radius of given position
 */
public class IsInColombiaRequest extends SQLRequest {
    /**
     * Get schools in radius of given position
     * @param clat center latitude
     * @param clon center longitude
     * @param radius radius IN METER
     */
    public IsInColombiaRequest(double clat, double clon) {
        super("SELECT ST_CONTAINS(geom, ST_SetSRID(ST_MakePoint(?, ?), 4326)) FROM national;",
              new Object[] {clon, clat});
    }

    public IsInColombiaRequest(LatLong latlong) {
        super("SELECT ST_CONTAINS(geom, ST_SetSRID(ST_MakePoint(?, ?), 4326)) FROM national;",
                new Object[] {latlong.getLongitude(), latlong.getLatitude()});
    }
}
