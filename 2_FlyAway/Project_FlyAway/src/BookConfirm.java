

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.flyhigh.DBConnection;


@WebServlet("/BookConfirm")
public class BookConfirm extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public BookConfirm() { super(); }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		
		int seat = Integer.parseInt(request.getParameter("seats"));
		int flight_no = Integer.parseInt(request.getParameter("flightNumber"));
		String fname = request.getParameter("fname");
		String lname = request.getParameter("lname");
		String age = request.getParameter("age");
		String address = request.getParameter("Address");
		String phone = request.getParameter("Phone");
		
		InputStream in = getServletContext().getResourceAsStream("/WEB-INF/config.properties");
		Properties props = new Properties();
		String ticket;
		props.load(in);
		
		RequestDispatcher rd = request.getRequestDispatcher("searchResult.html");
		rd.include(request, response);
		
		try {
			DBConnection conn = new DBConnection( props.getProperty("url") ,props.getProperty("userid") , props.getProperty("password"));
			PreparedStatement ps = conn.getConnection().prepareStatement("Select * from flight where flight_no = ?");
			ps.setLong(1, flight_no);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
			{
				int available =rs.getInt("seats");
				if(seat > available) {
					out.println("<div><h3 class=err>Only " + available + " Seats are Available in this Flight!</h3></div>");
				}
				else if(seat <= available) {
					available = available - seat;
					PreparedStatement ps1 = conn.getConnection().prepareStatement("update flight set seats=? where flight_no=?");
					ps1.setInt(1, available);
					ps1.setInt(2, flight_no);
					int k = ps1.executeUpdate();
					if(k == 1)
					{
						out.println("<div><h3 class=successful>" + seat + " Seats Booked Successfully!</h3></div>");
						
						ticket = request.getParameter("flightNumber").concat(phone);
						out.println("<div><fieldset class=bookConfirmFieldset>"
						+ "<legend class=bookConfirmLegend><b> Flight Details </b></legend><table>"
						+ "<tr><td><label for=tno><b>Ticket No : </b></td><td>TK" + ticket + "</label></td></tr>"
						+ "<tr><td><label for=fno><b>Flight No : </b></td><td>" + flight_no + "</label></td></tr>"
						+ "<tr><td><label for=from><b>From City : </b></td><td>" + rs.getString("fromCity") + "</label></td></tr>"
						+ "<tr><td><label for=to><b>To City : </b></td><td>" + rs.getString("toCity") + "</label></td></tr>"
						+ "<tr><td><label for=dt><b>Date : </b></td><td>" + rs.getString("date") + "</label></td></tr>"
						+ "<tr><td><label for=time><b>Time : </b></td><td>" + rs.getString("time") + "</label></td></tr>"
						+ "<tr><td><label for=amt><b>Amount : </b></td><td>Rs. " + rs.getInt("price") + "</label></td></tr>"
						+ "</table></fieldset><br>"
						+ "<fieldset class=bookConfirmFieldset>"
						+ "<legend class=bookConfirmLegend><b>Passenger Details</b></legend><table>"
						+ "<tr><td><label><b>Name : </b></td><td>" + fname + " " + lname + "</label></td></tr>"
						+ "<tr><td><label><b>Age : </b></td><td>" + age + "</label></td></tr>"
						+ "<tr><td><label><b>Address : </b></td><td>" + address + "</label></td></tr>"
						+ "<tr><td><label><b>Phone : </b></td><td>" + phone + "</label></td></tr>"
						+ "</table></fieldset>");
						out.println("<div><h4 class=successful>-- THANK YOU FOR BOOKING WITH FLYAWAY!!! --</h4></div><br>");
						}
				}
			}
			else {
				out.println("<div><p1 class=err>Invalid Flight Number !</p1></div>");
			}
			
			ps.close();
			
			// insert Passenger details to passenger table.
			ps = conn.getConnection().prepareStatement("insert into passenger (first_name, last_name, age, address, phone, flight_no) values (?,?,?,?,?,? )");
			ps.setString(1, fname);
			ps.setString(2, lname);
			ps.setString(3, age);
			ps.setString(4, address);
			ps.setString(5,phone);
			ps.setInt(6, flight_no);
			ps.execute();
			
		}catch(ClassNotFoundException | SQLException e){
			e.printStackTrace();
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
