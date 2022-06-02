package com.kye.blog.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.kye.blog.domain.user.User;
import com.kye.blog.domain.user.dto.LoginReqDto;



@WebServlet("/ajax1")
public class AjaxTextOne extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AjaxTextOne() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/* 읽기 */
		// json 데이터로 보내오면 서버에서는 buffer로 읽어야 한다.
		BufferedReader br = request.getReader();  // http body 데이터를 순수하게 읽는다 (원형)
		String reqData =  br.readLine();
		System.out.println(reqData);
		
		// Gson 라이브러리 이용
		// gson.fromJson() -> json을 자바 오브젝트로 변환
		// gson.toJson() -> 자바 오브젝트를 json으로 변환
		Gson gson = new Gson();
		
		// gson.fromJson() -> json을 자바 오브젝트로 변환
		LoginReqDto loginReqDto = gson.fromJson(reqData, LoginReqDto.class);		
		System.out.println(loginReqDto.getUsername());
		
		/* 응답 */
		// json으로 응답 시에는 반드시 writer로, http로 응답하는 것은 의미가 없음
		User usr = new User();
		usr.setId(1);
		usr.setUsername("김갑동");
		usr.setPassword("1111");
		usr.setAddress("서울시 서대문구 북가좌2동 212-22");
		
		PrintWriter out = response.getWriter();
		
		// gson.toJson() -> 자바 오브젝트를 json으로 변환
		String userJson = gson.toJson(usr);
		//  contentType을 json으로 설정해서 보내면 클라이언트에서 알아서 json으로 받음
		response.setContentType("application/json; charset=utf-8");
		System.out.println(userJson);
		out.print(userJson);
		out.flush();
		
		// 1. gson을 이용하여 json으로 변환해서 보내는 방법
		// 2. contentType을 json으로 설정해서 보내는 방법
		// 3. 클라이언트에서 dataType: "json" 으로 파싱하는 방법
		// 제일 좋은 방법은 3번, 서버에서 어떻게 줄지 알 수 없기 때문에

	}

}
