package com.example.nomnomapp.service;

import com.example.nomnomapp.exception.NomNomException;
import com.example.nomnomapp.model.Comment;
import com.example.nomnomapp.model.NomNomUser;
import com.example.nomnomapp.model.Recipe;
import com.example.nomnomapp.repository.CommentRepository;
import com.example.nomnomapp.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

@Service
public class CommentService {
    @Autowired
    private CommentRepository repo;
    @Autowired
    private RecipeRepository recipeRepo;

    @Autowired
    private RecipeService recipeService;

    /**
     * creates comment
     * @throws IllegalArgumentException if the fields are not valid.
     */
    @Transactional
    public Comment createComment( String commentContent, double rating, NomNomUser user,Recipe aRecipe) {

        
        if (commentContent.isEmpty()){
            //throw new NomNomException(HttpStatus.BAD_REQUEST, "Comment cannot be empty.");
            throw new IllegalArgumentException("Comment cannot be empty.");
        }
        if(rating<0 ||rating>5){
            throw new NomNomException(HttpStatus.BAD_REQUEST, "Rating must be between 0 and 5.");
        }
        if(recipeRepo.findByRecipeId(aRecipe.getRecipeID())==null){
            throw new NomNomException(HttpStatus.NOT_FOUND, "Recipe does not exist.");
        }
       // NomNomUser user = aUser.get();
        Date today=Date.valueOf(LocalDate.now());
        Comment c = new Comment(commentContent,today,rating,user, aRecipe);
        recipeService.updateAverageRating(aRecipe.getRecipeID());

        return repo.save(c);
    }
    @Transactional
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

        Date today=Date.valueOf(LocalDate.now());
        Comment c = repo.findCommentByCommentId(aCommentId);
        c.setCommentContent(aCommentContent);
        c.setRating(rating);
        c.setCreationDate(today);
        return repo.save(c);
    }

    public Comment getCommentById(int aCommentId) {

        if (aCommentId<0) {
            throw new NomNomException(HttpStatus.BAD_REQUEST, "Comment ID is not valid.");
        }
        Comment c= repo.findCommentByCommentId(aCommentId);
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
        Comment c= repo.findCommentByCommentId(aCommentId);
        if(c==null){
            throw new NomNomException(HttpStatus.NOT_FOUND,"Comment does not exist");
        }
        c.delete();
        repo.deleteById(aCommentId);
    }
    public Iterable<Comment> getAllComments() {
        return repo.findAll();
    }

    public void deleteAllComments() {

        repo.deleteAll();
    }
    

}