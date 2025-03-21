package com.example.nomnomapp.controller;
import java.util.List;

import com.example.nomnomapp.model.Comment;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService service;

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment){
        return ResponseEntity.ok(service.createComment(comment.getCommentContent(),comment.getRating(),comment.getNomNomUser(),comment.getRecipe()));
    }
    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@RequestBody Comment comment, @PathVariable int commentId){
        return ResponseEntity.ok(service.updateComment(commentId, comment.getCommentContent(), comment.getRating()));
    }
    @GetMapping("/{commentId}")
    public ResponseEntity<Comment> getCommentById(@PathVariable int commentId) {
        return ResponseEntity.ok(service.getCommentById(commentId));
    }
    @GetMapping
    public ResponseEntity<List<Comment>> getAllComments() {
        return ResponseEntity.ok(service.getAllComments());
    }

    @DeleteMapping("/{commentId}")
    public void deleteCommentById(@PathVariable int commentId) {
        service.deleteComment(commentId);
    }

}
