package com.qibu.service;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import com.qibu.bean.TBook;

public class TBookService {

	// 添加一个图书
	public boolean addBook(TBook book, Connection connetion)throws Exception{
		
		Statement statement = connetion.createStatement();
		
		String Book_Name = book.getBook_Name();
		String Book_Abstract = book.getBook_Abstract();
		int Book_Height = book.getBook_Height();
		String Download_Link = book.getDownload_Link();
		String Book_Cover = book.getBook_Cover();
		int Recommend_Num = book.getRecommend_Num();
		int Type_ID = book.getType_ID();
		String Type_Name = book.getType_Name();
		int Query_Num = book.getQuery_Num();
		int User_ID = book.getUser_ID();
		
		// 初始化时间
		SimpleDateFormat pSMDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date pDate = new Date();
        String pNowDate = pSMDate.format(pDate);
        
		StringBuffer insql = new StringBuffer( "insert into book_info (Book_Name,Book_Abstract,Book_Height,Download_Link,Book_Cover,Recommend_Num,Type_ID,Type_Name,Query_Num,User_ID,Update_Time) values(");
		insql.append("'").append(Book_Name).append("',");
		insql.append("'").append(Book_Abstract).append("',");
		insql.append(Book_Height).append(",");
		insql.append("'").append(Download_Link).append("',");
		insql.append("'").append(Book_Cover).append("',");
		insql.append(Recommend_Num).append(",");
		insql.append(Type_ID).append(",");
		insql.append("'").append(Type_Name).append("',");
		insql.append(Query_Num).append(",");
		insql.append(User_ID).append(",'");
		insql.append(pNowDate).append("')");
		
		System.out.println(insql);  
	    int inrs = statement.executeUpdate(insql.toString());  
	    statement.close();
	    if(inrs>0){
	    	return true;
	    }else{
	    	return false;
	    }
		
	}
	
	// 查询一个图书的内容
	public List<TBook> queryOneBook(TBook book, Connection connetion)throws Exception{
		
		Statement statement = connetion.createStatement();
		
		List<TBook> bookList = new ArrayList();
		String sql = "select * from book_info where Book_ID="+book.getBook_ID();
		System.out.println(sql);  
	    ResultSet rs = statement.executeQuery(sql);  
	    
	    while(rs.next()) {  
	    	TBook pBook = new TBook();
	    	pBook.setBook_ID(rs.getInt("Book_ID"));
	    	pBook.setBook_Name(rs.getString("Book_Name"));
	    	pBook.setBook_Abstract(rs.getString("Book_Abstract"));
	    	pBook.setBook_Height(rs.getInt("Book_Height"));
	    	pBook.setDownload_Link(rs.getString("Download_Link"));
	    	pBook.setBook_Cover(rs.getString("Book_Cover"));
	    	pBook.setRecommend_Num(rs.getInt("Recommend_Num"));
	    	pBook.setType_ID(rs.getInt("Type_ID"));
	    	pBook.setType_Name(rs.getString("Type_Name"));
	    	pBook.setQuery_Num(rs.getInt("Query_Num"));
	    	pBook.setUser_ID(rs.getInt("User_ID"));
	    	bookList.add(pBook);
	    }
	    rs.close();
	    statement.close();
	    
		return bookList;
	}
	
	// 查询图书的个数
	public int queryBookLength(int typeId, Connection connetion)throws Exception{
		int length = 0;
		Statement statement = connetion.createStatement();
		
		// 根据分类Id查出此分类下的个数
		String sql = "";
		if(typeId==0){
			sql = "select COUNT(*) from book_info";
		}else{  // 分类查询
			sql = "select COUNT(*) from book_info where Type_ID="+typeId;
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
	
	// 查询图书分页
	public List<TBook> pageQueryBook(int pageId, int pageCount, int typeId, Connection connetion)throws Exception{
		
		Statement statement = connetion.createStatement();
		
		List<TBook> bookList = new ArrayList();
		String sql = "";
		if(typeId==0){
			sql = "select * from book_info  order by Update_Time desc limit "+ pageCount*(pageId-1) + " , " + pageCount;
		}else{  // 分类查询
			sql = "select * from book_info  where Type_ID="+typeId +" order by Update_Time desc limit "+ pageCount*(pageId-1) + " , " + pageCount;
		}
		
		System.out.println(sql);  
	    ResultSet rs = statement.executeQuery(sql);  
	    
	    while(rs.next()) {  
	    	TBook pBook = new TBook();
	    	pBook.setBook_ID(rs.getInt("Book_ID"));
	    	pBook.setBook_Name(rs.getString("Book_Name"));
	    	pBook.setBook_Abstract(rs.getString("Book_Abstract"));
	    	pBook.setBook_Height(rs.getInt("Book_Height"));
	    	pBook.setDownload_Link(rs.getString("Download_Link"));
	    	pBook.setBook_Cover(rs.getString("Book_Cover"));
	    	pBook.setRecommend_Num(rs.getInt("Recommend_Num"));
	    	pBook.setType_ID(rs.getInt("Type_ID"));
	    	pBook.setType_Name(rs.getString("Type_Name"));
	    	pBook.setQuery_Num(rs.getInt("Query_Num"));
	    	pBook.setUser_ID(rs.getInt("User_ID"));
	    	bookList.add(pBook);
	    }
	    rs.close();
	    statement.close();
	    
		return bookList;
	}
	
	// 删除图书
	public boolean delBook(String idArr, Connection connetion)throws Exception{
		Statement statement = connetion.createStatement();
		
		// 删除多条数据
		String sql = "delete from book_info where Book_ID in("+idArr+")";
		int inrs = statement.executeUpdate(sql.toString());  
	    statement.close();
	    if(inrs>0){
	    	return true;
	    }else{
	    	return false;
	    }
	}
	
	// 修改一个图书
	public boolean updateBook(TBook book, Connection connetion)throws Exception{
		
		Statement statement = connetion.createStatement();
		
		int Book_Id = book.getBook_ID();
		String Book_Name = book.getBook_Name();
		String Book_Abstract = book.getBook_Abstract();
		int Book_Height = book.getBook_Height();
		String Download_Link = book.getDownload_Link();
		String Book_Cover = book.getBook_Cover();
		int Type_ID = book.getType_ID();
		String Type_Name = book.getType_Name();
		
		// 初始化时间
		SimpleDateFormat pSMDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date pDate = new Date();
        String pNowDate = pSMDate.format(pDate);
        
        StringBuffer insql = new StringBuffer( "update book_info set Book_Name='"+Book_Name+"', Book_Abstract='"+Book_Abstract+"', Book_Height='"+Book_Height+"', Download_Link='"+Download_Link+"', Book_Cover='"+Book_Cover+"', Type_ID='"+Type_ID+"', Type_Name='"+Type_Name+"', Update_Time='"+pNowDate+"' where Book_ID="+Book_Id+"");
		
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
