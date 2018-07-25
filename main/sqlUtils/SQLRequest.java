package main.sqlUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Allow formatting of a request by replacing the ? with the correct options
 * This object NEEDS to be closed by the user using the closeRequest() method
 */
public class SQLRequest {
    private PreparedStatement stmt = null;
    private ResultSet res = null ;

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
    public SQLRequest(String req, Object[] options) {
        try{
            Connection con = Connector.getCon();
            stmt = con.prepareStatement(req, res.TYPE_SCROLL_INSENSITIVE, res.CONCUR_UPDATABLE);
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

    /**
     * Close the statement and the ResultSet.
     */
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

    /**
     * Print the results of a query on the terminal. Useful for debugging
     */
    public void PrintResult() {
        try {
            res.beforeFirst();
            int columnsNumber = res.getMetaData().getColumnCount();
            while (res.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    Object columnValue = res.getObject(i);
                    System.out.print(res.getMetaData().getColumnName(i) + " " + columnValue);
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int resultCount(){

        int c = 0;

        try{
            if(res.last()){
                c = res.getRow();
                res.beforeFirst();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }
}
