package com.kye.blog.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.kye.blog.domain.board.Board;
import com.kye.blog.domain.board.dto.CommonRespDto;
import com.kye.blog.domain.board.dto.DetailRespDto;
import com.kye.blog.domain.board.dto.SaveReqDto;
import com.kye.blog.domain.reply.Reply;
import com.kye.blog.domain.user.User;
import com.kye.blog.domain.board.dto.UpdateReqDto;
import com.kye.blog.service.BoardService;
import com.kye.blog.service.ReplyService;
import com.kye.blog.util.Script;

@WebServlet("/board")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public BoardController() {
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
			throws ServletException, IOException {
		
		String cmd = request.getParameter("cmd");
		BoardService boardService = new BoardService();
		ReplyService replyService = new ReplyService();
		// http://localhost:8080/blog/board?cmd=saveForm
		HttpSession session = request.getSession();
		
		/* 글쓰기 화면으로 이동 */
		if (cmd.equals("saveForm")) {
			
			// 정상적인 로그인 인지 확인하기 위해 세션을 읽는다.
			User principal = (User) session.getAttribute("principal");		
			
			if (principal != null) {
				RequestDispatcher dis = request.getRequestDispatcher("board/saveForm.jsp");
				dis.forward(request, response);
			} else {
				RequestDispatcher dis = request.getRequestDispatcher("user/loginForm.jsp");
				dis.forward(request, response);
			}
			
		/* 글쓰기 저장 */
		} else if (cmd.equals("save")) {
			
			// userId는 세션에서 읽어도 되지만 클라이언트에서 
			// hidden으로 숨겨서 form데이터로 받는 것 선호 (더 안정적)
			int userId = Integer.parseInt(request.getParameter("userId"));
			String title = request.getParameter("title");
			String content = request.getParameter("content");

			SaveReqDto dto = new SaveReqDto();
			dto.setUserId(userId);
			dto.setTitle(title);
			dto.setContent(content);
			  
			int result = boardService.글쓰기(dto);
			
			if (result == 1) { // 정상
				response.sendRedirect("index.jsp");  //  index.jsp는 필터에서 예외로 뺐기 때문에 이동이 가능함
			} else {
				Script.back(response, "글쓰기실패");
			}
			
		/* 글 목록 보기 */	
		} else if (cmd.equals("list")) {			
			
			int page = Integer.parseInt(request.getParameter("page"));  // 최초 : 0, Next : 1, Next: 2
			List<Board> boards = boardService.글목록보기(page);
			request.setAttribute("boards", boards);			
			
			// 계산 (전체 데이터수랑 한페이지몇개 - 총 몇페이지 나와야되는 계산) 3page라면 page의 맥스값은 2
			// page == 2가 되는 순간  isEnd = true
			// request.setAttribute("isEnd", true);
			
			int boardCount = boardService.글개수();  // 게시글 총수
			// 0페이지 부터 이므로 (boardCount-1)
			int lastPage = (boardCount-1)/4; // 2/4 = 0, 3/4 = 0, 4/4 = 1, 9/4 = 2 ( 0page, 1page, 2page) 
			// 화면단의 막대바를 표현하기 위한 식
			double currentPosition = (double)page/(lastPage)*100;
			
			request.setAttribute("lastPage", lastPage);
			request.setAttribute("currentPosition", currentPosition); // 화면단의 막대바 그리기
			
			
			// sendRedirect를 사용하면 request.setAttribute 의 내용이 모두 사라지며 필터링도 다시 거치기 때문에 
			// 사용하지 말아야 한다 
			// 대신 RequestDispatcher를 이용하여 페이지 이동을 해야 request.setAttribute 의 내용이
			// 보존되면서 필터도 다시 거치지 않고 내부적으로 이동한다.
			RequestDispatcher dis = request.getRequestDispatcher("board/list.jsp");
			dis.forward(request, response);
			
			
			
		/*글 상세보기*/	
		}else if(cmd.equals("detail")) {
			
			int id = Integer.parseInt(request.getParameter("id"));
			
			DetailRespDto dto = boardService.글상세보기(id); // board테이블+user테이블 = 조인된 데이터 필요
			List<Reply> replys = replyService.글목록보기(id);
			
			// dto를 제대로 못 가져오면 
			if(dto == null) {
				Script.back(response, "상세보기에 실패하였습니다");
			}else {
				request.setAttribute("dto", dto);
				request.setAttribute("replys", replys);		
				
				RequestDispatcher dis = request.getRequestDispatcher("board/detail.jsp");
				dis.forward(request, response);
			}			
			
			
		/* 글 삭제 */	
		}else if(cmd.equals("delete")) {	
						
			// json -> 자바오브젝트 -> json 이러한 변환과정은 
			// 나중에 reflection에서 처리할 수 있다.
			
			// 1. 요청 받은 json 데이터를 자바 오브젝트로 파싱
			// json으로 받았기 때문에 버퍼를 이용하여 읽어야 한다.
			/*
			 * BufferedReader br = request.getReader(); 
			 * String data = br.readLine(); Gson
			 * gson = new Gson(); DeleteReqDto dto = gson.fromJson(data,
			 * DeleteReqDto.class);
			 */
			
			// 1. queryString 형태로 받아서 request의 파라미터로 읽을 수 있음
			int id = Integer.parseInt(request.getParameter("id"));

			// 2. DB에서 id값으로 글 삭제
			int result = boardService.글삭제(id);
			
			// 3. 응답할 json 데이터를 생성
			CommonRespDto<String> commonRespDto = new CommonRespDto<>();
			commonRespDto.setStatusCode(result);
			commonRespDto.setData("성공");
			
			Gson gson = new Gson();
			String respData = gson.toJson(commonRespDto);
			//System.out.println("respData : "+respData);
			PrintWriter out = response.getWriter();
			out.print(respData);
			out.flush();			
			
			
		/* 글 수정 화면으로 이동 */	
		}else if(cmd.equals("updateForm")) {			
			
			int id = Integer.parseInt(request.getParameter("id"));
			DetailRespDto dto = boardService.글상세보기(id);
			request.setAttribute("dto", dto);
			RequestDispatcher dis = request.getRequestDispatcher("board/updateForm.jsp");
			dis.forward(request, response);			
			
			
		/* 글 수정 */	
		}else if(cmd.equals("update")) {
			
			int id = Integer.parseInt(request.getParameter("id")); // board id
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			
			UpdateReqDto dto = new UpdateReqDto();
			dto.setId(id);
			dto.setTitle(title);
			dto.setContent(content);
			
			int result = boardService.글수정(dto);
			
			if(result == 1) {
				// 고민해보세요. 왜 RequestDispatcher 안썻는지... 한번 써보세요. detail.jsp 호출
				// 밖으로 나갔다가 글 상세 보기를 다시 타게 해야한다. 왜냐 하면
				// 수정된 글을 새롭게 읽어서 세션에 데이터를 넣어야 하기 때문에
				// sendRedirect를 사용하면 request.setAttribute 의 내용이 모두 사라지며 
				// 필터링도 다시 거친다.
				// 대신 RequestDispatcher를 이용하여 페이지 이동을 해야 request.setAttribute 의 내용이
				// 보존되면서 필터도 다시 거치지 않고 내부적으로 이동한다. 
				response.sendRedirect("/blog/board?cmd=detail&id="+id);
			}else {
				Script.back(response,"글 수정에 실패하였습니다.");
			}	
			
			
		/* 글 검색 */
		}else if(cmd.equals("search")) {
			
			String keyword = request.getParameter("keyword");
			int page = Integer.parseInt(request.getParameter("page"));
			
			List<Board> boards = boardService.글검색(keyword, page);
			request.setAttribute("boards", boards);
			
			// read 상태 바 처리를 위한 부분
			int boardCount = boardService.글개수(keyword);					
			int lastPage = (boardCount-1)/4; // 2/4 = 0, 3/4 = 0, 4/4 = 1, 9/4 = 2 ( 0page, 1page, 2page) 
			double currentPosition = (double)page/(lastPage)*100;
			
			System.out.println("boardCount === "+boardCount);
			System.out.println("lastPage === "+lastPage);
			
			request.setAttribute("lastPage", lastPage);
			request.setAttribute("currentPosition", currentPosition);
			
			RequestDispatcher dis = request.getRequestDispatcher("board/list.jsp");
			dis.forward(request, response);
			
		}
			
	}

}
