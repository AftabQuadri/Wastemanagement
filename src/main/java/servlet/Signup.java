package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Signup
 */
public class Signup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection con;
	//creating connection reference

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Signup() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		ServletContext sc=config.getServletContext();
		String driver=sc.getInitParameter("driver");
		String url=sc.getInitParameter("url");
		String username=sc.getInitParameter("username");
		String password=sc.getInitParameter("password");
		int receiverid=101;

			try {
				Class.forName(driver);
				con=DriverManager.getConnection(url,username,password);
				sc.setAttribute("oracle", con);
				sc.setAttribute("receiverid", receiverid);
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}

	/**
	 * @see Servlet#destroy()
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String name=request.getParameter("name");
		String email=request.getParameter("email");
		String password=request.getParameter("password");
		//accept all user entered fields
		String tablename="receiver";//decide which table to operate on donor or receipient
		ServletContext context=getServletContext();
		try {
			PreparedStatement pstmt=con.prepareStatement("select ? from ?");
			pstmt.setString(1, email);
			pstmt.setString(2, tablename);
			ResultSet rs=pstmt.executeQuery();
			PrintWriter pw=response.getWriter();

			if(rs.next()) {
				pw.println("Email already registered");
			}
			else {
				pstmt=con.prepareStatement("insert into ? values(?,?,?,?)");
				int id=(int)context.getAttribute(tablename+"id")+1;
				context.setAttribute(tablename+"id", id);
				pstmt.setString(1, tablename);
				pstmt.setInt(2, id);
				pstmt.setString(3, name);
				pstmt.setString(4, email);
				pstmt.setString(5, password);
				pw.println("Registration successful taking to sign in page ");
				response.sendRedirect("signin.html");
				}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
