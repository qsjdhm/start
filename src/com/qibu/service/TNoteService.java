package com.qibu.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.qibu.bean.TBook;
import com.qibu.bean.TNote;

public class TNoteService {

	// 添加一个图书
	public boolean addNote(TNote note, Connection connetion)throws Exception{
		
		Statement statement = connetion.createStatement();
		String Article_Title = note.getArticle_Title();
		String Article_Record = note.getArticle_Record();
		String Article_Text = note.getArticle_Text();
		int Sort_ID = note.getSort_ID(); 
		int Type_ID = note.getType_ID(); 
		String Type_Name = note.getType_Name();
		String Cover_Img = note.getCover_Img();
		String Aarticle_Label = note.getAarticle_Label();
		
		// 插入不重复的标签
		addLable(Aarticle_Label, connetion);
		
	    
		// 初始化时间
		SimpleDateFormat pSMDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date pDate = new Date();
        String pNowDate = pSMDate.format(pDate);
        
		StringBuffer insql = new StringBuffer( "insert into article_info (Article_Title,Article_Date,Article_Record,Aarticle_Label,Type_ID,Type_Name,Sort_ID,Cover_Img,Article_Text) values(");
		insql.append("'").append(Article_Title).append("',");
		insql.append("'").append(pNowDate).append("',");
		insql.append("'").append(Article_Record).append("',");
		insql.append("'").append(Aarticle_Label).append("',");
		insql.append(Type_ID).append(",");
		insql.append("'").append(Type_Name).append("',");
		insql.append(Sort_ID).append(",");
		insql.append("'").append(Cover_Img).append("',");
		insql.append("'").append(Article_Text).append("')");
		
		System.out.println(insql);  
	    int inrs = statement.executeUpdate(insql.toString());  
	    statement.close();
	    if(inrs>0){
	    	return true;
	    }else{
	    	return false;
	    }
		
	}
	
	// 添加不重复的标签
	private void addLable(String pLable, Connection connetion)throws Exception{
		Statement statement = connetion.createStatement();
		
		// 查出所有的标签
		String sql = "select Sort_Name from system_deploy ";
		System.out.println(sql);  
	    ResultSet rs = statement.executeQuery(sql);  
	    List tagList = new ArrayList();
	    while(rs.next()) {  
	    	tagList.add(rs.getString("Sort_Name"));
	    	System.out.println("-------------------------------------"); 
	    	System.out.println(rs.getString("Sort_Name")); 
	    }
	    
	    String[] labelList = pLable.split(",");
	    
	    // 对比已经存在的标签  不存在添加
	    for(int i=0; i<labelList.length; i++){
	    	if(!tagList.contains(labelList[i])){
	    		System.out.println("1-------------------------------------"); 
		    	System.out.println(labelList[i]); 
	    		StringBuffer insql = new StringBuffer( "insert into system_deploy (Sort_Name,F_Code) values(");
	    		insql.append("'").append(labelList[i]).append("',");
	    		insql.append(4).append(")");
	    		statement.executeUpdate(insql.toString());  
	    	}
	    }
	    
	    rs.close();
	    statement.close();
	}
	
	// 查询一个笔记的内容
	public List<TNote> queryOneNote(TNote note, Connection connetion)throws Exception{
		
		Statement statement = connetion.createStatement();
		
		List<TNote> noteList = new ArrayList();
		String sql = "select * from article_info where Article_ID="+note.getArticle_ID();
		System.out.println(sql);  
	    ResultSet rs = statement.executeQuery(sql);  
	    
	    while(rs.next()) {  
	    	TNote pNote = new TNote();
	    	
	    	pNote.setArticle_ID(rs.getInt("Article_ID"));
	    	pNote.setArticle_Title(rs.getString("Article_Title"));
	    	pNote.setArticle_Date(rs.getString("Article_Date"));
	    	pNote.setArticle_Record(new String((byte[])rs.getObject("Article_Record"),"utf-8"));
	    	pNote.setRecommend_Num(rs.getInt("Recommend_Num"));
	    	pNote.setAarticle_Label(rs.getString("Aarticle_Label"));
	    	pNote.setType_ID(rs.getInt("Type_ID"));
	    	pNote.setType_Name(rs.getString("Type_Name"));
	    	pNote.setQuery_Num(rs.getInt("Query_Num"));
	    	pNote.setUser_ID(rs.getInt("User_ID"));
	    	pNote.setSort_ID(rs.getInt("Sort_ID"));
	    	pNote.setCover_Img(rs.getString("Cover_Img"));
	    	pNote.setArticle_Text(new String((byte[])rs.getObject("Article_Text"),"utf-8"));
	    	noteList.add(pNote);
	    }
	    rs.close();
	    statement.close();
	    
		return noteList;
	}
	
	
	
	
	// 查询图书的个数
	public int queryNoteLength(int typeId, Connection connetion)throws Exception{
		int length = 0;
		Statement statement = connetion.createStatement();
		
		// 根据分类Id查出此分类下的个数
		String sql = "";
		if(typeId==0){
			sql = "select COUNT(*) from article_info where Sort_ID=2";  // 查询笔记所有的个数
		}else{  // 分类查询
			sql = "select COUNT(*) from article_info where Sort_ID=2 and Type_ID="+typeId;
		}
		
		System.out.println(sql);  
	    ResultSet rs = statement.executeQuery(sql);  
	    
	    while(rs.next()) {  
	    	length = rs.getInt(1);
	    }
	    rs.close();
	    statement.close();
		return length;
	}
	
	// 查询笔记分页
	public List<TNote> pageQueryNote(int pageId, int pageCount, int typeId, Connection connetion)throws Exception{
		
		Statement statement = connetion.createStatement();
		
		List<TNote> noteList = new ArrayList();
		String sql = "";
		if(typeId==0){
			sql = "select * from article_info where Sort_ID=2 order by Article_Date desc limit "+ pageCount*(pageId-1) + " , " + pageCount;
		}else{  // 分类查询
			sql = "select * from article_info  where Sort_ID=2 and Type_ID="+typeId +" order by Article_Date desc limit "+ pageCount*(pageId-1) + " , " + pageCount;
		}
		
		System.out.println(sql);  
	    ResultSet rs = statement.executeQuery(sql);  
	    
	    while(rs.next()) {  
	    	TNote pNote = new TNote();
	    	
	    	pNote.setArticle_ID(rs.getInt("Article_ID"));
	    	pNote.setArticle_Title(rs.getString("Article_Title"));
	    	pNote.setArticle_Date(rs.getString("Article_Date"));
	    	pNote.setArticle_Record(new String((byte[])rs.getObject("Article_Record"),"utf-8"));
	    	pNote.setRecommend_Num(rs.getInt("Recommend_Num"));
	    	pNote.setAarticle_Label(rs.getString("Aarticle_Label"));
	    	pNote.setType_ID(rs.getInt("Type_ID"));
	    	pNote.setType_Name(rs.getString("Type_Name"));
	    	pNote.setQuery_Num(rs.getInt("Query_Num"));
	    	pNote.setUser_ID(rs.getInt("User_ID"));
	    	pNote.setSort_ID(rs.getInt("Sort_ID"));
	    	pNote.setCover_Img(rs.getString("Cover_Img"));
	    	pNote.setArticle_Text(new String((byte[])rs.getObject("Article_Text"),"utf-8"));
	    	noteList.add(pNote);
	    }
	    rs.close();
	    statement.close();
	    
		return noteList;
	}
	
	// 删除图书
	public boolean delNote(String idArr, Connection connetion)throws Exception{
		Statement statement = connetion.createStatement();
		
		// 删除多条数据
		String sql = "delete from article_info where Article_ID in("+idArr+")";
		int inrs = statement.executeUpdate(sql.toString());  
	    statement.close();
	    if(inrs>0){
	    	return true;
	    }else{
	    	return false;
	    }
	}
	
	// 修改一个图书
	public boolean updateNote(TNote note, Connection connetion)throws Exception{
		
		Statement statement = connetion.createStatement();
		
		int Article_ID = note.getArticle_ID();
		String Article_Title = note.getArticle_Title();
		String Article_Record = note.getArticle_Record();
		String Article_Text = note.getArticle_Text();
		int Sort_ID = note.getSort_ID(); 
		int Type_ID = note.getType_ID(); 
		String Type_Name = note.getType_Name();
		String Cover_Img = note.getCover_Img();
		
		StringBuffer insql;
		// 初始化时间
		SimpleDateFormat pSMDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date pDate = new Date();
        String pNowDate = pSMDate.format(pDate);
        
        // 如果封面前台传的是空，就代表不需要修改
        if(Cover_Img.equals("")||Cover_Img.equals(null)){
        	insql = new StringBuffer( "update article_info set Article_Title='"+Article_Title+"', Article_Record='"+Article_Record+"', Article_Text='"+Article_Text+"', Sort_ID='"+Sort_ID+"', Type_ID='"+Type_ID+"', Type_Name='"+Type_Name+"', Article_Date='"+pNowDate+"' where Article_ID="+Article_ID+"");
        }else{
        	insql = new StringBuffer( "update article_info set Article_Title='"+Article_Title+"', Article_Record='"+Article_Record+"', Article_Text='"+Article_Text+"', Cover_Img='"+Cover_Img+"', Sort_ID='"+Sort_ID+"', Type_ID='"+Type_ID+"', Type_Name='"+Type_Name+"', Article_Date='"+pNowDate+"' where Article_ID="+Article_ID+"");
        }
		
		System.out.println(insql);  
	    int inrs = statement.executeUpdate(insql.toString());  
	    statement.close();
	    if(inrs>0){
	    	return true;
	    }else{
	    	return false;
	    }
		
	}

}
