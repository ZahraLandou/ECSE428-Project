/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package com.example.nomnomapp.model;
import java.util.*;

// line 55 "model.ump"
// line 98 "model.ump"
public class Ingredient
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Ingredient Attributes
  private String name;
  private String type;

  //Ingredient Associations
  private List<RecipeIngredients> recipeIngredients;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Ingredient(String aName, String aType)
  {
    name = aName;
    type = aType;
    recipeIngredients = new ArrayList<RecipeIngredients>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setType(String aType)
  {
    boolean wasSet = false;
    type = aType;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }

  public String getType()
  {
    return type;
  }
  /* Code from template association_GetMany */
  public RecipeIngredients getRecipeIngredient(int index)
  {
    RecipeIngredients aRecipeIngredient = recipeIngredients.get(index);
    return aRecipeIngredient;
  }

  public List<RecipeIngredients> getRecipeIngredients()
  {
    List<RecipeIngredients> newRecipeIngredients = Collections.unmodifiableList(recipeIngredients);
    return newRecipeIngredients;
  }

  public int numberOfRecipeIngredients()
  {
    int number = recipeIngredients.size();
    return number;
  }

  public boolean hasRecipeIngredients()
  {
    boolean has = recipeIngredients.size() > 0;
    return has;
  }

  public int indexOfRecipeIngredient(RecipeIngredients aRecipeIngredient)
  {
    int index = recipeIngredients.indexOf(aRecipeIngredient);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfRecipeIngredients()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public RecipeIngredients addRecipeIngredient(int aRecipeIngredientID, double aQuantity, String aUnit, Recipe aRecipe)
  {
    return new RecipeIngredients(aRecipeIngredientID, aQuantity, aUnit, aRecipe, this);
  }

  public boolean addRecipeIngredient(RecipeIngredients aRecipeIngredient)
  {
    boolean wasAdded = false;
    if (recipeIngredients.contains(aRecipeIngredient)) { return false; }
    Ingredient existingIngredient = aRecipeIngredient.getIngredient();
    boolean isNewIngredient = existingIngredient != null && !this.equals(existingIngredient);
    if (isNewIngredient)
    {
      aRecipeIngredient.setIngredient(this);
    }
    else
    {
      recipeIngredients.add(aRecipeIngredient);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeRecipeIngredient(RecipeIngredients aRecipeIngredient)
  {
    boolean wasRemoved = false;
    //Unable to remove aRecipeIngredient, as it must always have a ingredient
    if (!this.equals(aRecipeIngredient.getIngredient()))
    {
      recipeIngredients.remove(aRecipeIngredient);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addRecipeIngredientAt(RecipeIngredients aRecipeIngredient, int index)
  {  
    boolean wasAdded = false;
    if(addRecipeIngredient(aRecipeIngredient))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRecipeIngredients()) { index = numberOfRecipeIngredients() - 1; }
      recipeIngredients.remove(aRecipeIngredient);
      recipeIngredients.add(index, aRecipeIngredient);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveRecipeIngredientAt(RecipeIngredients aRecipeIngredient, int index)
  {
    boolean wasAdded = false;
    if(recipeIngredients.contains(aRecipeIngredient))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRecipeIngredients()) { index = numberOfRecipeIngredients() - 1; }
      recipeIngredients.remove(aRecipeIngredient);
      recipeIngredients.add(index, aRecipeIngredient);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addRecipeIngredientAt(aRecipeIngredient, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    while (recipeIngredients.size() > 0)
    {
      RecipeIngredients aRecipeIngredient = recipeIngredients.get(recipeIngredients.size() - 1);
      aRecipeIngredient.delete();
      recipeIngredients.remove(aRecipeIngredient);
    }
    
  }


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "type" + ":" + getType()+ "]";
  }
}