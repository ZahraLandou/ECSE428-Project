/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package com.example.nomnomapp.model;
import java.sql.Date;

// line 38 "model.ump"
// line 88 "model.ump"
public class Comment
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Comment Attributes
  private int commentId;
  private String commentContent;
  private Date creationDate;
  private double averageRating;

  //Comment Associations
  private Recipe recipe;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Comment(int aCommentId, String aCommentContent, Date aCreationDate, double aAverageRating, Recipe aRecipe)
  {
    commentId = aCommentId;
    commentContent = aCommentContent;
    creationDate = aCreationDate;
    averageRating = aAverageRating;
    boolean didAddRecipe = setRecipe(aRecipe);
    if (!didAddRecipe)
    {
      throw new RuntimeException("Unable to create comment due to recipe. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setCommentId(int aCommentId)
  {
    boolean wasSet = false;
    commentId = aCommentId;
    wasSet = true;
    return wasSet;
  }

  public boolean setCommentContent(String aCommentContent)
  {
    boolean wasSet = false;
    commentContent = aCommentContent;
    wasSet = true;
    return wasSet;
  }

  public boolean setCreationDate(Date aCreationDate)
  {
    boolean wasSet = false;
    creationDate = aCreationDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setAverageRating(double aAverageRating)
  {
    boolean wasSet = false;
    averageRating = aAverageRating;
    wasSet = true;
    return wasSet;
  }

  public int getCommentId()
  {
    return commentId;
  }

  public String getCommentContent()
  {
    return commentContent;
  }

  public Date getCreationDate()
  {
    return creationDate;
  }

  public double getAverageRating()
  {
    return averageRating;
  }
  /* Code from template association_GetOne */
  public Recipe getRecipe()
  {
    return recipe;
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
      existingRecipe.removeComment(this);
    }
    recipe.addComment(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Recipe placeholderRecipe = recipe;
    this.recipe = null;
    if(placeholderRecipe != null)
    {
      placeholderRecipe.removeComment(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "commentId" + ":" + getCommentId()+ "," +
            "commentContent" + ":" + getCommentContent()+ "," +
            "averageRating" + ":" + getAverageRating()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "creationDate" + "=" + (getCreationDate() != null ? !getCreationDate().equals(this)  ? getCreationDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "recipe = "+(getRecipe()!=null?Integer.toHexString(System.identityHashCode(getRecipe())):"null");
  }
}