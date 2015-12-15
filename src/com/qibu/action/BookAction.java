package com.qibu.action;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.qibu.bean.TBook;
import com.qibu.database.TJDBCManager;
import com.qibu.service.TBookService;

/**
 * Servlet implementation class BookAction
 */
public class BookAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BookAction() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		if(request.getParameter("opeType").equals("add")){
			boolean result;
			try {
				// 获取数据
				String bookName = URLDecoder.decode(URLDecoder.decode(request.getParameter("bookName"), "utf-8"), "utf-8"); 
				String Type_ID = request.getParameter("bookType");
				String Type_Name = URLDecoder.decode(URLDecoder.decode(request.getParameter("typeName"), "utf-8"), "utf-8"); 
				String bookHeight = request.getParameter("bookHeight");
				String bookCover = request.getParameter("bookCover");
				// 正则匹配一下&字符
				Pattern pattern = Pattern.compile("[*]");
				Matcher matcher = pattern.matcher(request.getParameter("bookDown"));
				StringBuffer sbr = new StringBuffer();
				while (matcher.find()) {
				    matcher.appendReplacement(sbr, "&");
				}
				matcher.appendTail(sbr);
				String bookDown = sbr.toString();
				String bookBrief = URLDecoder.decode(URLDecoder.decode(request.getParameter("bookBrief"), "utf-8"), "utf-8"); 
				
				TBook book = new TBook();
				
				book.setBook_Name(bookName);
				book.setType_ID(Integer.parseInt(Type_ID));
				book.setType_Name(Type_Name);
				book.setBook_Height(Integer.parseInt(bookHeight));
				System.out.println("************************");
				System.out.println("************************");
				System.out.println("************************");
				System.out.println("************************");
				System.out.println(bookCover);
				book.setBook_Cover(bookCover);
				book.setDownload_Link(bookDown);
				book.setBook_Abstract(bookBrief);
				
				TBookService bookService = new TBookService();
				
				result = bookService.addBook(book, connection);
				
				response.setCharacterEncoding("UTF-8");
				JSONObject jsonObject = new JSONObject();
				if(result){
					jsonObject.put("code", "1");
					jsonObject.put("msg", "添加成功");
				}else{
					jsonObject.put("code", "-1");
					jsonObject.put("msg", "添加失败");
				}
			    response.getWriter().print(jsonObject); 
			    
			} catch (Exception e) {
				e.printStackTrace();
			
			}
			
		}else if(request.getParameter("opeType").equals("queryOne")){
			try {
				// 获取数据
				String bookId = request.getParameter("bookId");
				
				TBook book = new TBook();
				
				book.setBook_ID(Integer.parseInt(bookId));
				
				TBookService bookService = new TBookService();
				
				List<TBook> listBook = bookService.queryOneBook(book, connection);
				
				JSONArray jsonArray = new JSONArray();
				for(int i=0; i<listBook.size(); i++){
					JSONObject bookJson = new JSONObject();
					TBook pBook = listBook.get(i);
					bookJson.put("Book_ID", pBook.getBook_ID());
					bookJson.put("Book_Name", pBook.getBook_Name());
					bookJson.put("Book_Abstract", pBook.getBook_Abstract());
					bookJson.put("Book_Height", pBook.getBook_Height());
					bookJson.put("Download_Link", pBook.getDownload_Link());
					bookJson.put("Book_Cover", pBook.getBook_Cover());
					bookJson.put("Recommend_Num", pBook.getRecommend_Num());
					bookJson.put("Type_ID", pBook.getType_ID());
					bookJson.put("Type_Name", pBook.getType_Name());
					bookJson.put("Query_Num", pBook.getQuery_Num());
					bookJson.put("User_ID", pBook.getUser_ID());
					jsonArray.add(bookJson);
				}
				
				response.setCharacterEncoding("UTF-8");
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("bookData", jsonArray);
			    response.getWriter().print(jsonObject); 
			    
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(request.getParameter("opeType").equals("queryLength")){
			int typeId = Integer.parseInt(request.getParameter("typeId"));  // 所属分类
			
			TBookService bookService = new TBookService();
			try {
				int length = bookService.queryBookLength(typeId, connection);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("length", length);
			    response.getWriter().print(jsonObject); 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}else if(request.getParameter("opeType").equals("queryPage")){
			int pageId = Integer.parseInt(request.getParameter("pageId"));  // 页数
			int pageCount = Integer.parseInt(request.getParameter("pageCount"));  // 每页个数
			int typeId = Integer.parseInt(request.getParameter("typeId"));  // 所属分类
			
			TBookService bookService = new TBookService();
			
			List<TBook> listBook = null;
			try {
				listBook = bookService.pageQueryBook(pageId, pageCount, typeId, connection);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			JSONArray jsonArray = new JSONArray();
			for(int i=0; i<listBook.size(); i++){
				JSONObject bookJson = new JSONObject();
				TBook pBook = listBook.get(i);
				bookJson.put("Book_ID", pBook.getBook_ID());
				bookJson.put("Book_Name", pBook.getBook_Name());
				bookJson.put("Book_Abstract", pBook.getBook_Abstract());
				bookJson.put("Book_Height", pBook.getBook_Height());
				bookJson.put("Download_Link", pBook.getDownload_Link());
				bookJson.put("Book_Cover", pBook.getBook_Cover());
				bookJson.put("Recommend_Num", pBook.getRecommend_Num());
				bookJson.put("Type_ID", pBook.getType_ID());
				bookJson.put("Type_Name", pBook.getType_Name());
				bookJson.put("Query_Num", pBook.getQuery_Num());
				bookJson.put("User_ID", pBook.getUser_ID());
				jsonArray.add(bookJson);
			}
			
			response.setCharacterEncoding("UTF-8");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("bookData", jsonArray);
		    response.getWriter().print(jsonObject); 
		}else if(request.getParameter("opeType").equals("del")){
			String idArr = request.getParameter("bookId"); 
			
			TBookService bookService = new TBookService();
			
			response.setCharacterEncoding("UTF-8");
			JSONObject jsonObject = new JSONObject();
			
			boolean result = true;
			try {
				result = bookService.delBook(idArr, connection);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(result){
				jsonObject.put("code", "1");
				jsonObject.put("msg", "删除成功");
			}else{
				jsonObject.put("code", "-1");
				jsonObject.put("msg", "删除失败");
			}
			response.getWriter().print(jsonObject); 
		}else if(request.getParameter("opeType").equals("update")){
			boolean result;
			try {
				// 获取数据
				int bookId = Integer.parseInt(request.getParameter("bookId")); 
				
				String bookName = URLDecoder.decode(URLDecoder.decode(request.getParameter("bookName"), "utf-8"), "utf-8"); 
				String Type_ID = request.getParameter("bookType");
				String Type_Name = URLDecoder.decode(URLDecoder.decode(request.getParameter("typeName"), "utf-8"), "utf-8"); 
				String bookHeight = request.getParameter("bookHeight");
				String bookCover = request.getParameter("bookCover");
				// 正则匹配一下&字符
				Pattern pattern = Pattern.compile("[*]");
				Matcher matcher = pattern.matcher(request.getParameter("bookDown"));
				StringBuffer sbr = new StringBuffer();
				while (matcher.find()) {
				    matcher.appendReplacement(sbr, "&");
				}
				matcher.appendTail(sbr);
				String bookDown = sbr.toString();
				String bookBrief = URLDecoder.decode(URLDecoder.decode(request.getParameter("bookBrief"), "utf-8"), "utf-8"); 
				
				
				TBook book = new TBook();
				
				
				book.setBook_ID(bookId);
				book.setBook_Name(bookName);
				book.setType_ID(Integer.parseInt(Type_ID));
				book.setType_Name(Type_Name);
				book.setBook_Height(Integer.parseInt(bookHeight));
				book.setDownload_Link(bookDown);
				book.setBook_Cover(bookCover);
				book.setBook_Abstract(bookBrief);
				
				TBookService bookService = new TBookService();
				
				result = bookService.updateBook(book, connection);
				
				response.setCharacterEncoding("UTF-8");
				JSONObject jsonObject = new JSONObject();
				if(result){
					jsonObject.put("code", "1");
					jsonObject.put("msg", "修改成功");
				}else{
					jsonObject.put("code", "-1");
					jsonObject.put("msg", "修改失败");
				}
			    response.getWriter().print(jsonObject); 
			    
			} catch (Exception e) {
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
					e.printStackTrace();
				}
			}
			
		}
	}

}
