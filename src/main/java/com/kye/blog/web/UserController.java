package com.kye.blog.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kye.blog.domain.user.User;
import com.kye.blog.domain.user.dto.JoinReqDto;
import com.kye.blog.domain.user.dto.LoginReqDto;
import com.kye.blog.service.UserService;
import com.kye.blog.util.Script;

@WebServlet("/user")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UserController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doProcess(request, response);
	}

	// http://localhost:8090/blog/user?cmd=xxx
	protected void doProcess(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 쿼리 스트링의 cmd 값에 따라서 분기처리
		String cmd = request.getParameter("cmd");

		UserService userService = new UserService();

		/* 로그인 화면 요청이면 */
		if (cmd.equals("loginForm")) { 
			
			// 화면 이동
			//response.sendRedirect("user/loginForm.jsp");
			
			  RequestDispatcher dis =
			  request.getRequestDispatcher("user/loginForm.jsp"); dis.forward(request,
			  response);			 

		/* 로그인 처리 요청이면 */
		} else if (cmd.equals("login")) { 

			
			
			// dto 만들기
			// (원래는 여기서 처리하지 않고 리틀렉션에서 필터로 처리되어야 깔끔함)
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			
			LoginReqDto dto = new LoginReqDto();
			dto.setUsername(username);
			dto.setPassword(password);

			// 서비스 호출
			User userEntity = userService.로그인(dto);

			// 회원가입자라면 세션을 생성해 준다.
			if(userEntity != null) {
				HttpSession session = request.getSession();
				session.setAttribute("principal", userEntity); // 인증주체
				response.sendRedirect("index.jsp");
			}else {
				Script.back(response, "로그인실패");  // history.back(); 여기서 history는 윈도우가 들고 있는 객체이다.
			}

			/* 회원가입 화면 요청 */
		} else if (cmd.equals("joinForm")) { 
			// 화면이동
			//response.sendRedirect("user/joinForm.jsp");

			RequestDispatcher dis = 
					request.getRequestDispatcher("user/joinForm.jsp");
				dis.forward(request, response);

		/* 회원가입 처리 요청이면 */
		} else if (cmd.equals("join")) { 

			System.out.println("join");
			// dto 만들기
			// (원래는 여기서 처리하지 않고 리틀렉션에서 필터로 처리되어야 깔끔함)
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String email = request.getParameter("email");
			String address = request.getParameter("address");

			JoinReqDto dto = new JoinReqDto();
			dto.setUsername(username);
			dto.setPassword(password);
			dto.setEmail(email);
			dto.setAddress(address);

			// 서비스 호출
			int result = userService.회원가입(dto);
			if (result == 1) {

				response.sendRedirect("index.jsp");
			} else {

				Script.back(response, "회원가입 실패");
			}

			/* id 중복 체크 */
		} else if (cmd.equals("idCheck")) {
			System.out.println("아이디 중복 체크");
			 // 일반 text로 보냈기 때문에 키:밸류 가 아니라서 버퍼를 읽어야 한다.
			 BufferedReader br = request.getReader(); 
			 String username = br.readLine();   // 데이터가 하나라서 while문을 돌릴 필요가 없음
			 System.out.println(username);
			 int result = userService.유저네임중복체크(username);
			 
			 PrintWriter out = response.getWriter();
			 if(result == 1) {
			 out.print("ok");
			 }else {
			 out.print("fail");
			 }
			 out.flush();
			 
		/* 로그아웃 */
		} else if (cmd.equals("logout")) {

			HttpSession session = request.getSession();
			session.invalidate();  // 세션 무효화
			response.sendRedirect("index.jsp");
		}

	}

}
