package main.sqlUtils;

/**
 * SQL request for the nearest hospital from a given position
 */
public class FindNearestHospitalRequest extends SQLRequest {

    public FindNearestHospitalRequest(double latitude, double longitude) {
        super("SELECT gid, name FROM healthsites " +
                "ORDER BY geom <-> ST_SetSRID(ST_MakePoint(?, ?),4326) " +
                "LIMIT 1;",
                new Double[] {longitude, latitude});
    }
}
