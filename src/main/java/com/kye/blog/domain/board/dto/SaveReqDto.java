package com.kye.blog.domain.board.dto;

import lombok.Data;

@Data
public class SaveReqDto {
	private int UserId;
	private String title;
	private String content;

}
