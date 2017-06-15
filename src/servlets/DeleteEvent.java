package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class DeleteEvent extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		
	}
	
	
	 public void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	
	 StringBuffer jb = new StringBuffer();
	 String line = null;
	 Connection connection = null; 
	 
	 try {
	    BufferedReader reader = request.getReader();
	    while ((line = reader.readLine()) != null)
	      jb.append(line);
	  } catch (Exception e) {  }
	    
	    Gson evGson = new Gson();
	    model.Event ev = evGson.fromJson(jb.toString(), model.Event.class);
	    
	    
	 try {
		Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(
                   "jdbc:mysql://141.19.96.199:3306/eic01DB"+"?autoReconnect=true&useSSL=false", "eic01-mysql", "Pptbcefkxtj1");
        PreparedStatement pstmt = connection.prepareStatement("delete from events where evID=?");
		pstmt.setInt(1, ev.getId());
		System.out.println(ev.getId());
		pstmt.executeUpdate();	
			
		connection.commit();
		pstmt.close();
	} catch (Exception e) {
		// TODO: handle exception
	}
}

}
