package com.kye.blog.domain.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.kye.blog.config.DB;
import com.kye.blog.domain.user.dto.JoinReqDto;
import com.kye.blog.domain.user.dto.LoginReqDto;

public class UserDao {
	
	// login에서 사용
	public User findByUsernameAndPassword(LoginReqDto dto) {
		String sql = "SELECT id, username, email, address FROM user WHERE username = ? AND password = ?";
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs  = null;
		try {
			pstmt = conn.prepareStatement(sql);  // prepareStatement()는 injection공격을 자동으로 막아줌
			pstmt.setString(1, dto.getUsername());
			pstmt.setString(2, dto.getPassword());
			rs =  pstmt.executeQuery();
			
			// 스프링에서는 Persistence API가 자동으로 해준다.
			// 즉, 쿼리만 날리면 알아서 결과물을 User오브젝트에 담아준다.
			if(rs.next()) {
				// User오브젝트로 만들어서 리턴한다. 
				User user = User.builder()
						.id(rs.getInt("id"))
						.username(rs.getString("username"))
						.email(rs.getString("email"))
						.address(rs.getString("address"))
						.build();   // new 없이도 객체를 만들어 준다.
				return user;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 무조건 실행
			DB.close(conn, pstmt, rs);
		}
		
		return null;
	}
	
	// id 중복 체크 
	// id 하나이고 클라이언트에서 텍스트 형태로 하나만 보내서 버퍼로 읽어서 
	// 인자 형태로 넘겨옴 그래서 DTO를 사용하지 않음
	public int findByUsername(String username) {
		String sql = "SELECT * FROM user WHERE username = ?";
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs  = null;  // select이므로 RestsuluSet 사용하여 결과 값 받음
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			rs =  pstmt.executeQuery();  // select -> executeQuery 사용
			
			if(rs.next()) {  // 값이 하나라도 있으면 중복
				return 1; // 있어
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 무조건 실행
			DB.close(conn, pstmt, rs);
		}
		return -1; // 없어
	}
	
	public int save(JoinReqDto dto) { // 회원가입
		String sql = "INSERT INTO user(username, password, email, address, userRole, createDate) VALUES(?,?,?,?, 'USER', now())";
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getUsername());
			pstmt.setString(2, dto.getPassword());
			pstmt.setString(3, dto.getEmail());
			pstmt.setString(4, dto.getAddress());
			int result = pstmt.executeUpdate();   // insert, update, delete -> executeUpdate 사용
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 무조건 실행
			DB.close(conn, pstmt);
		}
		return -1;
	}
	
	public void update() { // 회원수정
		
	}
	
	public void usernameCheck() { // 아이디 중복 체크
		
	}
	
	public void findById() { // 회원정보보기
		
	}
}
