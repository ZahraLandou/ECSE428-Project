package com.example.nomnomapp.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ratings")
public class Rating implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ratingId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private NomNomUser nomNomUser; // Reference to the user who gave the rating

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe; // Reference to the rated recipe

    @Column(nullable = false)
    private int ratingValue;
    public Rating() {}

    public Rating(NomNomUser nomNomUser, Recipe recipe, int ratingValue) {
        this.nomNomUser = nomNomUser;
        this.recipe = recipe;
        this.setRatingValue(ratingValue);
    }

    public int getRatingId() {
        return ratingId;
    }

    public NomNomUser getNomNomUser() {
        return nomNomUser;
    }

    public void setNomNomUser(NomNomUser nomNomUser) {
        this.nomNomUser = nomNomUser;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public boolean setRatingValue(int ratingValue) {
        if (ratingValue < 0 || ratingValue > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5.");
        }
        this.ratingValue = ratingValue;
        return true;
    }

    @Override
    public String toString() {
        return "Rating [ratingId=" + ratingId +
                ", user=" + nomNomUser.getUsername() +
                ", recipe=" + recipe.getTitle() +
                ", ratingValue=" + ratingValue + "]";
    }
}
