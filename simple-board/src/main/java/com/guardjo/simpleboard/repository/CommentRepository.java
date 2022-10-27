package com.guardjo.simpleboard.repository;

import com.guardjo.simpleboard.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
