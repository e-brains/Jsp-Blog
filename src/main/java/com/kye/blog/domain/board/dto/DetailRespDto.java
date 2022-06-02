package com.kye.blog.domain.board.dto;

//import java.util.List;
//import com.kye.blog.domain.reply.Reply;
import lombok.Data;

@Data
public class DetailRespDto {
	private int id;
	private String title;
	private String content;
	private int readCount;
	private String username;
	// 화면상에서 글쓴이와 로그인한 사람이 동일인 일때
	// 수정/삭제 버튼을 보여주기 위해서 추가
	private int userId; 

	// 루시 필터 적용해보기
	public String getTitle() {
		return title.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}
}
