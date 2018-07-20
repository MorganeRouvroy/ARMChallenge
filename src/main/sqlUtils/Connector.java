package src.main.sqlUtils;
import java.sql.*;

import static java.lang.System.err;

/**
 *
 * Class managing the connection to the database
*/

public class Connector {
 	
	private static Connection con = null; //current connection
	
	/** Creation of the connection: driver registration + connection*/
 	public Connector(){
 		try{
 			//PostGre driver registration
 			System.out.print("Loading PostGRE driver... "); 
			DriverManager.registerDriver (new org.postgresql.Driver());
 			System.out.println("loaded");
 			
 			//Connection 
 			System.out.print("Connecting to the database... "); 
 			String URL = "jdbc:postgresql://arm-challenge.ckbmr2i1ii7b.eu-west-2.rds.amazonaws.com/ARMChallenge";
 			String USER = "guest";
 			String PASSWD = "guest";

 			con = DriverManager.getConnection(URL, USER, PASSWD);
 			System.out.println("connected");

			//make autocommit OFF
			con.setAutoCommit(false);

 		} catch (SQLException e){
 			e.printStackTrace(err);
            err.println("Cette application nécessite un accès internet.");
            System.exit(1);
 		}
 	}
 	
	/**Getter
	 * @return the established connection
	 */
 	public static Connection getCon(){
 		return con;
 	}
	
 	/**Close the current connection*/
	public static void closeCon(){
		try{
 			System.out.print("Closing connection... "); 
			con.close();
 			System.out.println(" closed");
		} catch (SQLException e){
			e.printStackTrace();
			System.exit(1);
		}
	}
}

