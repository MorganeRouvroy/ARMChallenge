package src.test;

import src.main.sqlUtils.Connector;
import src.main.sqlUtils.FindNearestHospitalRequest;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestSQL {

    @Test
    public void testConnector() {
        new Connector();
        assertNotNull(Connector.getCon());
        Connector.closeCon();
    }

    @Test
    public void testFindNearestHospital() {
        new Connector();
        assertNotNull(Connector.getCon());

        FindNearestHospitalRequest request = new FindNearestHospitalRequest(4.710989, 74.072092);
        request.PrintResult();

        request.closeRequest();
        Connector.closeCon();
    }
}
