package com.lec.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lec.blog.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
