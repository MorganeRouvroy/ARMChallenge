package test;

import main.sqlUtils.Connector;
import main.sqlUtils.FindNearestHospitalRequest;
import main.sqlUtils.IsInColombiaRequest;
import main.sqlUtils.SchoolsInRadiusRequest;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;
/*
 * Tests for database related methods. All queries tests are made on a special testing database
 * with known values
 */
public class TestSQL {

    @Test
    /*
      Test connection methods
     */
    public void testConnector() {
        new Connector();
        assertNotNull(Connector.getCon());
        Connector.closeCon();
    }

    @Test
    /*
      Test nearest request. Nearest hospital from ARM ltd should be Addensbrook
     */
    public void testFindNearestHospital() {
        new Connector("jdbc:postgresql://arm-challenge.ckbmr2i1ii7b.eu-west-2.rds.amazonaws.com/Testing",
                    "guest",
                    "guest");
        assertNotNull(Connector.getCon());

        FindNearestHospitalRequest request = new FindNearestHospitalRequest(52.181908, 0.178871);
        ResultSet res = request.getRequestResult();
        String name ="";
        try {
            if(res.next()) {
                 name = res.getString(2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        request.closeRequest();
        assertEquals("Addensbrook hospital", name);
        Connector.closeCon();
    }

    @Test
    /*
      Test in radius request. Center is Arm ltd.
      With a radius of 1km, should not return anything
      With a 2km radius, only return Cambridge international school
      With a radius of 10km should return both Cambridge international school and Milton Road primary school
     */
    public void testSchoolsInRadius() {
        new Connector("jdbc:postgresql://arm-challenge.ckbmr2i1ii7b.eu-west-2.rds.amazonaws.com/Testing",
                "guest",
                "guest");
        assertNotNull(Connector.getCon());

        try {
            SchoolsInRadiusRequest request0 = new SchoolsInRadiusRequest(52.181908, 0.178871, 1000);
            ResultSet res0 = request0.getRequestResult();
            res0.last();
            assertEquals(0, res0.getRow());
            request0.closeRequest();

            SchoolsInRadiusRequest request1 = new SchoolsInRadiusRequest(52.181908, 0.178871, 2000);
            ResultSet res1 = request1.getRequestResult();
            res1.last();
            assertEquals(1, res1.getRow());
            res1.first();
            String name = res1.getString(2);
            assertEquals("Cambridge International school", name);
            request1.closeRequest();

            SchoolsInRadiusRequest request2 = new SchoolsInRadiusRequest(52.181908, 0.178871, 10000);
            ResultSet res2 = request2.getRequestResult();
            res2.last();
            assertEquals(2, res2.getRow());
            res2.first();
            name = res2.getString(2);
            assertEquals("Cambridge International school", name);
            res2.next();
            String name2 = res2.getString(2);
            assertEquals("Milton Road Primary school", name2);
            request2.closeRequest();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Connector.closeCon();
    }


    @Test
    /*
      Test IsInColumbiaRequest

     */
    public void testIsInColumbia() {
        new Connector("jdbc:postgresql://arm-challenge.ckbmr2i1ii7b.eu-west-2.rds.amazonaws.com/Testing",
                "guest",
                "guest");
        assertNotNull(Connector.getCon());

        try {
            //Variable to hold results
            ResultSet res;

            //Bogota - should be within
            IsInColombiaRequest request0 = new IsInColombiaRequest(4.676343, -74.087217);
            res = request0.getRequestResult();
            res.next();
            assertTrue(res.getBoolean(1));
            request0.closeRequest();

            //Puerto Carreño, just inside of Venezuelan border - should be inside
            IsInColombiaRequest request1 = new IsInColombiaRequest(6.185448, -67.478935);
            res = request1.getRequestResult();
            res.next();
            assertTrue(res.getBoolean(1));
            request1.closeRequest();

            //Puerto Páez, just outside Venezuelan border - should be outside
            IsInColombiaRequest request2 = new IsInColombiaRequest(6.214964, -67.449682);
            res = request2.getRequestResult();
            res.next();
            assertFalse(res.getBoolean(1));
            request1.closeRequest();

            //Rio de Janeiro in Brazil
            IsInColombiaRequest request3 = new IsInColombiaRequest(-22.925874, -43.447431);
            res = request3.getRequestResult();
            res.next();
            assertFalse(res.getBoolean(1));
            request1.closeRequest();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        Connector.closeCon();
    }
}
