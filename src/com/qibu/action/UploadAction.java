package com.qibu.action;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


/**
 * Servlet implementation class Upload
 */
public class UploadAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadAction() {
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
		request.setCharacterEncoding("UTF-8");  
			System.out.println(File.separator);
		    String savePath = this.getServletConfig().getServletContext().getRealPath("");
            // String savePathd = "admin"+File.separator+"uploads"+File.separator;
	        String savePathd = "admin/uploads/";
	        savePath = savePath + "/" + savePathd;
	        System.out.println("savePath:");
	        System.out.println(savePath);
	        File f1 = new File(savePath);
	        System.out.println("f1:");
	        System.out.println(f1);
	        System.out.println("!f1.exists():");
	        System.out.println(f1);
	        if (!f1.exists()) {
	            f1.mkdirs();
	        }
	        DiskFileItemFactory fac = new DiskFileItemFactory();
	        ServletFileUpload upload = new ServletFileUpload(fac);
	        upload.setHeaderEncoding("utf-8");
	        List fileList = null;
	        try {
	        	System.out.println("fileList start:");
	        	System.out.println(request);
	            fileList = upload.parseRequest(request);
	            System.out.println("fileList end:");
	            System.out.println(fileList);
	        } catch (FileUploadException ex) {
	        	System.out.println("ex:");
	        	System.out.println(ex);
	            return;
	        }

	        Iterator<FileItem> it = fileList.iterator();
	        String name = "";
	        String extName = "";
	        while (it.hasNext()) {
	            FileItem item = it.next();
	            if (!item.isFormField()) {
	                name = item.getName();
	                long size = item.getSize();
	                String type = item.getContentType();
	                System.out.println(size + " " + type);
	                if (name == null || name.trim().equals("")) {
	                    continue;
	                }
	                //扩展名格式： 
	                if (name.lastIndexOf(".") >= 0) {
	                    extName = name.substring(name.lastIndexOf("."));
	                }
	                File file = null;
	                do {
	                    //生成文件名：
	                    name = UUID.randomUUID().toString();
	                    file = new File(savePath + name + extName);
	                } while (file.exists());
	                File saveFile = new File(savePath + name + extName);
	                try {
	                    item.write(saveFile);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        System.out.println("-------------");
	        System.out.println(File.separator+savePathd+name + extName);
	        
	        response.getWriter().print(File.separator+savePathd+name + extName);
	        
	        //response.getWriter().print(""+savePathd+name + extName);


	}

}
