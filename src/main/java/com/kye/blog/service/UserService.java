package com.kye.blog.service;

import com.kye.blog.domain.user.User;
import com.kye.blog.domain.user.UserDao;
import com.kye.blog.domain.user.dto.JoinReqDto;
import com.kye.blog.domain.user.dto.LoginReqDto;
import com.kye.blog.domain.user.dto.UpdateReqDto;

/**  회원가입 , 회원수정, 로그인 , 아이디중복체크  **/
public class UserService {

	private UserDao userDao; 
	
	// UserService가 생성될때 UserDao 객체 를 생성하면 메서드 마다 
	// new로 객체를 생성하는 코드를 넣을 필요 없다.
	public UserService() {
		userDao = new UserDao();
	}
	
	public int 회원가입(JoinReqDto dto) {
		int result = userDao.save(dto);
		return result;
	}
	
	public User 로그인(LoginReqDto dto) {
		return userDao.findByUsernameAndPassword(dto);
	}
	
	public int 회원수정(UpdateReqDto dto) {
		
		return -1;
	}

	public int 유저네임중복체크(String username) {
		int result = userDao.findByUsername(username);
		return result;
	}
	
}
