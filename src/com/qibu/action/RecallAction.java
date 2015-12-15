package com.qibu.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qibu.database.TJDBCManager;

/**
 * Servlet implementation class RecallAction
 */
public class RecallAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RecallAction() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String uuid=request.getParameter("id");
		 System.out.println(uuid);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		Connection connection = null;
		request.setCharacterEncoding("UTF-8");
		try{
			connection = TJDBCManager.getInstance().getConnection();
			
			 Statement stmt;
			 stmt = connection.createStatement();
			 String uuid=request.getParameter("id");
			 String userid=null;
			 System.out.print(uuid);
			 String qq="select userid from user_info where uuid='"+uuid+"'";
			 ResultSet rs = stmt.executeQuery(qq);
				if (rs.next()) {
					userid = rs.getString("userid");
					request.setAttribute("userid", userid);
					RequestDispatcher rd = request.getRequestDispatcher("zpass.jsp");
					rd.forward(request, response);
				}
				out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
				out.println("<HTML>");
				out.println("  <HEAD><TITLE>召回密码</TITLE></HEAD>");
				out.println("  <BODY>");
				out.print("    This is ");
				out.print(this.getClass());
				out.println(", using the GET method");
				out.println("</HTML>");
				out.flush();
				out.close();
		}
		catch(Exception e){
			out.println(e);
		}finally{
		if(connection!=null){
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
		}
}
