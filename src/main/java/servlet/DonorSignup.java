package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DonorSignup
 */
public class DonorSignup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection con;

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		ServletContext sc = config.getServletContext();
		String driver = sc.getInitParameter("driver");
		String url = sc.getInitParameter("url");
		String username = sc.getInitParameter("username");
		String password = sc.getInitParameter("password");

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			sc.setAttribute("oracle", con);

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void destroy() {
		try {
			if (con != null && !con.isClosed()) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DonorSignup() {
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
		PrintWriter pw = response.getWriter();
		String name = request.getParameter("donorname");
		String email = request.getParameter("donoremail");
		String password = request.getParameter("donorpassword");
		System.out.println(con);
		String usertype=request.getParameter("usertype");
		System.out.println("donorsignup usertype: "+usertype);
		String tablename=usertype;

		PreparedStatement pstmt;
		ResultSet rs;
		// accept all user entered fields
		String sql = "select * from "+tablename+" where email=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			if (rs.next()) {
				// if user email are already present in the table
				pw.println("{\"error\": \"Email already registered\"}");
			} else {
				pstmt = con.prepareStatement("insert into "+tablename+"(name,email,password) values(?,?,?)");
				pstmt.setString(1, name);
				pstmt.setString(2, email);
				pstmt.setString(3, password);
				System.out.println(name + " " + email + " " + password);

				int result = pstmt.executeUpdate();
				if (result > 0) {
					pw.println("{\"message\": \"Registration successful. Redirecting to sign-in page in \"}");
				} else {
					pw.println("{\"error\": \"Something went wrong\"}");
				}

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
