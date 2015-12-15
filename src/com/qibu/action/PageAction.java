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

import com.qibu.bean.TClassify;
import com.qibu.bean.TComment;
import com.qibu.bean.TNote;
import com.qibu.database.TJDBCManager;
import com.qibu.service.TArticleService;
import com.qibu.service.TClassifyService;
import com.qibu.service.TCommentService;
import com.qibu.service.TNoteService;

/**
 * Servlet implementation class PageAction
 */
public class PageAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PageAction() {
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
		// TODO Auto-generated method stub
		
		Connection connection = null;
		connection = TJDBCManager.getInstance().getConnection();
		request.setCharacterEncoding("UTF-8");
		try{
		// 判断操作类型
		if(request.getParameter("opeType").equals("queryLikeLableLength")){
			String lableName = URLDecoder.decode(URLDecoder.decode(request.getParameter("lableName"), "utf-8"), "utf-8"); 
			
			TArticleService articleService = new TArticleService();
			try {
				int length = articleService.queryLikeLableLength(lableName, connection);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("length", length);
			    response.getWriter().print(jsonObject); 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(request.getParameter("opeType").equals("queryLikeLable")){
			String lableName = URLDecoder.decode(URLDecoder.decode(request.getParameter("lableName"), "utf-8"), "utf-8"); 
			int pageId = Integer.parseInt(request.getParameter("pageId"));  // 页数
			int pageCount = Integer.parseInt(request.getParameter("pageCount"));  // 每页个数
			
			TArticleService articleService = new TArticleService();
			connection = TJDBCManager.getInstance().getConnection();
			
			List<TNote> listNote = null;
			try {
				listNote = articleService.pageLableInArticle(pageId, pageCount, lableName, connection);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			JSONArray jsonArray = new JSONArray();
			for(int i=0; i<listNote.size(); i++){
				JSONObject noteJson = new JSONObject();
				TNote pNote = listNote.get(i);
				
				int cLength = 0;
				if(230 > pNote.getArticle_Record().length()){
					cLength = pNote.getArticle_Record().length();
				}else{
					cLength = 230;
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
				noteJson.put("Article_Text", pNote.getArticle_Text());
				
				jsonArray.add(noteJson);
			}
			
			response.setCharacterEncoding("UTF-8");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("articleData", jsonArray);
		    response.getWriter().print(jsonObject); 
		}else if(request.getParameter("opeType").equals("queryKeywordLength")){
			String keyword = URLDecoder.decode(URLDecoder.decode(request.getParameter("keyword"), "utf-8"), "utf-8"); 
			
			TArticleService articleService = new TArticleService();
			
			try {
				int length = articleService.queryLikeKeywordLength(keyword, connection);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("length", length);
			    response.getWriter().print(jsonObject); 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}else if(request.getParameter("opeType").equals("queryKeyword")){
			String keyword = URLDecoder.decode(URLDecoder.decode(request.getParameter("keyword"), "utf-8"), "utf-8"); 
			int pageId = Integer.parseInt(request.getParameter("pageId"));  // 页数
			int pageCount = Integer.parseInt(request.getParameter("pageCount"));  // 每页个数
			
			TArticleService articleService = new TArticleService();
			
			List<TNote> listNote = null;
			try {
				listNote = articleService.queryLikeKeyword(pageId, pageCount, keyword, connection);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			JSONArray jsonArray = new JSONArray();
			for(int i=0; i<listNote.size(); i++){
				JSONObject noteJson = new JSONObject();
				TNote pNote = listNote.get(i);
				
				int cLength = 0;
				if(230 > pNote.getArticle_Record().length()){
					cLength = pNote.getArticle_Record().length();
				}else{
					cLength = 230;
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
				noteJson.put("Article_Text", pNote.getArticle_Text());
				
				jsonArray.add(noteJson);
			}
			
			response.setCharacterEncoding("UTF-8");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("articleData", jsonArray);
		    response.getWriter().print(jsonObject); 
		}else if(request.getParameter("opeType").equals("queryClassifyNum")){  // 查询笔记分类下的笔记个数
			
			TNoteService noteService = new TNoteService();
			TClassifyService classifyService = new TClassifyService();
			
			List<TClassify> listClassify = null;
			
			try {
				listClassify = classifyService.pageQueryBook(1, 1000, 2, connection);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			JSONArray jsonArray = new JSONArray();
			for(int i=0; i<listClassify.size(); i++){
				JSONObject classifyJson = new JSONObject();
				TClassify pClassify = listClassify.get(i);
				
				// 通过类型id查找文章类型是此类型的文章个数
				int length = 0;
				try {
					length = noteService.queryNoteLength(pClassify.getSort_ID(), connection);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				classifyJson.put("Sort_ID", pClassify.getSort_ID());
				classifyJson.put("Sort_Name", pClassify.getSort_Name());
				classifyJson.put("Article_Length", length);
				jsonArray.add(classifyJson);
			}
			
			response.setCharacterEncoding("UTF-8");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("classifyData", jsonArray);
		    response.getWriter().print(jsonObject); 
		}else if(request.getParameter("opeType").equals("queryAllLength")){
			int type = Integer.parseInt(request.getParameter("type"));  // 文章分类
			
			TArticleService articleService = new TArticleService();
			
			try {
				// 查询页面所需要的全部图书的长度
				int length = articleService.queryNoteLengthP(type, connection);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("length", length);
			    response.getWriter().print(jsonObject); 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}else if(request.getParameter("opeType").equals("queryAllPageData")){
			int typeId = Integer.parseInt(request.getParameter("typeId"));  // 分类
			int pageId = Integer.parseInt(request.getParameter("pageId"));  // 页数
			int pageCount = Integer.parseInt(request.getParameter("pageCount"));  // 每页个数
			int contentLength = Integer.parseInt(request.getParameter("contentLength"));  // 带有标签的长度
			int textLength = Integer.parseInt(request.getParameter("textLength"));  // 纯文本的长度
			
			TArticleService articleService = new TArticleService();
			
			List<TNote> listNote = null;
			try {
				// 查询页面所需要的所有的文章
				listNote = articleService.pageQueryNoteP(typeId, pageId, pageCount, connection);
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
			jsonObject.put("articleData", jsonArray);
		    response.getWriter().print(jsonObject); 
		}else if(request.getParameter("opeType").equals("addComment")){
			boolean result;
			try {
				
				String Comment_Person_Name = URLDecoder.decode(URLDecoder.decode(request.getParameter("userName"), "utf-8"), "utf-8");
				String Comment_Person_email = URLDecoder.decode(URLDecoder.decode(request.getParameter("userEmail"), "utf-8"), "utf-8");
				String Comment_Record = URLDecoder.decode(URLDecoder.decode(request.getParameter("commentContent"), "utf-8"), "utf-8");
				int Article_ID = Integer.parseInt(request.getParameter("articleId")); 
				int F_Code = Integer.parseInt(request.getParameter("fCode")); 
				
				TComment comment = new TComment();
				
				comment.setComment_Person_Name(Comment_Person_Name);
				comment.setComment_Person_email(Comment_Person_email);
				comment.setComment_Record(Comment_Record);
				comment.setArticle_ID(Article_ID);
				comment.setF_Code(F_Code);
				
				TCommentService commentService = new TCommentService();
				
				result = commentService.addComment(comment, connection);
				
				response.setCharacterEncoding("UTF-8");
				JSONObject jsonObject = new JSONObject();
				if(result){
					// 查询刚添加的那条数据
					List<TComment> listComment = commentService.queryTopComment(connection);
					JSONArray jsonArray = new JSONArray();
					for(int i=0; i<listComment.size(); i++){
						JSONObject commentJson = new JSONObject();
						TComment topComment = listComment.get(i);
						commentJson.put("Comment_ID", topComment.getComment_ID());
						commentJson.put("Comment_Person_ID", topComment.getComment_Person_ID());
						commentJson.put("Comment_DataTime", topComment.getComment_DataTime());
						commentJson.put("F_Code", topComment.getF_Code());
						
						jsonArray.add(commentJson);
					}
					
					jsonObject.put("code", "1");
					jsonObject.put("msg", "添加成功");
					jsonObject.put("topData", jsonArray);
				}else{
					jsonObject.put("code", "-1");
					jsonObject.put("msg", "添加失败");
				}
			    response.getWriter().print(jsonObject); 
			    
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(request.getParameter("opeType").equals("queryRecomLength")){
			
			TArticleService articleService = new TArticleService();
			
			try {
				// 查询页面所需要的全部图书的长度
				int length = articleService.queryRecomLength(connection);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("length", length);
			    response.getWriter().print(jsonObject); 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}else if(request.getParameter("opeType").equals("queryNewNoteRecomLength")){
			
			TArticleService articleService = new TArticleService();
			
			try {
				// 查询页面所需要的全部图书的长度
				int length = articleService.queryNewNoteRecomLength(connection);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("length", length);
			    response.getWriter().print(jsonObject); 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}else if(request.getParameter("opeType").equals("queryRecomData")){
			int pageId = Integer.parseInt(request.getParameter("pageId"));  // 页数
			int pageCount = Integer.parseInt(request.getParameter("pageCount"));  // 每页个数
			int contentLength = Integer.parseInt(request.getParameter("contentLength"));  // 带有标签的长度
			int textLength = Integer.parseInt(request.getParameter("textLength"));  // 纯文本的长度
			
			TArticleService articleService = new TArticleService();
			
			List<TNote> listNote = null;
			try {
				// 查询页面所需要的所有的文章
				listNote = articleService.queryRecomData(pageId, pageCount, connection);
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
			jsonObject.put("articleData", jsonArray);
		    response.getWriter().print(jsonObject); 
		}else if(request.getParameter("opeType").equals("queryNewNoteRecomData")){
			int pageId = Integer.parseInt(request.getParameter("pageId"));  // 页数
			int pageCount = Integer.parseInt(request.getParameter("pageCount"));  // 每页个数
			int contentLength = Integer.parseInt(request.getParameter("contentLength"));  // 带有标签的长度
			int textLength = Integer.parseInt(request.getParameter("textLength"));  // 纯文本的长度
			
			TArticleService articleService = new TArticleService();
			
			List<TNote> listNote = null;
			try {
				// 查询页面所需要的所有的文章
				listNote = articleService.queryNewNoteRecomData(pageId, pageCount, connection);
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
			jsonObject.put("articleData", jsonArray);
		    response.getWriter().print(jsonObject); 
		}else if(request.getParameter("opeType").equals("queryNoteArtRecomData")){  // 查询笔记分类下的笔记个数
			
			TArticleService articleService = new TArticleService();
			//connection = TJDBCManager.getInstance().getConnection();
			// 查询推荐文章数据
			List<TNote> listArt = null;
			try {
				// 查询页面所需要的所有的文章
				listArt = articleService.queryRecomData(1, 5, connection);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			JSONArray artJsonArray = new JSONArray();
			for(int i=0; i<listArt.size(); i++){
				JSONObject artJson = new JSONObject();
				TNote pArt = listArt.get(i);
				artJson.put("Article_ID", pArt.getArticle_ID());
				artJson.put("Article_Title", pArt.getArticle_Title());
				artJsonArray.add(artJson);
			}
			
			
			// 查询推荐笔记数据
			List<TNote> listNote = null;
			try {
				// 查询页面所需要的所有的文章
				listNote = articleService.queryRecomNoteData(connection);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			JSONArray noteJsonArray = new JSONArray();
			for(int i=0; i<listArt.size(); i++){
				JSONObject noteJson = new JSONObject();
				TNote pArt = listNote.get(i);
				noteJson.put("Article_ID", pArt.getArticle_ID());
				noteJson.put("Article_Title", pArt.getArticle_Title());
				noteJsonArray.add(noteJson);
			}
			
			response.setCharacterEncoding("UTF-8");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("articleData", artJsonArray);
			jsonObject.put("noteData", noteJsonArray);
		    response.getWriter().print(jsonObject); 
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
