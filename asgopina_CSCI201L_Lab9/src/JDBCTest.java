import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCTest {

	public static void main (String[] args) {
		Connection conn = null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		Map<String, Integer> classFreqs = new HashMap<>();		// maps from className to number of students
		Map<String, Map<Integer, String>> gradebook = new HashMap<>();		// maps from className to a map from name to grade
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/lab8_student?user=root&password=root");
			st = conn.createStatement();
//			String myname = "Anjali";
			// rs = st.executeQuery("SELECT * from studentinfo where fname='" + name + "'");
//			ps = conn.prepareStatement("SELECT * FROM studentinfo WHERE name=?");
			ps = conn.prepareStatement("SELECT * FROM grades");
//			ps.setString(1, myname); // set first variable in prepared statement
			rs = ps.executeQuery();
//			int numCols = rs.getMetaData().getColumnCount();
//			System.out.println("executing query...");
//			int rowIdx = 1;
//			System.out.println("about to enter while loop");
			while (rs.next()) {
//				System.out.println("here");
				String className = rs.getString("ClassName");
				if (classFreqs.containsKey(className)) {
					// if map already has this class, increment student freq
//					ps.setInt(4*rowIdx + 2, classFreqs.get(className)+1);
					classFreqs.put(className, classFreqs.get(className) + 1);
					gradebook.get(className).put(rs.getInt("SID"), rs.getString("Grade"));
				} else {
					classFreqs.put(className, 1);
					Map<Integer, String> studentGrades = new HashMap<>();
					studentGrades.put(rs.getInt("SID"), rs.getString("Grade"));
					gradebook.put(className, studentGrades);
				}
				
//				String name = rs.getString("name");
//				String lname = rs.getString("lname");
//				int studentID = rs.getInt("sid");
//				System.out.println ("name = " + name);
//				System.out.println ("lname = " + lname);
//				System.out.println ("studentID = " + studentID);
			}
			DBTablePrinter.printTable(conn, "grades");
		} catch (SQLException sqle) {
			System.out.println ("SQLException: " + sqle.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
		
		classFreqs.forEach((key, value) -> System.out.println(key + " : "  + value));
	}
}