package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Inet4Address;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import model.Fachschaft;


/**
 * Servlet implementation class Fachschaften
 */
@WebServlet("/Fachschaften")
public class Fachschaften extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Fachschaften() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
		
		response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out
                .println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        out.println("<HTML>");
        out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
        out.println("  <BODY>");
        out.print("    This is ");
        out.println(Inet4Address.getLocalHost().getHostAddress());
        out.print(this.getClass());
        out.println(", using the GET method");
        out.println("  </BODY>");
        out.println("</HTML>");
        out.flush();
        out.close();
    
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("Incomming connection in Fachschaften-Servlet!");
        Connection connection = null;
        Statement statment = null;
        ResultSet result;


        try {
            // Connect to database retrieve user credentials
            Class.forName("com.mysql.jdbc.Driver");
           
            connection = DriverManager.getConnection(
                    "jdbc:mysql://141.19.96.199:3306/eic01DB", "eic01-mysql", "Pptbcefkxtj1");
            statment = connection.createStatement();
            result = statment
                    .executeQuery("SELECT fachschaften_id, fachschaften_name,  faculty, fsSprecher, member, adress, email, facebook, website FROM fachschaften;");
            PrintWriter out = response.getWriter();
            List<Fachschaft> fsList = new ArrayList<>();
            
            while (result.next()){
            
            	
            	Fachschaft fachschaft = new Fachschaft();
                fachschaft.setId(result.getLong("fachschaften_id"));
                fachschaft.setName(result.getString("fachschaften_name"));
                fachschaft.setFaculty(result.getString("faculty"));
                fachschaft.setFsSprecher(result.getString("fsSprecher"));
                fachschaft.setMember(result.getString("member"));
                fachschaft.setAdress(result.getString("adress"));
                fachschaft.setEmail(result.getString("email"));
                fachschaft.setFacebook(result.getString("facebook"));                
                fachschaft.setWebsite(result.getString("website"));                
                
 

                fsList.add(fachschaft);
                 
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Gson gson = new Gson();
            
            String json = gson.toJson(fsList);
            System.out.println(json);
        	out.print(json);            
            out.flush();
            out.close();
            } catch (SQLException e) {
        	System.out.println("Error while asking for user-credentials: " + e.getMessage());
            
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            PrintWriter out = response.getWriter();

              //create Json Object
              String json = new Gson().toJson("");
                // finally output the json string       
             out.print(json);
            
            out.flush();
            out.close();
            // jlog.info("SQL-Error: " + e.getMessage() );
        } catch (ClassNotFoundException e) {
        	System.out.println("MysqlDriver not found: " + e.getMessage());
                e.printStackTrace();
        } finally {
            try {
                if (statment != null) {
                    statment.close();
                    statment = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException e) {
            }
        }
	}

}
