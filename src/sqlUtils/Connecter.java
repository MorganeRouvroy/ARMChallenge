/**
 * 
 */
package sqlUtils;
import java.sql.*;

/**
 * @author morga
 *
 */
/**
* Class managing the connection to the database
*/

public class Connecter {
 	
	static Connection con; //current connection
	
	/** Creation of the connection: driver registration + connection*/
 	public Connecter(){
 		try{
 			//PostGre driver registration
 			System.out.print("Loading PostGRE driver... "); 
			DriverManager.registerDriver (new org.postgresql.Driver());
 			System.out.println("loaded");
 			
 			//Connection 
 			System.out.print("Connecting to the database... "); 
 			String URL = "jdbc:postgresql:ARM_innovationChallenge";
 			String USER = "guest";
 			String PASSWD = "guest";

 			con = DriverManager.getConnection(URL, USER, PASSWD);
 			System.out.println("connected");
 			
			//make autocommit OFF
			con.setAutoCommit(false);
			
 		} catch (SQLException e){
 			System.err.println(e);
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
		}
	}
}

