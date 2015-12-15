package com.qibu.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


import com.qibu.database.TJDBCManager;
import com.qibu.service.TUserService;

import com.qibu.bean.TUser;

/**
 * Servlet implementation class UserAction
 */
public class UserAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserAction() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection connection = null;
		connection = TJDBCManager.getInstance().getConnection();
		request.setCharacterEncoding("UTF-8");
		try{
		// 判断操作类型
		if(request.getParameter("opeType").equals("login")){
			
			
			boolean result = false;
			
			String Account_Num = URLDecoder.decode(URLDecoder.decode(request.getParameter("name"), "utf-8"), "utf-8"); 
			String Pass_word = URLDecoder.decode(URLDecoder.decode(request.getParameter("pwd"), "utf-8"), "utf-8"); 
			
			TUser user = new TUser();
			user.setAccount_Num(Account_Num);
			user.setPass_word(Pass_word);
			user.setSessionId(request.getSession().getId());
			
			
			TUserService userService = new TUserService();
			
			try {
				result = userService.login(user, connection);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
			response.setCharacterEncoding("UTF-8");
			JSONObject jsonObject = new JSONObject();
			if(result){
				jsonObject.put("code", "1");
				jsonObject.put("msg", "登陆成功");
			}else{
				jsonObject.put("code", "-1");
				jsonObject.put("msg", "登陆失败");
			}
		    response.getWriter().print(jsonObject); 
			
		}else if(request.getParameter("opeType").equals("recall")){
			String E_mail = request.getParameter("email"); 
			
			TUser user = new TUser();
			user.setAccount_Num("ganshenme");
			user.setE_mail(E_mail);
			
			TUserService userService = new TUserService();
			
			try {
				boolean pRes = userService.recall(user, connection);
				response.setCharacterEncoding("UTF-8");
				JSONObject jsonObject = new JSONObject();
				if(pRes){
					jsonObject.put("code", "1");
					jsonObject.put("msg", "发送成功");
				}else{
					jsonObject.put("code", "-1");
					jsonObject.put("msg", "发送失败");
				}
			    response.getWriter().print(jsonObject); 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		}catch(Exception e){
			System.out.println(e);
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
