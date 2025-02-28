package com.example.nomnomapp.service;

import com.example.nomnomapp.exception.NomNomException;
import com.example.nomnomapp.model.Comment;
import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.repository.CommentRepository;
import com.example.nomnomapp.repository.RecipeRepository;
import com.example.nomnomapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * creates comment
     * @throws IllegalArgumentException if the fields are not valid.
     */
    public Comment createComment(String aUsername, String aCommentContent, double aRating, int aRecipeId) {

        if (aCommentContent.isEmpty()){
            throw new NomNomException(HttpStatus.BAD_REQUEST, "Comment cannot be empty.");
        }
        if(aRating<0 || aRating>5){
            throw new NomNomException(HttpStatus.BAD_REQUEST, "Rating must be between 0 and 5.");
        }
        Recipe recipe = recipeRepository.findByRecipeId(aRecipeId);
        if(recipe==null){
            throw new NomNomException(HttpStatus.NOT_FOUND, "Recipe does not exist.");
        }
        Optional<NomNomUser> optionalUser = userRepository.findByUsername(aUsername);
        if(!optionalUser.isPresent()){
            throw new NomNomException(HttpStatus.NOT_FOUND, "User does not exist.");
        }

        NomNomUser user = optionalUser.get();
        Comment c = new Comment(
                            aCommentContent,
                            aRating,
                            user,
                            recipe
                            );
        return commentRepository.save(c);
    }

    public Comment updateComment(int aCommentId, String aCommentContent) {
        if (aCommentId<0) {
            throw new NomNomException(HttpStatus.BAD_REQUEST, "Comment ID is not valid.");
        }
        if (aCommentContent.isEmpty()){
            throw new NomNomException(HttpStatus.BAD_REQUEST, "Comment cannot be empty.");
        }

        Comment c = commentRepository.findCommentByCommentId(aCommentId);
        c.setCommentContent(aCommentContent);
        return commentRepository.save(c);
    }

    public Comment updateComment(int aCommentId, double rating) {
        if (aCommentId<0) {
            throw new NomNomException(HttpStatus.BAD_REQUEST, "Comment ID is not valid.");
        }
        if(rating<0 ||rating>5){
            throw new NomNomException(HttpStatus.BAD_REQUEST, "Rating must be between 0 and 5.");
        }

        Comment c = commentRepository.findCommentByCommentId(aCommentId);
        c.setRating(rating);
        return commentRepository.save(c);
    }

    public Comment updateComment(int aCommentId, String aCommentContent, double rating) {
        if (aCommentId<0) {
            throw new NomNomException(HttpStatus.BAD_REQUEST, "Comment ID is not valid.");
        }
        if (aCommentContent.isEmpty()){
            throw new NomNomException(HttpStatus.BAD_REQUEST, "Comment cannot be empty.");
        }
        if(rating<0 ||rating>5){
            throw new NomNomException(HttpStatus.BAD_REQUEST, "Rating must be between 0 and 5.");
        }

        Comment c = commentRepository.findCommentByCommentId(aCommentId);
        c.setCommentContent(aCommentContent);
        c.setRating(rating);
        return commentRepository.save(c);
    }

    public Comment getCommentById(int aCommentId) {

        if (aCommentId<0) {
            throw new NomNomException(HttpStatus.BAD_REQUEST, "Comment ID is not valid.");
        }
        Comment c= commentRepository.findCommentByCommentId(aCommentId);
        if(c==null){
            throw new NomNomException(HttpStatus.NOT_FOUND,"Comment does not exist");
        }
        return c;
    }

    @Transactional
    public void deleteComment(int aCommentId) {

        if (aCommentId<0) {
            throw new NomNomException(HttpStatus.BAD_REQUEST, "Comment ID is not valid.");
        }
        Comment c= commentRepository.findCommentByCommentId(aCommentId);
        if(c==null){
            throw new NomNomException(HttpStatus.NOT_FOUND,"Comment does not exist");
        }
        c.delete();
        commentRepository.deleteById(aCommentId);
    }
    public Iterable<Comment> getAllComments() {
        return commentRepository.findAll();
    }
    

}
