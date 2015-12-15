package com.qibu.service;


import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sun.misc.BASE64Encoder;

import net.sf.json.JSONObject;


import com.qibu.bean.TBook;
import com.qibu.bean.TUser;
import com.qibu.filter.TUserInfo;
import com.qibu.util.MailSenderInfo;
import com.qibu.util.SimpleMailSender;

public class TUserService {

	// 用户登陆
	public boolean login(TUser user, Connection connetion)throws Exception{
		int length = 0;
		Statement statement = connetion.createStatement();
		
		String Account_Num = user.getAccount_Num();
		String Pass_word = md5Util(user.getPass_word());
		System.out.println(Pass_word);
		String sql = "select * from user_info where Account_Num='"+Account_Num +"' and Pass_word='"+Pass_word+"'";
		
		System.out.println(sql);  
		ResultSet rs = statement.executeQuery(sql);  
	    
	    while(rs.next()) {  
	    	length = rs.getInt(1);
	    } 
	    System.out.println(length);  
	    statement.close();
	    if(length>0){
	    	TUserInfo.setUserId(user.getSessionId(), String.valueOf(length));
	    	return true;
	    }else{
	    	return false;
	    }
		
	}
	
	// 用户召回
	public boolean recall(TUser user, Connection connetion)throws Exception{
		Statement statement  = null;
		ResultSet rs = null;
		try {
			String userid = user.getAccount_Num();
			String email = user.getE_mail();
			
			int id = 0;
			String them = "" + userid + "找回密码通知";
		    statement = connetion.createStatement();
			String sql = "select * from user_info where Account_Num='" + userid
					+ "'and E_mail='" + email + "'";
			// System.out.print(email);
			 rs = statement.executeQuery(sql);
			if (rs.next()) {
				id = rs.getInt(1);

				String body = "如果您确定该申请，请点击以下链接：<br><a href=http://localhost:8080/start/RecallAction?id="
						+ id + ">http://127.0.0.1:8080/manage/RecallAction?id="
						+ id +  "</a><br>如果该链接无法点击，请直接拷贝以上链接到浏览器(例如IE)地址栏中访问。如果您错误地收到了此电子邮件，您无需执行任何操作!";
				// 这个类主要是设置邮件
				MailSenderInfo mailInfo = new MailSenderInfo();
				mailInfo.setMailServerHost("smtp.qq.com");
				mailInfo.setMailServerPort("25");
				mailInfo.setValidate(true);
				mailInfo.setUserName("859823365@qq.com");
				mailInfo.setPassword("pyw.121519");// 您的邮箱密码
				mailInfo.setFromAddress("859823365@qq.com");
				mailInfo.setToAddress(email);
				mailInfo.setSubject(them);
				mailInfo.setContent(body);
				// 这个类主要来发送邮件
				SimpleMailSender sms = new SimpleMailSender();
				// sms.sendTextMail(mailInfo);//发送文体格式
				sms.sendHtmlMail(mailInfo);// 发送html格式
				
				return true;
			} else {
				
				return false;
				
			}
		} catch (Exception e) {
			System.out.print(e);
			return false;
		}finally{
			rs.close();
			statement.close();
		}
		
	}
	
	
	
	
	public final static String md5Util(String Password){
        char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        
        try {
            byte[] pStrTemt = Password.getBytes();
            BASE64Encoder base64 = new BASE64Encoder();
            String base64Str = base64.encode(pStrTemt);
            byte[] pbase64 = base64Str.getBytes();
            MessageDigest pMdTemp = MessageDigest.getInstance("MD5");
            pMdTemp.update(pbase64);
            byte[] md = pMdTemp.digest();
            int j = md.length;
            char str[] = new char[j*2];
            int k = 0;
            for(int i = 0; i<j; i++){
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>>6 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
          
           return null;
        }
        
    }
	
}
