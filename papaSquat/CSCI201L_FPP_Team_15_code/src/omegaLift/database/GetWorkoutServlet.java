package omegaLift.database;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import omegaLift.models.Workout;

@WebServlet("/GetWorkout")
public class GetWorkoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String workoutID = request.getParameter("workoutID");
        String query = "SELECT * FROM Workouts WHERE workoutID = " + workoutID + ";";
        
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/omegaLift?user=root&password=root");
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Workout workout = new Workout();
            while (rs.next()) {
                workout = new Workout(
                        rs.getInt("workoutID"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("caption"),
                        rs.getString("description"),
                        rs.getTimestamp("posted")
                );
            }
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            out.println(new Gson().toJson(workout));
        } catch (SQLException | ClassNotFoundException sqle) {
            System.out.println ("SQLException: " + sqle.getMessage());
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                System.out.println("sqle: " + sqle.getMessage());
            }
        }
    }
}
