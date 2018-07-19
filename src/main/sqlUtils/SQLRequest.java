package main.sqlUtils;

import java.sql.*;

/**
 * Allow formatting of a request by replacing the ? with the correct options
 * This object NEEDS to be closed by the user using the closeRequest() method
 */
public class SQLRequest {
    private PreparedStatement stmt = null;
    ResultSet res = null ;

    /**
     * Default constructor. For request with no ?
     * @param req the request
     */
    public SQLRequest(String req) {
        this(req, new String[0]);
    }


    /**
     * Constructor for ? requests
     * /!\ requires options.length == "nb of ? in req"
     * @param req the request with ?
     * @param options the options
     */
    SQLRequest(String req, Object[] options) {
        try{
            Connection con = Connector.getCon();
            stmt = con.prepareStatement(req);

            for (int i=1; i<=options.length; i++) {
                stmt.setObject(i, options[i - 1]);
            }
            executeRequest();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public ResultSet getRequestResult() {
        return res;
    }

    public void closeRequest(){
        try {
            res.close();
            stmt.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    private void executeRequest() {
        try {
            res = stmt.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void PrintResult() {
        try {
            while (res.next()) {
                for (int i = 1; i <= 2; i++) {
                    if (i > 1) System.out.print(",  ");
                    Object columnValue = res.getObject(i);
                    System.out.print(columnValue + " " );
                }
                System.out.println("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
