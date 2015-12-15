package com.qibu.action;

import java.io.IOException;
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
import com.qibu.bean.TNote;
import com.qibu.database.TJDBCManager;
import com.qibu.service.TBookService;
import com.qibu.service.TNoteService;

/**
 * Servlet implementation class NoteAction
 */
public class NoteAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NoteAction() {
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
		if(request.getParameter("opeType").equals("add")){
			boolean result;
			try {
				
				String Article_Title = request.getParameter("noteTitle");
				String Article_Record = request.getParameter("content");
				String Article_Text = request.getParameter("contentText");
				int Sort_ID = Integer.parseInt(request.getParameter("fCode")); 
				int Type_ID = Integer.parseInt(request.getParameter("typeId")); 
				String Type_Name = request.getParameter("typeName");
				String Cover_Img = request.getParameter("coverImg");
				String Aarticle_Label = request.getParameter("label");
				
				TNote note = new TNote();
				
				note.setArticle_Title(Article_Title);
				note.setArticle_Record(Article_Record);
				note.setArticle_Text(Article_Text);
				note.setSort_ID(Sort_ID);
				note.setType_ID(Type_ID);
				note.setType_Name(Type_Name);
				note.setCover_Img(Cover_Img);
				note.setAarticle_Label(Aarticle_Label);
				
				TNoteService noteService = new TNoteService();
				
				result = noteService.addNote(note, connection);
				
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
			}finally{
				try {
					connection.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}else if(request.getParameter("opeType").equals("queryOne")){
			try {
				// 获取数据
				String noteId = request.getParameter("noteId");
				
				TNote note = new TNote();
				
				note.setArticle_ID(Integer.parseInt(noteId));
				
				TNoteService noteService = new TNoteService();
				
				List<TNote> listNote = noteService.queryOneNote(note, connection);
				
				JSONArray jsonArray = new JSONArray();
				for(int i=0; i<listNote.size(); i++){
					JSONObject noteJson = new JSONObject();
					TNote pNote = listNote.get(i);
					noteJson.put("Article_ID", pNote.getArticle_ID());
					noteJson.put("Article_Title", pNote.getArticle_Title());
					noteJson.put("Article_Date", pNote.getArticle_Date());
					noteJson.put("Article_Record", pNote.getArticle_Record());
					noteJson.put("Recommend_Num", pNote.getRecommend_Num());
					noteJson.put("Aarticle_Label", pNote.getAarticle_Label());
					noteJson.put("Type_ID", pNote.getType_ID());
					noteJson.put("Type_Name", pNote.getType_Name());
					noteJson.put("Query_Num", pNote.getQuery_Num());
					noteJson.put("User_ID", pNote.getUser_ID());
					noteJson.put("Sort_ID", pNote.getSort_ID());
					noteJson.put("Cover_Img", pNote.getCover_Img());
					noteJson.put("Article_Text", pNote.getArticle_Text());
					
					jsonArray.add(noteJson);
				}
				
				response.setCharacterEncoding("UTF-8");
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("noteData", jsonArray);
			    response.getWriter().print(jsonObject); 
			    
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(request.getParameter("opeType").equals("queryLength")){
			int typeId = Integer.parseInt(request.getParameter("typeId"));  // 所属分类
			
			TNoteService noteService = new TNoteService();
			try {
				int length = noteService.queryNoteLength(typeId, connection);
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
			int contentLength = Integer.parseInt(request.getParameter("contentLength"));  // 带有标签的长度
			int textLength = Integer.parseInt(request.getParameter("textLength"));  // 纯文本的长度
			
			TNoteService noteService = new TNoteService();
			
			List<TNote> listNote = null;
			try {
				listNote = noteService.pageQueryNote(pageId, pageCount, typeId, connection);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			JSONArray jsonArray = new JSONArray();
			for(int i=0; i<listNote.size(); i++){
				JSONObject noteJson = new JSONObject();
				TNote pNote = listNote.get(i);
				int cLength = 0;
				int tLength = 0;
				if(contentLength > pNote.getArticle_Record().length()){
					cLength = pNote.getArticle_Record().length();
				}else{
					cLength = contentLength;
				}
				
				if(textLength > pNote.getArticle_Text().length()){
					tLength = pNote.getArticle_Text().length();
				}else{
					tLength = textLength;
				}
				
				noteJson.put("Article_ID", pNote.getArticle_ID());
				noteJson.put("Article_Title", pNote.getArticle_Title());
				noteJson.put("Article_Date", pNote.getArticle_Date());
				noteJson.put("Article_Record", pNote.getArticle_Record().substring(0,cLength));
				noteJson.put("Recommend_Num", pNote.getRecommend_Num());
				noteJson.put("Aarticle_Label", pNote.getAarticle_Label());
				noteJson.put("Type_ID", pNote.getType_ID());
				noteJson.put("Type_Name", pNote.getType_Name());
				noteJson.put("Query_Num", pNote.getQuery_Num());
				noteJson.put("User_ID", pNote.getUser_ID());
				noteJson.put("Sort_ID", pNote.getSort_ID());
				noteJson.put("Cover_Img", pNote.getCover_Img());
				noteJson.put("Article_Text", pNote.getArticle_Text().substring(0,tLength));
				
				jsonArray.add(noteJson);
			}
			
			response.setCharacterEncoding("UTF-8");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("noteData", jsonArray);
		    response.getWriter().print(jsonObject); 
		}else if(request.getParameter("opeType").equals("del")){
			String idArr = request.getParameter("noteId"); 
			
			TNoteService noteService = new TNoteService();
			
			response.setCharacterEncoding("UTF-8");
			JSONObject jsonObject = new JSONObject();
			
			boolean result = true;
			try {
				result = noteService.delNote(idArr, connection);
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
				int Article_ID = Integer.parseInt(request.getParameter("noteId")); 
				String Article_Title = request.getParameter("noteTitle");
				String Article_Record = request.getParameter("content");
				String Article_Text = request.getParameter("contentText");
				int Sort_ID = Integer.parseInt(request.getParameter("fCode"));
				int Type_ID = Integer.parseInt(request.getParameter("typeId"));
				String Type_Name = request.getParameter("typeName");
				String Cover_Img = request.getParameter("coverImg");
				
				TNote note = new TNote();
				
				note.setArticle_ID(Article_ID);
				note.setArticle_Title(Article_Title);
				note.setArticle_Record(Article_Record);
				note.setArticle_Text(Article_Text);
				note.setSort_ID(Sort_ID);
				note.setType_ID(Type_ID);
				note.setType_Name(Type_Name);
				note.setCover_Img(Cover_Img);
				
				
				TNoteService noteService = new TNoteService();
				
				result = noteService.updateNote(note, connection);
				
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
