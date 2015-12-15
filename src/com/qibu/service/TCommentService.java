package com.qibu.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.qibu.bean.TBook;
import com.qibu.bean.TComment;
import com.qibu.bean.TNote;

public class TCommentService {

	// 添加一个图书
	public boolean addComment(TComment comment, Connection connetion)throws Exception{
		
		Statement statement = connetion.createStatement();
		
		String Comment_Person_Name = comment.getComment_Person_Name();
		String Comment_Person_email = comment.getComment_Person_email();
		String Comment_Record = comment.getComment_Record();
		int Article_ID = comment.getArticle_ID();
		int F_Code = comment.getF_Code();
		
		// 初始化时间
		SimpleDateFormat pSMDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date pDate = new Date();
        String pNowDate = pSMDate.format(pDate);
        
		StringBuffer insql = new StringBuffer( "insert into comment_info (Comment_Person_Name,Comment_Person_email,Comment_Record,Article_ID,F_Code,Comment_DataTime) values(");
		insql.append("'").append(Comment_Person_Name).append("',");
		insql.append("'").append(Comment_Person_email).append("',");
		insql.append("'").append(Comment_Record).append("',");
		insql.append(Article_ID).append(",");
		insql.append(F_Code).append(",");
		insql.append("'").append(pNowDate).append("')");
		
		System.out.println(insql);  
	    int inrs = statement.executeUpdate(insql.toString());  
	    
	    statement.close();
	    if(inrs>0){
	    	return true;
	    }else{
	    	return false;
	    }
		
	}
	
	
	// 查询笔记分页
	public List<TComment> queryArticleComment(int articleId, Connection connetion)throws Exception{
		
		Statement statement = connetion.createStatement();
		
		List<TComment> commentList = new ArrayList();
		String sql = "select * from comment_info where Article_ID="+articleId +" order by Comment_ID asc ";
		
		System.out.println(sql);  
	    ResultSet rs = statement.executeQuery(sql);  
	    
	    while(rs.next()) {  
	    	TComment pComment = new TComment();
	    	
	    	pComment.setComment_ID(rs.getInt("Comment_ID"));
	    	pComment.setComment_Person_ID(rs.getInt("Comment_Person_ID"));
	    	pComment.setComment_Person_Name(rs.getString("Comment_Person_Name"));
	    	pComment.setComment_Person_email(rs.getString("Comment_Person_email"));
	    	pComment.setComment_Record(rs.getString("Comment_Record"));
	    	pComment.setArticle_ID(rs.getInt("Article_ID"));
	    	pComment.setComment_DataTime(rs.getString("Comment_DataTime"));
	    	pComment.setF_Code(rs.getInt("F_Code"));
	    	commentList.add(pComment);
	    }
	    rs.close();
	    statement.close();
	    
		return commentList;
	}
	
	
	// 查询最新的评论
	public List<TComment> queryTopComment(Connection connetion)throws Exception{
		
		Statement statement = connetion.createStatement();
		
		List<TComment> commentList = new ArrayList();
		String sql = "select * from comment_info order by Comment_DataTime desc limit 0,1";
		
		System.out.println(sql);  
	    ResultSet rs = statement.executeQuery(sql);  
	    
	    while(rs.next()) {  
	    	TComment pComment = new TComment();
	    	
	    	pComment.setComment_ID(rs.getInt("Comment_ID"));
	    	pComment.setComment_Person_ID(rs.getInt("Comment_Person_ID"));
	    	pComment.setComment_Person_Name(rs.getString("Comment_Person_Name"));
	    	pComment.setComment_Person_email(rs.getString("Comment_Person_email"));
	    	pComment.setComment_Record(rs.getString("Comment_Record"));
	    	pComment.setArticle_ID(rs.getInt("Article_ID"));
	    	pComment.setComment_DataTime(rs.getString("Comment_DataTime"));
	    	pComment.setF_Code(rs.getInt("F_Code"));
	    	commentList.add(pComment);
	    }
	    rs.close();
	    statement.close();
	    
		return commentList;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
