package com.example.nomnomapp.repository;

import com.example.nomnomapp.model.Comment;
import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer> {
    Comment findCommentByCommentId(Integer commentId);

    Optional<Comment> findByCommentContentAndNomNomUserAndRecipe(String commentContent, NomNomUser nomNomUser, Recipe recipe);
}
