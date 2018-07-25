package main.sqlUtils;

public class NationalCoverageRequest extends SQLRequest{

    public NationalCoverageRequest(String name) {
        super("SELECT ST_X((ST_DumpPoints(geom)).geom), " +
                "ST_Y((ST_DumpPoints(geom)).geom), score FROM departments " +
                "WHERE admin1name = ?;",
                new String[]{name});
    }
}
