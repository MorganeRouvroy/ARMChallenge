package main.sqlUtils;

public class NationalCoverageRequest extends SQLRequest{

    public NationalCoverageRequest(String name) {
        super("SELECT ST_X((ST_DumpPoints(St_SIMPLIFY(geom, 0.005))).geom), " +
                "ST_Y((ST_DumpPoints(St_SIMPLIFY(geom, 0.005))).geom), score FROM departments " +
                "WHERE admin1name = ?;",
                new String[]{name});
    }
}
