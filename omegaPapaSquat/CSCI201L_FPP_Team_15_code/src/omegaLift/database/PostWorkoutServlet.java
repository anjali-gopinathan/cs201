package omegaLift.database;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/PostWorkout")
public class PostWorkoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String caption = request.getParameter("caption");
        String description = request.getParameter("description");
        
        Connection conn = null;
        Statement st = null;
        String json = "{\"result\":\"success\"}";
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/omegaLift?user=root&password=root");
            st = conn.createStatement();
            st.executeUpdate("INSERT INTO Workouts (title, author, caption, description, posted) VALUES ('"
                    + title + "', '"
                    + author + "', '"
                    + caption + "', '"
                    + description + "', NOW())"
            );
            out.println(json);
        } catch (SQLException | ClassNotFoundException sqle) {
            json = "{\"result\":\"error\", \"message\":\""
                    + "SQLException: " + sqle.getMessage()
                    + "\"}";
            out.println(json);
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
