package main.sqlUtils;

public class NationalCoverageRequest extends SQLRequest{

    public NationalCoverageRequest(String name) {
        super("SELECT ST_X((ST_DumpPoints(St_SIMPLIFY(geom, 0.005))).geom), " +
                "ST_Y((ST_DumpPoints(St_SIMPLIFY(geom, 0.005))).geom), score, ST_Y(ST_CENTROID(St_SIMPLIFY(geom, 0.5))), ST_X(ST_CENTROID(St_SIMPLIFY(geom, 0.5))) FROM departments " +
                "WHERE admin1name = ?;",
                new String[]{name});
    }
}
