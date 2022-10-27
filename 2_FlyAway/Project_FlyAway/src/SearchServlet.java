
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

@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SearchServlet() { super(); }

    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
        out.println("<html><body>");
        
        String from = request.getParameter("fromCity");
        String to = request.getParameter("toCity");
        String dt = request.getParameter("date");
        if (from.isEmpty() || to.isEmpty() || dt.isEmpty()) {
        	RequestDispatcher rd = request.getRequestDispatcher("index.html");
			rd.include(request, response);
        	out.println("<br><div><h3><b style=color:red> Please fill the required details. </b></h3></div>");
        }
        
        else {
	        try
			{
	            //Load the DB Connection configuration parameters.
	            InputStream in = getServletContext().getResourceAsStream("/WEB-INF/config.properties");
	            Properties props = new Properties();
	            props.load(in); // We have added URL, user, password to the Props object.
	            
	            //Create the DBConnection object here.
	            DBConnection conn = new DBConnection(props.getProperty("url"), props.getProperty("userid"), props.getProperty("password"));
	            PreparedStatement ps = conn.getConnection().prepareStatement("Select * from flight where fromCity=? AND toCity=? AND date=?");
	            ps.setString(1, request.getParameter("fromCity"));
				ps.setString(2, request.getParameter("toCity"));
				ps.setString(3, request.getParameter("date"));
				ResultSet rs = ps.executeQuery();
				
				if(rs.next())  {
					RequestDispatcher rd = request.getRequestDispatcher("searchResult.html");
					rd.include(request, response);
					out.println("<h1 style=color:#5c2a68>FlyAway Airline Booking Portal<br><br></h1>");
					out.println("<div><h1 style=text-decoration-line:overline>Available Flight Details</h1></div><br>");
					int flightNumber;
					String fromCity;
					String toCity;
					String date;
					String time;
					out.println("<div><table border=2 cellspacing=5 cellpadding=5 style=border-collapse:collapse>"
							+ "<tr style=background-color:#ddbde5><th>Flight Number</th>"+ "<th>From Airport</th><th>To Airport</th><th>Seats Available</th><th>Fare (INR)</th><th>Date</th><th>Time</th></tr>");
					
					do {
						flightNumber = rs.getInt("flight_no");
						fromCity = rs.getString("fromCity");
						toCity = rs.getString("toCity");
						date = rs.getString("date");
						time = rs.getString("time");
					
						// printing the values from the result set (table).
						out.println("" + "<tr> " + "" + "<td>"+ flightNumber + "</td>"
							+ "<td>"+ fromCity +"</td>" + "<td>" + toCity + "</td>"	+ "<td>"+ rs.getInt("seats") + "</td>"
							+ "<td>" + rs.getInt("price")+" RS</td>" + "<td>"+rs.getString("date")+"</td>"	+ "<td>"+rs.getString("time")+"</td>"
							+ "<td><i><b><a href='bookflight?flightNumber="+flightNumber+"&fromCity="+fromCity+"&toCity="+toCity+"&date="+date+"&time="+time+"'><div class='red'>Book Now</div></a></b></i></td></tr>"
						);
					}while(rs.next());
					out.println("</table></div>");
				}else {
					RequestDispatcher rd = request.getRequestDispatcher("index.html");
					rd.include(request, response);
					out.println("<br><div><h3><b style=color:red> Sorry!! No Flights Available </b></h3></div>");
				}
	            //conn.closeConnection();
	            //out.println("</body></html>");
	                       
			}catch(ClassNotFoundException | SQLException e) // Piping Exceptions - JDK 1.7
			{
				e.printStackTrace();
			}
        }
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
