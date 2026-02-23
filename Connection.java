import java.sql.*;
import java.util.*;

public class Connection {
    public static void main(String[] args) {
        
        Connection conn = null;
        
        do{
            //enter oracle id and pass
            string oracle_id, oracle_password;
            Scanner scan = new Scanner(System.in);

            System.out.println("Enter Oracle ID: ");
            oracle_id = scan.nextline();
            Sytem.out.println("Enter Oracle Password: ");
            oracle_password = scan.nextline();

            try{
                conn = DriverManager.getConnection(DB_URL, oracle_id, oracle_password);
                System.out.println("Connection Confirmed.");
            }
            catch(SQLException se){
                System.err.println("Connection Failed.");
            }
        } while (conn == null);

        // here goes work

        if(conn != null){
            try{
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}