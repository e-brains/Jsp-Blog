package com.kye.blog.config;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 이제부터는 내부에서의 모든 요청은 RequestDispatcher로 해야한다. 
// 그래야 다시 필터를 타지 않는다.

public class ForbiddenUrlConfig implements Filter{

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
    	HttpServletResponse response = (HttpServletResponse) resp;

		/*
		 * URL과 URI의 차이점 
		 * URL -> http://localhost:8090/blog/index.jsp 
		 * URI -> /blog/index.jsp  ( conetxt path와 파일명 )
		 */    	
//    	System.out.println(request.getRequestURL()); 
//    	System.out.println(request.getRequestURI());
    	
    	// index.jsp 는 예외로 호출될 수 있도록 하고 이외의 모든 경우는 막는다.
    	// "/blog/"를 열어주는 이유는 index.jsp가 여기에 있기 때문에 열어줘야 함 
    	// "/blog/" 는 ContextPath 이다.
    	if(request.getRequestURI().equals("/blog/") || request.getRequestURI().equals("/blog/index.jsp")) {
    		chain.doFilter(request, response);
    	}else {
    		PrintWriter out = response.getWriter();
    		out.print("잘못된 접근입니다.");
    		out.flush();
    	}
		
	}
}
