/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


package com.example.nomnomapp.model;
// line 62 "model.ump"
// line 111 "model.ump"

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class RecipeIngredients
{


  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //RecipeIngredients Attributes
  @Id
  @GeneratedValue(strategy =  GenerationType.IDENTITY)
  private int recipeIngredientID;

  private double quantity;
  private String unit;

  //RecipeIngredients Associations
  
  // Many-to-One relationship with Recipe
  @ManyToOne
  @JoinColumn(name = "recipeID", nullable = false) // foreign key in RecipeIngredients table
  private Recipe recipe;

  // Many-to-One relationship with Ingredient
  @ManyToOne
  @JoinColumn(name = "ingredientName", nullable = false) // foreign key in RecipeIngredients table
  private Ingredient ingredient;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public RecipeIngredients(int aRecipeIngredientID, double aQuantity, String aUnit, Recipe aRecipe, Ingredient aIngredient)
  {
    recipeIngredientID = aRecipeIngredientID;
    quantity = aQuantity;
    unit = aUnit;
    boolean didAddRecipe = setRecipe(aRecipe);
    if (!didAddRecipe)
    {
      throw new RuntimeException("Unable to create recipeIngredient due to recipe. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddIngredient = setIngredient(aIngredient);
    if (!didAddIngredient)
    {
      throw new RuntimeException("Unable to create recipeIngredient due to ingredient. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }
  public RecipeIngredients(){

  }

  public RecipeIngredients(double aQuantity, String aUnit, Recipe aRecipe, Ingredient aIngredient)
  {
    quantity = aQuantity;
    unit = aUnit;
    boolean didAddRecipe = setRecipe(aRecipe);
    if (!didAddRecipe)
    {
      throw new RuntimeException("Unable to create recipeIngredient due to recipe. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddIngredient = setIngredient(aIngredient);
    if (!didAddIngredient)
    {
      throw new RuntimeException("Unable to create recipeIngredient due to ingredient. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setRecipeIngredientID(int aRecipeIngredientID)
  {
    boolean wasSet = false;
    recipeIngredientID = aRecipeIngredientID;
    wasSet = true;
    return wasSet;
  }

  public boolean setQuantity(double aQuantity)
  {
    boolean wasSet = false;
    quantity = aQuantity;
    wasSet = true;
    return wasSet;
  }

  public boolean setUnit(String aUnit)
  {
    boolean wasSet = false;
    unit = aUnit;
    wasSet = true;
    return wasSet;
  }

  public int getRecipeIngredientID()
  {
    return recipeIngredientID;
  }

  public double getQuantity()
  {
    return quantity;
  }

  public String getUnit()
  {
    return unit;
  }
  /* Code from template association_GetOne */
  public Recipe getRecipe()
  {
    return recipe;
  }
  /* Code from template association_GetOne */
  public Ingredient getIngredient()
  {
    return ingredient;
  }
  /* Code from template association_SetOneToMany */
  public boolean setRecipe(Recipe aRecipe)
  {
    boolean wasSet = false;
    if (aRecipe == null)
    {
      return wasSet;
    }

    Recipe existingRecipe = recipe;
    recipe = aRecipe;
    if (existingRecipe != null && !existingRecipe.equals(aRecipe))
    {
      existingRecipe.removeRecipeIngredient(this);
    }
    recipe.addRecipeIngredient(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setIngredient(Ingredient aIngredient)
  {
    boolean wasSet = false;
    if (aIngredient == null)
    {
      return wasSet;
    }

    Ingredient existingIngredient = ingredient;
    ingredient = aIngredient;
    if (existingIngredient != null && !existingIngredient.equals(aIngredient))
    {
      existingIngredient.removeRecipeIngredient(this);
    }
    ingredient.addRecipeIngredient(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Recipe placeholderRecipe = recipe;
    this.recipe = null;
    if(placeholderRecipe != null)
    {
      placeholderRecipe.removeRecipeIngredient(this);
    }
    Ingredient placeholderIngredient = ingredient;
    this.ingredient = null;
    if(placeholderIngredient != null)
    {
      placeholderIngredient.removeRecipeIngredient(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "recipeIngredientID" + ":" + getRecipeIngredientID()+ "," +
            "quantity" + ":" + getQuantity()+ "," +
            "unit" + ":" + getUnit()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "recipe = "+(getRecipe()!=null?Integer.toHexString(System.identityHashCode(getRecipe())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "ingredient = "+(getIngredient()!=null?Integer.toHexString(System.identityHashCode(getIngredient())):"null");
  }
}