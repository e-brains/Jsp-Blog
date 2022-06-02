<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../layout/ly_header.jsp"%>



<% 
	// 단순 sendRedirect를 사용하면 밖으로 나갔다가 다시 접근하는 것이므로 
	// 톰캣이 request와 response를 다시 생성하게 되고 필터를 다시 타면서  접근이 불허된다.	
	//response.sendRedirect("board/listMain.jsp");

	// RequestDispatcher를 사용하면  톰캣이 생성하는 request와 response를 덮어 씌운다.
	// 그래서밖으로 나갔다가 다시 접근하는 것이 아니라 디스패처에 의해 내부적으로 이동한다. 
	// controller를 호출해야 하므로 cmd로 호출, 최초 페이지는 0 페이지  (0부터 시작이라서 이게 편함)
	// 서버단의 BaordDao에서 pstmt.setInt(1, page*4); // 0 -> 0, 1 ->4, 2->8 처리 되므로
	RequestDispatcher dis = request.getRequestDispatcher("/board?cmd=list&page=0");
	dis.forward(request, response); // 톰캣이 생성하는 request와 response를 덮어 씌운다.
%>