package com.kye.blog.web;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.kye.blog.domain.board.dto.CommonRespDto;
import com.kye.blog.domain.reply.Reply;
import com.kye.blog.domain.reply.dto.SaveReqDto;
import com.kye.blog.service.ReplyService;
import com.kye.blog.util.Script;

@WebServlet("/reply")
public class ReplyrController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ReplyrController() {
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
	
	protected void doProcess(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException  {
		
		String cmd = request.getParameter("cmd");
		ReplyService replyService = new ReplyService();
		// http://localhost:8080/blog/reply?cmd=save
		//HttpSession session = request.getSession();
		
		/* 댓글 쓰기 저장 */
		if (cmd.equals("save")) {

			// json을 받는 경우 기본적으로 String이기 때문에 버퍼로 받는다.
			BufferedReader br = request.getReader();
			String reqData = br.readLine();
			Gson gson = new Gson();
			SaveReqDto dto = gson.fromJson(reqData, SaveReqDto.class);
			//System.out.println("dto : "+dto);

			CommonRespDto<Reply> commonRespDto = replyService.댓글쓰기및목록(dto);
			String responseData = gson.toJson(commonRespDto); 
			//System.out.println("responseData : "+responseData);
			Script.responseData(response, responseData);
			
			/* 댓글 삭제 */	
		}else if(cmd.equals("delete")) {
			
			int id = Integer.parseInt(request.getParameter("id"));
			int result = replyService.댓글삭제(id);
			
			// <>제네릭 타입을 채우지 않으면 기본  Object 타입이 채워진다.
			// CommonRespDto == CommonRespDto<Object> <-  툴에서 자동 제안해 줌
			CommonRespDto commonDto = new CommonRespDto<>();
			commonDto.setStatusCode(result);  //1, -1  클라이언트에서 분기하도록 한다.
			
			Gson gson = new Gson();
			String jsonData = gson.toJson(commonDto);
			// { "statusCode" : 1 }
			Script.responseData(response, jsonData);
		}
	}

}
