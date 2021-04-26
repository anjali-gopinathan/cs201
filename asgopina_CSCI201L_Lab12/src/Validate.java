import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Validate")
public class Validate extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fieldToValidate = request.getParameter("field");
		PrintWriter out = response.getWriter();
		
		String fname = request.getParameter("fname");
		String lname = request.getParameter("lname");
		String email = request.getParameter("email");
		
		response.setContentType("application/json");
		out.println("{");
		out.println("\"First name\":\"" + fname + "\"");
		out.println("\"Last name\":\"" + lname + "\"");
		out.println("\"Email\":\"" + email + "\"");

		out.println("}");
		
		
		if(fieldToValidate != null && fieldToValidate.equals("username")) {
			String username = request.getParameter("username");
			if(username != null && username.length() > 0) {
				out.println("Valid Username");
			}
			else {
				out.println("Invalid Username");
			}
		}
		if(fieldToValidate != null && fieldToValidate.equals("password")) {
			String password = request.getParameter("password");
			if(password != null && password.length() > 0) {
				out.println("Valid Password");
			}
			else {
				out.println("Invalid Password");
			}
		}
	}
}
