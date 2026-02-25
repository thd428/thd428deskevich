import java.sql.*;
import java.util.*;

public class Transcript {

    private static final String DB_URL = "jdbc:oracle:thin:@//rocordb01.cse.lehigh.edu:1522/cse241pdb";
    public static void main(String[] args) {
        
        Connection conn = null;
        Scanner scan = new Scanner(System.in);

        do{
            //enter oracle id and pass
            String oracle_id, oracle_password;

            System.out.println("Enter Oracle ID: ");
            oracle_id = scan.nextLine();
            System.out.println("Enter Oracle Password: ");
            oracle_password = scan.nextLine();

            try{
                conn = DriverManager.getConnection(DB_URL, oracle_id, oracle_password);
                System.out.println("Connection Confirmed.");
            }
            catch(SQLException se){
                System.err.println("Connection Failed.");
            }
        } while (conn == null);
        
        while (true) {

            System.out.print("Search student names by substring: ");
            String sub = scan.nextLine();

            String sql =
                "SELECT id, name FROM student WHERE LOWER(name) LIKE LOWER(?) ORDER BY name";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, "%" + sub + "%");
                ResultSet rs = ps.executeQuery();
                int count = 0;

                while (rs.next()) {
                    count++;
                    System.out.printf("ID: %d, Name: %s%n",
                            rs.getInt("id"),
                            rs.getString("name"));
                }

                if (count > 0)
                    break;

                System.out.println("No matches. Try again.\n");

            } catch (SQLException e) {
                System.out.println("Search failed. Try again.");
            }
        }


        int studentId = -1;

        while (true) {
            System.out.print("Enter a student ID: ");
            String input = scan.nextLine();

            try {
                studentId = Integer.parseInt(input);
                if (studentId >= 0)
                    break;
            } catch (NumberFormatException ignored) {}

            System.out.println("Invalid ID. Enter a number.\n");
        }

        scan.close();


        try (PreparedStatement check = conn.prepareStatement("SELECT name FROM student WHERE id = ?")) {

            check.setInt(1, studentId);
            ResultSet rs = check.executeQuery();

            if (!rs.next()) {
                System.out.println("No student exists with that ID.");
                conn.close();
                return;
            }

            String studentName = rs.getString("name");
            System.out.println("\nTranscript for " + studentName + " (ID " + studentId + ")\n");

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        String transcriptSQL =
            "SELECT t.year, t.semester, c.dept_name AS dept, " +
            "t.course_id, c.title, t.grade " +
            "FROM takes t JOIN course c ON t.course_id = c.course_id " +
            "WHERE t.id = ? " +
            "ORDER BY t.year ASC, " +
            "CASE t.semester WHEN 'Spring' THEN 0 ELSE 1 END, " +
            "t.course_id ASC";

        try (PreparedStatement ps = conn.prepareStatement(transcriptSQL)) {

            ps.setInt(1, studentId);

            ResultSet rs = ps.executeQuery();
            boolean empty = true;

            System.out.printf("%-6s %-8s %-10s %-8s %-35s %-6s%n",
                    "Year", "Sem", "Dept", "CID", "Title", "Grade");
            System.out.println("--------------------------------------------------------------------------");

            while (rs.next()) {
                empty = false;

                System.out.printf("%-6d %-8s %-10s %-8d %-35s %-6s%n",
                        rs.getInt("year"),
                        rs.getString("semester"),
                        rs.getString("dept"),
                        rs.getInt("course_id"),
                        rs.getString("title"),
                        rs.getString("grade"));
            }

            if (empty)
                System.out.println("This student has not taken any courses.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}