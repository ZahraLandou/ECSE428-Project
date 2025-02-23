/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package com.example.nomnomapp.model;
import java.sql.Date;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;


// line 38 "model.ump"
// line 88 "model.ump"
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Comment
{

  //------------------------

  // MEMBER VARIABLES
  //------------------------

  //Comment Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int commentId;

  private String commentContent;
  private Date creationDate;
  private double rating;
  @ManyToOne
  private NomNomUser nomNomUser;
  //Comment Associations
  @ManyToOne
  private Recipe recipe;

  //------------------------
  // CONSTRUCTOR
  //------------------------
  public Comment(){}

  public Comment(int aCommentId, String aCommentContent, Date aCreationDate, double aRating, NomNomUser aNomNomUser, Recipe aRecipe)
  {
    commentId = aCommentId;
    commentContent = aCommentContent;
    creationDate = aCreationDate;
    rating = aRating;
    boolean didAddNomNomUser = setNomNomUser(aNomNomUser);
    if (!didAddNomNomUser)
    {
      throw new RuntimeException("Unable to create comment due to nomNomUser. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
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

  public boolean setRating(double aRating)
  {
    boolean wasSet = false;
    rating = aRating;
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

  public double getRating()
  {
    return rating;
  }
  /* Code from template association_GetOne */
  public NomNomUser getNomNomUser()
  {
    return nomNomUser;
  }
  /* Code from template association_GetOne */
  public Recipe getRecipe()
  {
    return recipe;
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
      existingNomNomUser.removeComment(this);
    }
    nomNomUser.addComment(this);
    wasSet = true;
    return wasSet;
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
    NomNomUser placeholderNomNomUser = nomNomUser;
    this.nomNomUser = null;
    if(placeholderNomNomUser != null)
    {
      placeholderNomNomUser.removeComment(this);
    }
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
            "rating" + ":" + getRating()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "creationDate" + "=" + (getCreationDate() != null ? !getCreationDate().equals(this)  ? getCreationDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "nomNomUser = "+(getNomNomUser()!=null?Integer.toHexString(System.identityHashCode(getNomNomUser())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "recipe = "+(getRecipe()!=null?Integer.toHexString(System.identityHashCode(getRecipe())):"null");
  }
}