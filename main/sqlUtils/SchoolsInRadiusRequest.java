package main.sqlUtils;

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
        super("SELECT DISTINCT ON (gid) gid, name, ST_X(geom), ST_Y(geom) FROM schools " +
              "WHERE ST_DWithin(geom::geography, ST_GeogFromText(?),?);",
              new Object[] {"POINT("+clon + " " + clat+")", radius});
    }
}
