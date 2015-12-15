package com.qibu.action;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.qibu.bean.TBook;
import com.qibu.bean.TClassify;
import com.qibu.database.TJDBCManager;
import com.qibu.service.TBookService;
import com.qibu.service.TClassifyService;

public class ClassifyAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ClassifyAction() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
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
				String fCode = request.getParameter("fCode");
				String sortName = URLDecoder.decode(URLDecoder.decode(request.getParameter("sortName"), "utf-8"), "utf-8"); 
				
				
				TClassify classify = new TClassify();
				
				classify.setSort_Name(sortName);
				classify.setF_Code(Integer.parseInt(fCode));
				
				TClassifyService classifyService = new TClassifyService();
				
				result = classifyService.addClassify(classify, connection);
				
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
			
		}else if(request.getParameter("opeType").equals("queryLength")){
			int fCode = Integer.parseInt(request.getParameter("fCode"));  // 所属分类
			
			TClassifyService classifyService = new TClassifyService();
			try {
				int length = classifyService.queryClassifyLength(fCode, connection);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("length", length);
			    response.getWriter().print(jsonObject); 
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}else if(request.getParameter("opeType").equals("queryPage")){
			int pageId = Integer.parseInt(request.getParameter("pageId"));  // 页数
			int pageCount = Integer.parseInt(request.getParameter("pageCount"));  // 每页个数
			int fCode = Integer.parseInt(request.getParameter("fCode"));  // 所属分类
			
			TClassifyService classifyService = new TClassifyService();
			
			List<TClassify> listClassify = null;
			try {
				listClassify = classifyService.pageQueryBook(pageId, pageCount, fCode, connection);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			JSONArray jsonArray = new JSONArray();
			for(int i=0; i<listClassify.size(); i++){
				JSONObject classifyJson = new JSONObject();
				TClassify pClassify = listClassify.get(i);
				classifyJson.put("Sort_ID", pClassify.getSort_ID());
				classifyJson.put("Sort_Name", pClassify.getSort_Name());
				jsonArray.add(classifyJson);
			}
			
			response.setCharacterEncoding("UTF-8");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("classifyData", jsonArray);
		    response.getWriter().print(jsonObject); 
		}else if(request.getParameter("opeType").equals("del")){
			String idArr = request.getParameter("classifyId"); 
			
			TClassifyService classifyService = new TClassifyService();
			
			response.setCharacterEncoding("UTF-8");
			JSONObject jsonObject = new JSONObject();
			
			boolean result = true;
			try {
				result = classifyService.delClassify(idArr, connection);
			} catch (Exception e) {
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
				int classifyId = Integer.parseInt(request.getParameter("classifyId")); 
				String classifyName = URLDecoder.decode(URLDecoder.decode(request.getParameter("classifyName"), "utf-8"), "utf-8"); 
				
				int fCode = Integer.parseInt(request.getParameter("fCode"));
				
				TClassify classify = new TClassify();
				
				classify.setSort_ID(classifyId);
				classify.setSort_Name(classifyName);
				classify.setF_Code(fCode);
				
				TClassifyService classifyService = new TClassifyService();
				
				result = classifyService.updateClassify(classify, connection);
				
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
