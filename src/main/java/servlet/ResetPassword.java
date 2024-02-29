package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ResetPassword
 */
public class ResetPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con;
	private static final String FROM_EMAIL = "quadriaftab0@gmail.com"; // Replace with your email
	private static final String PASSWORD = "wdvnhjqhcmahxclw"; // Replace with your email password
	public boolean sendEmail(String to ,String from, String subject, String message) {
		boolean flag=false;
		//sending message logic

		//smtp properties
		Properties properties=new Properties();
		properties.put("mail.smtp.auth",true);
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.port","587");
		properties.put("mail.smtp.host","smtp.gmail.com");
//		String username="quadriaftab0@gmail.com";
//		String password="wdvnhjqhcmahxclw";
		Session session=Session.getInstance(properties,new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(FROM_EMAIL,PASSWORD);
			}
		});


		try {
			Message message1= new MimeMessage(session);
			message1.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message1.setFrom(new InternetAddress(from));
			message1.setSubject(subject);
			message1.setText(message);
			Transport.send(message1);
			flag=true;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ResetPassword() {
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

		String email = request.getParameter("email");
		String usertype = request.getParameter("usertype");
		String tablename = usertype;
		System.out.println("tablename: "+tablename);
		System.out.println("Email :" + email);
		con = (Connection) getServletContext().getAttribute("oracle");
		System.out.println(con);
		PrintWriter pw = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		String userpassword = getPassword(email, tablename,response);
		System.out.println("doPost: " + userpassword);
		if (userpassword.equals("NA")) {
			pw.println("{\"error\":\"Invalid Email id\"}");
		} else {
			String message="Your current password for the WasteManager websit is: "+"\""+userpassword+"\"";
			sendEmail("skaftab984@gmail.com", FROM_EMAIL, "WasteManager password reset email", message);
			System.out.println("Password sent to email");
			pw.println("{\"message\":\"Please check your email\"}");

		}

	}


	private String getPassword(String email, String tablename,HttpServletResponse response) {
		String password = "NA";
		con = (Connection) getServletContext().getAttribute("oracle");
		System.out.println(con);
		PreparedStatement pstmt1;
		try {
			pstmt1 = con.prepareCall("select password from " + tablename + " where email=?");
			pstmt1.setString(1, email);
			ResultSet rs;
			rs = pstmt1.executeQuery();
			if (rs.next()) {
				System.out.println("rs1");
				password = rs.getString(1);

			} else {
				System.out.println(rs.next() + ": value of resut set ");
				
			}
			rs.close();
			pstmt1.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			sendMessageOutput(e.toString(),response);
			e.printStackTrace();

		}
		System.out.println("validateEmailAndGetPassword: " + password);
		return password;
	}
	private void sendMessageOutput(String message,HttpServletResponse response) {
		try {
			PrintWriter pw=response.getWriter();
			pw.println("{\"message\":\"" + message + "\"}");
			System.out.println(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error");
//			e.printStackTrace();
		}
		
	}
	// Implement the logic to send a password reset email
	}




