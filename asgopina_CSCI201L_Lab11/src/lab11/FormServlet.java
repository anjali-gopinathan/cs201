package lab11;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class lab11
 */
@WebServlet("/FormServlet")
public class FormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
//    public FormServlet() {
//        super();
//        // TODO Auto-generated constructor stub
//    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
//	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		doGet(request, response);
//	}
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String fname = request.getParameter("fname");
		String lname = request.getParameter("lname");
		String email = request.getParameter("email");
		int age = Integer.parseInt(request.getParameter("age"));
		int rating = Integer.parseInt(request.getParameter("rating"));
		String month = request.getParameter("month");
//		System.out.println("fname = " + fname);
//		System.out.println("lname = " + lname);
		
		response.setContentType("application/json");
		out.println("{");
		out.println("\"First name\":\"" + fname + "\"");
		out.println("\"Last name\":\"" + lname + "\"");
		out.println("\"Email\":\"" + email + "\"");
		out.println("\"Age\":" + age);
		out.println("\"DayRating\":" + rating);
		out.println("\"Month\":\"" + month + "\"");

		out.println("}");

		
	}

}
