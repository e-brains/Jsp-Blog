package com.kye.blog.domain.board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.kye.blog.config.DB;
import com.kye.blog.domain.board.dto.DetailRespDto;
import com.kye.blog.domain.board.dto.SaveReqDto;
import com.kye.blog.domain.board.dto.UpdateReqDto;
import com.kye.blog.domain.user.User;

public class BoardDao {
	
	/* 글쓰기 저장 */
	public int save(SaveReqDto dto) { 
		
		String sql = "INSERT INTO board(userId, title, content, createDate) VALUES(?,?,?, now())";
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getUserId());
			pstmt.setString(2, dto.getTitle());
			pstmt.setString(3, dto.getContent());
			int result = pstmt.executeUpdate();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 무조건 실행
			DB.close(conn, pstmt);
		}
		return -1;
	}
	
	/* 글 목록 읽기 */
	public List<Board> findAll(int page){
		// ORDER BY id DESC ->최근 글이 먼저 오도록 한다.
		// Oracle에서는 inline View를 써서 rownum을 사용했는데 mysql은 LIMIT 사용 
		// 페이징을 위하여 LIMIT를 사용, LIMIT 시작번호, 갯수 
		// LIMIT 0, 4 첫줄 부터 4개  read, LIMIT 4, 4 4번째 줄 부터 4개 read
		String sql = "SELECT * FROM  board ORDER BY id DESC LIMIT ?, 4"; // 0,4   4,4   8,4
		
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs  = null;  // select이므로
		List<Board> boards = new ArrayList<>();  // 목록 담을 리스트
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, page*4); // 0 -> 0, 1 ->4, 2->8
			rs =  pstmt.executeQuery();
			
			// Persistence API
			while(rs.next()) { // 커서를 이동하는 함수
				Board board = Board.builder()
						.id(rs.getInt("id"))
						.title(rs.getString("title"))
						.content(rs.getString("content"))
						.readCount(rs.getInt("readCount"))
						.userId(rs.getInt("userId"))
						.createDate(rs.getTimestamp("createDate"))
						.build();
				boards.add(board);	
			}
			return boards;
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 무조건 실행
			DB.close(conn, pstmt, rs);
		}
		
		return null;
	}
	
	/* 글목록 읽기 count */
	public int count() {
		String sql = "SELECT count(*) FROM board";
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs  = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			rs =  pstmt.executeQuery();
			if(rs.next()) {  // 한건이기 때문에 while이 아니다.
				return rs.getInt(1); // index는 시작번호가 1 부터 시작
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 무조건 실행
			DB.close(conn, pstmt, rs);
		}
		return -1;
	}
	
	/* 글상세 보기 */
	public DetailRespDto findById(int id){
		// board를 읽으면서 username을 가져와야 하니까 user테이블과 조인 필요
		// select b.id, b.title, b.content, b.readCount, b.userId, u.username
		// from board b inner join user u on b.userId = u.id where b.id = ? 
		StringBuffer sb = new StringBuffer();
		
		// 마지막에 공백 한칸 주는 것 잊지 말것
		sb.append("select b.id, b.title, b.content, b.readCount, b.userId, u.username ");
		sb.append("from board b inner join user u "); 
		sb.append("on b.userId = u.id ");
		sb.append("where b.id = ?");
		
		String sql = sb.toString();
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs  = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs =  pstmt.executeQuery();
			
			// Persistence API
			if(rs.next()) { // 커서를 이동하는 함수
				DetailRespDto dto = new DetailRespDto();
				dto.setId(rs.getInt("b.id"));
				dto.setTitle(rs.getString("b.title"));
				dto.setContent(rs.getString("b.content"));
				dto.setReadCount(rs.getInt("b.readCount"));
				dto.setUserId(rs.getInt("b.userId"));
				dto.setUsername(rs.getString("u.username"));
				return dto;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 무조건 실행
			DB.close(conn, pstmt, rs);
		}
		return null;
	}
	
	/* 상세보기 시 조회수 증가 */
	public int updateReadCount(int id) {
		String sql = "UPDATE board SET readCount = readCount+1 WHERE id = ?";
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			int result = pstmt.executeUpdate();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 무조건 실행
			DB.close(conn, pstmt);
		}
		return -1;
	}
	
	/* 글 삭제 */
	public int deleteById(int id) {
		String sql = "DELETE FROM board WHERE id = ?";
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			int result = pstmt.executeUpdate();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 무조건 실행
			DB.close(conn, pstmt);
		}
		return -1;
	}
	
	/* 글 수정 */
	public int update(UpdateReqDto dto) {
		String sql = "UPDATE board SET title = ?, content = ? WHERE id = ?";
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getContent());
			pstmt.setInt(3, dto.getId());
			int result = pstmt.executeUpdate();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 무조건 실행
			DB.close(conn, pstmt);
		}
		return -1;
	}
	
	/* 글검색 */
	public List<Board> findByKeyword(String keyword, int page){
		String sql = "SELECT * FROM  board WHERE title like ? ORDER BY id DESC LIMIT ?, 4"; // 0,4   4,4   8,4
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs  = null;
		List<Board> boards = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%"+keyword+"%"); // %기호는 반드시 여기서 넣어야 한다. sql에 넣으면 안됨
			pstmt.setInt(2, page*4); // 0 -> 0, 1 ->4, 2->8
			rs =  pstmt.executeQuery();
			
			// Persistence API
			while(rs.next()) { // 커서를 이동하는 함수
				Board board = Board.builder()
						.id(rs.getInt("id"))
						.title(rs.getString("title"))
						.content(rs.getString("content"))
						.readCount(rs.getInt("readCount"))
						.userId(rs.getInt("userId"))
						.createDate(rs.getTimestamp("createDate"))
						.build();
				boards.add(board);	
			}
			return boards;
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 무조건 실행
			DB.close(conn, pstmt, rs);
		}
		
		return null;
	}
	
	/* 글검색 count */
	public int count(String keyword) {
		String sql = "SELECT count(*) FROM board WHERE title like ?";
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs  = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%"+keyword+"%");
			rs =  pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 무조건 실행
			DB.close(conn, pstmt, rs);
		}
		return -1;
	}
	
}
