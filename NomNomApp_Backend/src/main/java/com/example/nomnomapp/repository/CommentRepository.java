package com.example.nomnomapp.repository;

import com.example.nomnomapp.model.Comment;
import com.example.nomnomapp.model.NomNomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer> {
    Comment findCommentByCommentId(Integer commentId);


}
