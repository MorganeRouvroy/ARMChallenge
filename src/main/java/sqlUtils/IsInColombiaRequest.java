package sqlUtils;

import com.lynden.gmapsfx.javascript.object.LatLong;

/**
 * SQL request checking if a position is in Colombia
 */
public class IsInColombiaRequest extends SQLRequest {
    /**
     * Check if given position is in colombia
     * @param clat center latitude
     * @param clon center longitude
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
