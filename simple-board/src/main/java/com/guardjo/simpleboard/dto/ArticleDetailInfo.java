package com.guardjo.simpleboard.dto;

import java.util.List;

public record ArticleDetailInfo(
	ArticleDto article,
	List<CommentDto> comments
) {
}
