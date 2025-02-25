package com.example.nomnomapp.repository;

import com.example.nomnomapp.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer> {
    Comment findCommentByCommentId(Integer commentId);


}
