package servlets;
import java.io.IOException;
import model.User;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Login extends HttpServlet {

    private static final long serialVersionUID = 1084936780013849522L;
    //private static Logger jlog = Logger.getLogger(Login.class);

    /**
     * Constructor of the object.
     */
    public Login() {
        super();
    }

    /**
     * The doGet method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to get.
     *
     * @param request
     *            the request send by the client to the server
     * @param response
     *            the response send by the server to the client
     * @throws ServletException
     *             if an error occurred
     * @throws IOException
     *             if an error occurred
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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
     * The doPost method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to
     * post.
     *
     * @param request
     *            the request send by the client to the server
     * @param response
     *            the response send by the server to the client
     * @throws ServletException
     *             if an error occurred
     * @throws IOException
     *             if an error occurred
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	// jlog.info("Incomming connection at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));	
    	
    	System.out.println("Incomming connection in Login-Servlet!");
        Connection connection = null;
        Statement statment = null;
        ResultSet result;

        String UserEmail = request.getParameter("email");
        String Password = request.getParameter("password");

        try {
            // Connect to database retrieve user credentials
            Class.forName("com.mysql.jdbc.Driver");
           
            connection = DriverManager.getConnection(
                    "jdbc:mysql://141.19.96.199:3306/eic01DB", "eic01-mysql", "Pptbcefkxtj1");
            statment = connection.createStatement();
            result = statment
                    .executeQuery("SELECT user_id, username, password, permission FROM users WHERE email='"
                            + UserEmail + "';");
            result.next();
            if (result.getObject("password").toString().compareTo(Password) == 0) {

            	User user = new User();
                user.setId(result.getLong("user_id"));
                user.setName(result.getString("username"));
                user.setEmail(UserEmail);
                //user.setPassword(result.getString("password"));
                user.setRole(result.getString("permission"));                
                
            	response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                PrintWriter out = response.getWriter();

                  //create Json Object
                String json = new Gson().toJson(user);
                    // finally output the json string     
                System.out.println(json);
                 out.print(json);
                
                out.flush();
                out.close();
            } else {
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                PrintWriter out = response.getWriter();

                  //create Json Object
                  String json = new Gson().toJson("");
                    // finally output the json string       
                 out.print(json);
                
                out.flush();
                out.close();
                
            }
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