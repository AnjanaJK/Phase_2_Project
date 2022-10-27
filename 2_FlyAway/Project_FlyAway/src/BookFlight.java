

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/bookflight")
public class BookFlight extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public BookFlight() { super(); }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		int flight_no = Integer.parseInt(request.getParameter("flightNumber"));
		String fromCity = request.getParameter("fromCity");
		String toCity = request.getParameter("toCity");
		String date = request.getParameter("date");
		String time = request.getParameter("time");
		int seat = 1;
		
		RequestDispatcher rd = request.getRequestDispatcher("searchResult.html");
		rd.include(request, response);
		
		out.println("<div><form action='bookconfirm' method='post'>"
				+ "<fieldset class=bffieldset>" + "<legend class=bflegend>Booking Information</legend><table>"
				+ "<tr><td><label for=fno><b>Flight No : </b></td><td>"+flight_no+"</label></td></tr>"
				+ "<tr><td><label for=from><b>From : </b></td><td>"+fromCity+"</label></td></tr>"
				+ "<tr><td><label for=to><b>To : </b></td><td>"+toCity+"</label></td></tr>"
				+ "<tr><td><label for=dt><b>Date : </b></td><td>"+date+"</label></td></tr>"
				+ "<tr><td><label for=time><b>Time : </b></td><td>"+time+"</label></td></tr>"
				+ "<tr><td><label for=seats><b>No of Seats : </b></td><td><input type='text' name='seats' value='"+seat+"'</label></td></tr>"
				+ "</table></fieldset><br>"
				+ "<fieldset class=bffieldset>"+ "<legend class=bflegend>Enter Passenger Details</legend><table>"
				+ "<tr><td><label for=pname><b>First Name : </b></td><td></label><input type='text' name='fname' ></td></tr>"
				+ "<tr><td><label for=pname><b>Last Name : </b></td><td></label><input type='text' name='lname' ></td></tr>"
				+ "<tr><td><label for=pname><b>Age : </b></td><td></label><input type='text' name='age' ></td></tr>"
				+ "<tr><td><label for=address><b>Address : </b></td><td></label><input type='text' name='Address' ></td></tr>"
				+ "<tr><td><label for=phone><b>Phone : </b></td><td></label><input type='text' name='Phone' ></td></tr>"
				+ "<tr><td><input type='hidden' name='flightNumber' value='"+flight_no+"'></td></tr>"
				+ "</table></fieldset></div>"
				+ "<br><br><div><p1><input type='submit' value='Pay & Book'></p1></div>"			
				+ "</form>");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
