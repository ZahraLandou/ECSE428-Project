/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package com.example.nomnomapp.model;
import java.util.*;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

// line 69 "model.ump"
// line 116 "model.ump"

@Entity
public class RecipeList
{
  public RecipeList() {

  }

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum ListCategory { Likes, Favorites, Regular }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //RecipeList Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int recipeListID;
  private String name;


  private ListCategory category;

  //RecipeList Associations
  private NomNomUser nomNomUser;

  @ManyToMany(mappedBy = "recipeLists")
  private List<Recipe> recipes;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public RecipeList(int aRecipeListID, String aName, ListCategory aCategory, NomNomUser aNomNomUser)
  {
    recipeListID = aRecipeListID;
    name = aName;
    category = aCategory;
    boolean didAddNomNomUser = setNomNomUser(aNomNomUser);
    if (!didAddNomNomUser)
    {
      throw new RuntimeException("Unable to create recipeList due to nomNomUser. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    recipes = new ArrayList<Recipe>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setRecipeListID(int aRecipeListID)
  {
    boolean wasSet = false;
    recipeListID = aRecipeListID;
    wasSet = true;
    return wasSet;
  }

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setCategory(ListCategory aCategory)
  {
    boolean wasSet = false;
    category = aCategory;
    wasSet = true;
    return wasSet;
  }

  public int getRecipeListID()
  {
    return recipeListID;
  }

  public String getName()
  {
    return name;
  }

  public ListCategory getCategory()
  {
    return category;
  }
  /* Code from template association_GetOne */
  public NomNomUser getNomNomUser()
  {
    return nomNomUser;
  }
  /* Code from template association_GetMany */
  public Recipe getRecipe(int index)
  {
    Recipe aRecipe = recipes.get(index);
    return aRecipe;
  }

  public List<Recipe> getRecipes()
  {
    List<Recipe> newRecipes = Collections.unmodifiableList(recipes);
    return newRecipes;
  }

  public int numberOfRecipes()
  {
    int number = recipes.size();
    return number;
  }

  public boolean hasRecipes()
  {
    boolean has = recipes.size() > 0;
    return has;
  }

  public int indexOfRecipe(Recipe aRecipe)
  {
    int index = recipes.indexOf(aRecipe);
    return index;
  }
  /* Code from template association_SetOneToMany */
  public boolean setNomNomUser(NomNomUser aNomNomUser)
  {
    boolean wasSet = false;
    if (aNomNomUser == null)
    {
      return wasSet;
    }

    NomNomUser existingNomNomUser = nomNomUser;
    nomNomUser = aNomNomUser;
    if (existingNomNomUser != null && !existingNomNomUser.equals(aNomNomUser))
    {
      existingNomNomUser.removeRecipeList(this);
    }
    nomNomUser.addRecipeList(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfRecipes()
  {
    return 0;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addRecipe(Recipe aRecipe)
  {
    boolean wasAdded = false;
    if (recipes.contains(aRecipe)) { return false; }
    recipes.add(aRecipe);
    if (aRecipe.indexOfRecipeList(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aRecipe.addRecipeList(this);
      if (!wasAdded)
      {
        recipes.remove(aRecipe);
      }
    }
    return wasAdded;
  }
  /* Code from template association_RemoveMany */
  public boolean removeRecipe(Recipe aRecipe)
  {
    boolean wasRemoved = false;
    if (!recipes.contains(aRecipe))
    {
      return wasRemoved;
    }

    int oldIndex = recipes.indexOf(aRecipe);
    recipes.remove(oldIndex);
    if (aRecipe.indexOfRecipeList(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aRecipe.removeRecipeList(this);
      if (!wasRemoved)
      {
        recipes.add(oldIndex,aRecipe);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addRecipeAt(Recipe aRecipe, int index)
  {  
    boolean wasAdded = false;
    if(addRecipe(aRecipe))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRecipes()) { index = numberOfRecipes() - 1; }
      recipes.remove(aRecipe);
      recipes.add(index, aRecipe);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveRecipeAt(Recipe aRecipe, int index)
  {
    boolean wasAdded = false;
    if(recipes.contains(aRecipe))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRecipes()) { index = numberOfRecipes() - 1; }
      recipes.remove(aRecipe);
      recipes.add(index, aRecipe);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addRecipeAt(aRecipe, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    NomNomUser placeholderNomNomUser = nomNomUser;
    this.nomNomUser = null;
    if(placeholderNomNomUser != null)
    {
      placeholderNomNomUser.removeRecipeList(this);
    }
    ArrayList<Recipe> copyOfRecipes = new ArrayList<Recipe>(recipes);
    recipes.clear();
    for(Recipe aRecipe : copyOfRecipes)
    {
      aRecipe.removeRecipeList(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "recipeID" + ":" + getRecipeListID()+ "," +
            "name" + ":" + getName()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "category" + "=" + (getCategory() != null ? !getCategory().equals(this)  ? getCategory().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "nomNomUser = "+(getNomNomUser()!=null?Integer.toHexString(System.identityHashCode(getNomNomUser())):"null");
  }
}