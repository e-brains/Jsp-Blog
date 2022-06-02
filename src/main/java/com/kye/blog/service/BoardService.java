package com.kye.blog.service;


import java.util.List;
import com.kye.blog.domain.board.*;
import com.kye.blog.domain.board.dto.*;

public class BoardService {
	
	private BoardDao boardDao;

	public BoardService() {
		boardDao = new BoardDao();
	}
	
	public int 글쓰기(SaveReqDto dto) {		
		return boardDao.save(dto);
	}
	
	public List<Board>글목록보기(int page){
		return boardDao.findAll(page );
	}
	
	public DetailRespDto 글상세보기(int id) {
		// 조회수 업데이트
		int result = boardDao.updateReadCount(id);
		
		// 조회수 증가가 정상적이면 
		if (result == 1) {
			return boardDao.findById(id);
		// 조회수 증가가 비정상적이면
		}else { 
			return null;
		}
	}
	
	public List<Board>글검색(String keyword, int page){
		return boardDao.findByKeyword(keyword, page );
	}	
	
	public int 글삭제(int id) {
		return boardDao.deleteById(id);
	}
	
	public int 글수정(UpdateReqDto dto) {
		return boardDao.update(dto);
	}
		
	/* 글 목록 읽기 count */
	public int 글개수() {
		return boardDao.count();		
	}
	
	/* 글 검색의 count */
	public int 글개수(String keyword) {
		return boardDao.count( keyword);		
	}
	
	
	
}
