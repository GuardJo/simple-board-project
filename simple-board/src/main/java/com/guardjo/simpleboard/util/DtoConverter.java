package com.guardjo.simpleboard.util;

import java.util.Set;
import java.util.stream.Collectors;

import com.guardjo.simpleboard.domain.Article;
import com.guardjo.simpleboard.domain.Comment;
import com.guardjo.simpleboard.domain.Hashtag;
import com.guardjo.simpleboard.domain.Member;
import com.guardjo.simpleboard.dto.ArticleDetailInfo;
import com.guardjo.simpleboard.dto.ArticleDto;
import com.guardjo.simpleboard.dto.ArticleWithCommentDto;
import com.guardjo.simpleboard.dto.CommentDto;
import com.guardjo.simpleboard.dto.HashtagDto;
import com.guardjo.simpleboard.dto.MemberDto;
import com.guardjo.simpleboard.dto.security.SimpleBoardPrincipal;

public class DtoConverter {
	public static ArticleDto from(Article article) {
		return ArticleDto.of(
			article.getId(),
			article.getCreator(),
			article.getCreateTime(),
			article.getTitle(),
			article.getContent(),
			DtoConverter.from(article.getHashtags())
		);
	}

	public static CommentDto from(Comment comment) {
		CommentDto commentDto = CommentDto.of(
			comment.getId(),
			comment.getArticle().getId(),
			comment.getParentCommentId(),
			comment.getCreator(),
			comment.getCreateTime(),
			comment.getContent()
		);

		if (comment.hasChildComments()) {
			commentDto.addAllChildComments(comment.getChildComments().stream()
				.map(DtoConverter::from).collect(Collectors.toSet()));
		}

		return commentDto;
	}

	public static MemberDto from(Member member) {
		return MemberDto.of(
			member.getEmail(),
			member.getName(),
			member.getPassword()
		);
	}

	public static ArticleWithCommentDto fromArticleWithComment(Article article) {
		return ArticleWithCommentDto.of(
			article.getId(),
			article.getCreator(),
			article.getCreateTime(),
			article.getModifier(),
			article.getModifiedTime(),
			article.getTitle(),
			article.getContent(),
			from(article.getHashtags()),
			from(article.getMember()),
			from(excludeChildComment(article.getComments()))
		);
	}

	public static MemberDto from(SimpleBoardPrincipal principal) {
		return MemberDto.of(principal.email(), principal.name(), principal.password());
	}

	public static HashtagDto from(Hashtag hashtag) {
		return HashtagDto.of(
			hashtag.getId(),
			hashtag.getHashtagName()
		);
	}

	public static ArticleDetailInfo toArticleDetailInfo(Article article) {
		return new ArticleDetailInfo(
			DtoConverter.from(article),
			article.getComments().stream()
				.map(DtoConverter::from)
				.toList()
		);
	}

	public static Set from(Set set) {
		return (Set)set.stream()
			.map((t) -> {
				if (t instanceof Hashtag) {
					return from((Hashtag)t);
				} else {
					return from((Comment)t);
				}
			})
			.collect(Collectors.toSet());
	}

	private static Set<Comment> excludeChildComment(Set<Comment> comments) {
		return comments.stream()
			.filter(comment -> comment.getParentCommentId() == null)
			.collect(Collectors.toSet());
	}
}
