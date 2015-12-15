package com.qibu.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionEvent;


/**
 * Servlet Filter implementation class TIsLoginFilter
 */
public class TIsLoginFilter implements Filter {

    /**
     * Default constructor. 
     */
    public TIsLoginFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
	    HttpServletRequest pRequest   = (HttpServletRequest) request;
	    HttpServletResponse pResponse = (HttpServletResponse) response;
	    String pRUrl   = pRequest.getRequestURL().toString();
	    
	    if("http://192.168.1.103:8080/start/".equals(pRUrl) ||"http://localhost:8080/start/".equals(pRUrl) || "http://www.czlqibu.com/".equals(pRUrl) || "http://czlqibu.com/".equals(pRUrl) || pRUrl.contains("/code.html") || pRUrl.contains("/design.html") || pRUrl.contains("/front.html")|| pRUrl.contains("/index.html") || pRUrl.contains("/joy.html") || pRUrl.contains("/more.html") || pRUrl.contains("/product.html") || pRUrl.contains("/search.html") || pRUrl.contains("/show.html") || pRUrl.contains("/topic.html") || pRUrl.contains("/about.html") || pRUrl.contains("/login.html")){
	    	if(pRUrl.contains("/admin/index.html")){
	    		String pUserId =  TUserInfo.getUserId(pRequest.getSession().getId());

		    	if(null==pUserId || "".equals(pUserId)){
		    		PrintWriter out = response.getWriter();  
	                out.println("<html>");    
	                out.println("<script>");    
	                out.println("window.location.href = 'http://www.czlqibu.com/admin/login.html'");  
	                out.println("</script>");    
	                out.println("</html>");  
		    	}
	    	}
	    }else{
	    	String pUserId =  TUserInfo.getUserId(pRequest.getSession().getId());

	    	if(null==pUserId || "".equals(pUserId)){
	    		PrintWriter out = response.getWriter();  
                out.println("<html>");    
                out.println("<script>");    
                out.println("window.location.href = 'http://www.czlqibu.com/admin/login.html'");  
                out.println("</script>");    
                out.println("</html>");  
	    	}
	    }
	    chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

	public void sessionCreated(HttpSessionEvent arg0) {
		
	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
		String pSId = arg0.getSession().getId();
		TUserInfo.removeUserId(pSId);
		
	}

}
