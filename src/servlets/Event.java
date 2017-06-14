package servlets;

import java.io.BufferedReader;

import org.json.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Event extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	 public void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
		 
	     Connection connection = null;
	     
	     List<String> eventList = new ArrayList<String>();
			
				
			try {
				 Class.forName("com.mysql.jdbc.Driver");
		         connection = DriverManager.getConnection(
		                    "jdbc:mysql://141.19.96.199:3306/eic01DB"+"?autoReconnect=true&useSSL=false", "eic01-mysql", "Pptbcefkxtj1");
		         
				 PreparedStatement pstmt = connection.prepareStatement("select evID, title, location, description, faculty, eventDate, evTime from events");				
				 ResultSet rs = pstmt.executeQuery();
									
					while (rs.next()) {
						model.Event ev = new model.Event();
						ev.setTitle(rs.getString("title"));
						ev.setLocation(rs.getString("location"));
						ev.setDescription(rs.getString("description"));
						ev.setFaculty(rs.getString("faculty"));
						ev.setEventDate(rs.getString("eventDate"));
						ev.setTime(rs.getString("evTime"));
						ev.setId(rs.getInt("evID"));
						
						Gson gson = new Gson();
						String evObj = gson.toJson(ev);
						eventList.add(evObj);
					}			
				
				response.setContentType("application/json");
	            response.setCharacterEncoding("utf-8");
	            PrintWriter out = response.getWriter();
	            
	            System.out.println(eventList);
                out.print(eventList);
               
		         
		        /*
		       // response.setContentType("application/json");
		       // response.setCharacterEncoding("utf-8");
		        PrintWriter out = response.getWriter();
		        
		        String json = "Hallo";
		        out.print(json);
		        */
                out.flush();
                out.close();
				
			} catch (Exception e) {
				
			} finally {	
				
			}	     
	        
	 }
	 
	 public void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
		 
		 StringBuffer jb = new StringBuffer();
		 String line = null;
		 Connection connection = null; 
		 PreparedStatement pstmt = null;
		 
		 try {
		    BufferedReader reader = request.getReader();
		    while ((line = reader.readLine()) != null)
		      jb.append(line);
		  } catch (Exception e) {  }

		 
		    System.out.println(jb.toString());;
		    
		    Gson evGson = new Gson();
		    model.Event ev = evGson.fromJson(jb.toString(), model.Event.class);
		    
		 
		    
		 try {
			 Class.forName("com.mysql.jdbc.Driver");
	         connection = DriverManager.getConnection(
	                    "jdbc:mysql://141.19.96.199:3306/eic01DB"+"?autoReconnect=true&useSSL=false", "eic01-mysql", "Pptbcefkxtj1");
	        
	         
	         if (ev.getId() != 0) {
	        	 pstmt = connection.prepareStatement("UPDATE events set title=?, location=?, description=?, faculty=?, eventDate=?, evCreationDate=?, evTime=? where evID=?");
	        	 pstmt.setInt(8, ev.getId());
	        	 
	         }
	         else {
		         pstmt = connection.prepareStatement("INSERT INTO events (title, location, description, faculty, eventDate, evCreationDate, evTime) VALUES (?,?,?,?,?,?,?)");
	         }
	         
				pstmt.setString(1, ev.getTitle());
				pstmt.setString(2, ev.getLocation());
				pstmt.setString(3, ev.getDescription());
				pstmt.setString(4, ev.getFaculty());
				pstmt.setString(5, ev.getEventDate());
				Date date = java.sql.Date.valueOf(java.time.LocalDate.now()); 
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String creationDate = df.format(date);
				pstmt.setString(6, creationDate);
				pstmt.setString(7, ev.getTime());
				pstmt.executeUpdate();
				
				connection.commit();
				pstmt.close();
				
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			
		}
		    
		    
		    
		 
		  
		  
	 }

}
