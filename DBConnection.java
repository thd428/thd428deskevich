import java.sql.*;
import java.util.*;

public class DBConnection {

    private static final String DB_URL = "jdbc:oracle:thin:@//rocordb01.cse.lehigh.edu:1522/cse241pdb";
    public static void main(String[] args) {
        
        Connection conn = null;
        Scanner scan = new Scanner(System.in);

        do{
            //enter oracle id and pass
            //String oracle_id, oracle_password;

            //System.out.println("Enter Oracle ID: ");
            //oracle_id = scan.nextLine();
            //System.out.println("Enter Oracle Password: ");
            //oracle_password = scan.nextLine();

            try{
                conn = DriverManager.getConnection(DB_URL, "thd428", "P800119540");
                System.out.println("Connection Confirmed.");
            }
            catch(SQLException se){
                System.err.println("Connection Failed.");
            }
        } while (conn == null);
        
        ResultSet rs = null;
        int rsLen = 0;

        do {
            System.out.println("Search student names by substring: ");
            String sub = scan.nextLine();
            
            try {
                PreparedStatement ps = conn.prepareStatement("SELECT id,name FROM student WHERE name LIKE ?");
                ps.setString(1, "%" + sub + "%");
                rs = ps.executeQuery();
                rsLen = 0;
                while (rs.next()) {
                    rsLen++;
                    int id = rs.getInt("id");
                    String firstName = rs.getString("name");
                    System.out.println("ID: " + id + ", Name: " + firstName);
                }
            } catch (SQLException se) {
                se.printStackTrace();
                System.err.println("Exectution Failed.");
            }
        } while (rsLen == 0);
        
        
        
        System.out.println("Enter Student ID: ");
        int id = scan.nextInt();

        if (idExists(rs, id)){ ///writeME

        } else {
            System.out.println("ID does not correspond to a student.")
        }
        

        if(conn != null){
            try{
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}