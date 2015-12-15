package com.qibu.service;

import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import com.qibu.bean.TBook;
import com.qibu.bean.TClassify;

public class TClassifyService {

	// 添加一个图书
	public boolean addClassify(TClassify classify, Connection connetion)throws Exception{
		
		Statement statement = connetion.createStatement();
		
		String Sort_Name = classify.getSort_Name();
		int F_Code = classify.getF_Code();
        
		StringBuffer insql = new StringBuffer( "insert into system_deploy (Sort_Name,F_Code) values(");
		insql.append("'").append(Sort_Name).append("',");
		insql.append(F_Code).append(")");
		
		System.out.println(insql);  
	    int inrs = statement.executeUpdate(insql.toString());  
	    statement.close();
	    if(inrs>0){
	    	return true;
	    }else{
	    	return false;
	    }
	}
	
	// 查询分类的个数
	public int queryClassifyLength(int fCode, Connection connetion)throws Exception{
		int length = 0;
		Statement statement = connetion.createStatement();
		
		// 根据分类Id查出此分类下的个数
		String sql = "select COUNT(*) from system_deploy where F_Code="+fCode;
		
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
	public List<TClassify> pageQueryBook(int pageId, int pageCount, int fCode, Connection connetion)throws Exception{
		
		Statement statement = connetion.createStatement();
		
		List<TClassify> classifyList = new ArrayList();
		String sql = "select * from system_deploy where F_Code="+fCode +" order by Sort_ID desc limit "+ pageCount*(pageId-1) + " , " + pageCount;
		
		
		System.out.println(sql);  
	    ResultSet rs = statement.executeQuery(sql);  
	    
	    while(rs.next()) {  
	    	TClassify pClassify = new TClassify();
	    	pClassify.setSort_ID(rs.getInt("Sort_ID"));
	    	pClassify.setSort_Name(rs.getString("Sort_Name"));
	    	classifyList.add(pClassify);
	    }
	    rs.close();
	    statement.close();
	    
		return classifyList;
	}
	
	// 删除图书
	public boolean delClassify(String idArr, Connection connetion)throws Exception{
		Statement statement = connetion.createStatement();
		
		// 删除多条数据
		String sql = "delete from system_deploy where Sort_ID in("+idArr+")";
		int inrs = statement.executeUpdate(sql.toString());  
	    statement.close();
	    if(inrs>0){
	    	return true;
	    }else{
	    	return false;
	    }
	}
	
	// 修改一个图书
	public boolean updateClassify(TClassify classify, Connection connetion)throws Exception{
		
		Statement statement = connetion.createStatement();
		
		int Sort_ID = classify.getSort_ID();
		String Sort_Name = classify.getSort_Name();
		int F_Code = classify.getF_Code();
		
        StringBuffer insql = new StringBuffer( "update system_deploy set Sort_Name='"+Sort_Name+"', F_Code='"+F_Code+"'  where Sort_ID="+Sort_ID+"");
		
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
