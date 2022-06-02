package com.kye.blog.service;

import java.util.List;

import com.kye.blog.domain.board.dto.CommonRespDto;
import com.kye.blog.domain.reply.Reply;
import com.kye.blog.domain.reply.ReplyDao;
import com.kye.blog.domain.reply.dto.SaveReqDto;


public class ReplyService {

	private ReplyDao replyDao;
	
	public ReplyService() {
		replyDao = new ReplyDao();
	}
	
	public int 댓글삭제(int id) {
		return replyDao.deleteById(id);
	}
	
	public List<Reply> 글목록보기(int boardId){
		return replyDao.findAll(boardId);
	}
	
	public CommonRespDto<Reply> 댓글쓰기및목록(SaveReqDto dto) {
		
		//Reply reply = 댓글찾기(dto.getBoardId());
		CommonRespDto<Reply> commonRespDto = new CommonRespDto<>();
		Reply reply = null;
		int resultId = replyDao.save(dto);	
		
		// 저당된 댓글의  id 로 넘어오니까 조건을 " -1 이 아니면" 으로 해야 됨
		// 저장 실패가 아니면
		if(resultId != -1) {
			reply = replyDao.findById(resultId);
			commonRespDto.setStatusCode(1); //1, -1 중 성공했으니 1
			commonRespDto.setData(reply); // 저장된 id의 댓글 데이터 (UI에서 비동기로 데이터 뿌리기 위해)
		}else { // 실패
			commonRespDto.setStatusCode(-1); //1, -1
		}
		return commonRespDto;
	}
	
	public Reply 댓글찾기(int id) {
		return replyDao.findById(id);
	}
}
