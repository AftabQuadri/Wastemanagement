package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DonorSignIn
 */
public class DonorSignIn extends HttpServlet {
	Connection con;
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DonorSignIn() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		con = (Connection) getServletContext().getAttribute("oracle");
		PrintWriter pw = response.getWriter();
		String usertype=request.getParameter("usertype");
		String tablename=usertype;
		String email = request.getParameter("donoremail");
		String password = request.getParameter("donorpassword");
		System.out.println(con);
		PreparedStatement pstmt;
		// accept all user entered fields
		try {
			// checking if user email exist in donor table
			System.out.println("Tablename: "+tablename);
			String sql = "select * from " +tablename+" where email=? and password=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			if (rs.next()) {
				pw.println("{\"message\": \"Sign in successful\"}");
			} else {
				pw.print("{\"error\": \"Invalid email id or password\"}");

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
