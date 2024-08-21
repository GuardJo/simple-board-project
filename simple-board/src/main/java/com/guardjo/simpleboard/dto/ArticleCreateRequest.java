package com.guardjo.simpleboard.dto;

public record ArticleCreateRequest(
	String title,
	String content
) {
}
