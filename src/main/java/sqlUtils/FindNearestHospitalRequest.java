package sqlUtils;

import com.lynden.gmapsfx.javascript.object.LatLong;

/**
 * SQL request for the nearest hospital from a given position
 */
public class FindNearestHospitalRequest extends SQLRequest {

    public FindNearestHospitalRequest(double latitude, double longitude) {
        super("SELECT gid, name, ST_Y(geom), ST_X(geom) FROM healthsites " +
                "ORDER BY geom <-> ST_SetSRID(ST_MakePoint(?, ?),4326) " +
                "LIMIT 1;",
                new Double[] {longitude, latitude});
    }

    public FindNearestHospitalRequest(LatLong latlong) {
        super("SELECT gid, name, ST_Y(geom), ST_X(geom) FROM healthsites " +
                        "ORDER BY geom <-> ST_SetSRID(ST_MakePoint(?, ?),4326) " +
                        "LIMIT 1;",
                new Double[] {latlong.getLongitude(), latlong.getLatitude()});
    }
}
