package sqlUtils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static java.lang.System.err;

/**
 *
 * Class managing the connection to the database
*/

public class Connector {
 	
	private static Connection con = null; //current connection

	/**
	 * Default constructor connects to application database
	 */
	public Connector() {
		this("jdbc:postgresql://arm-challenge.ckbmr2i1ii7b.eu-west-2.rds.amazonaws.com/ARMChallenge",
				"guest",
				"guest");
	}

	/** Creation of the connection: driver registration + connection*/
 	public Connector(String URL, String user, String passwd){
 		try{
 			//PostGre driver registration
 			System.out.print("Loading PostGRE driver... "); 
			DriverManager.registerDriver (new org.postgresql.Driver());
 			System.out.println("loaded");
 			
 			//Connection 
 			System.out.print("Connecting to the database... "); 
 			con = DriverManager.getConnection(URL, user, passwd);
 			System.out.println("connected");

			//make autocommit OFF
			con.setAutoCommit(false);

 		} catch (SQLException e){
 			e.printStackTrace(err);
			Alert alert = new Alert(Alert.AlertType.ERROR, "The application needs an internet connection!", ButtonType.CLOSE);
			alert.setTitle("No internet connection");
			alert.showAndWait();

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

