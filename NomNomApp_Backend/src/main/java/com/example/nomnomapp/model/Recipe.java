/*Code partially generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package com.example.nomnomapp.model;
import java.sql.Date;
import java.util.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Recipe
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum RecipeCategory { Breakfast, Dinner, Brunch, Lunch, Dessert }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Recipe Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int recipeID;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;

  @Lob
  private String instructions;

  @Column(name= "created_date", nullable = false, updatable = false)
  @CreatedDate
  private Date creationDate;

  private RecipeCategory category;
  private int likes;
  private String picture;
  private double averageRating;

  //Recipe Associations
  @OneToOne
  @JoinColumn(name = "videoId")
  private ShortFormVideo shortFormVideo;

  @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RecipeIngredients> recipeIngredients;

  @ManyToMany
  @JoinTable(
    name = "List_of_recipes",
    joinColumns = @JoinColumn(name = "recipe_id"),
    inverseJoinColumns = @JoinColumn(name = "recipe_list_id")
  )
  private List<RecipeList> recipeLists;

  
  private NomNomUser nomNomUser;

  @OneToMany(mappedBy = "recipe")
  private List<Comment> comments;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Recipe(){
    
  }
  public Recipe(int aRecipeID, String aTitle, String aDescription, String aInstructions, Date aCreationDate, RecipeCategory aCategory, int aLikes, String aPicture, double aAverageRating, NomNomUser aNomNomUser)
  {
    recipeID = aRecipeID;
    title = aTitle;
    description = aDescription;
    instructions = aInstructions;
    creationDate = aCreationDate;
    category = aCategory;
    likes = aLikes;
    picture = aPicture;
    averageRating = aAverageRating;
    recipeIngredients = new ArrayList<RecipeIngredients>();
    recipeLists = new ArrayList<RecipeList>();
    boolean didAddNomNomUser = setNomNomUser(aNomNomUser);
    if (!didAddNomNomUser)
    {
      throw new RuntimeException("Unable to create recipe due to nomNomUser. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    comments = new ArrayList<Comment>();
  } 

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setRecipeID(int aRecipeID)
  {
    boolean wasSet = false;
    recipeID = aRecipeID;
    wasSet = true;
    return wasSet;
  } 


  public boolean setTitle(String aTitle)
  {
    boolean wasSet = false;
    title = aTitle;
    wasSet = true;
    return wasSet;
  }

  public boolean setDescription(String aDescription)
  {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public boolean setInstructions(String aInstructions)
  {
    boolean wasSet = false;
    instructions = aInstructions;
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

  public boolean setCategory(RecipeCategory aCategory)
  {
    boolean wasSet = false;
    category = aCategory;
    wasSet = true;
    return wasSet;
  }

  public boolean setLikes(int aLikes)
  {
    boolean wasSet = false;
    likes = aLikes;
    wasSet = true;
    return wasSet;
  }

  public boolean setPicture(String aPicture)
  {
    boolean wasSet = false;
    picture = aPicture;
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

  public int getRecipeID()
  {
    return recipeID;
  }

  public String getTitle()
  {
    return title;
  }

  public String getDescription()
  {
    return description;
  }

  public String getInstructions()
  {
    return instructions;
  }

  public Date getCreationDate()
  {
    return creationDate;
  }

  public RecipeCategory getCategory()
  {
    return category;
  }

  public int getLikes()
  {
    return likes;
  }

  public String getPicture()
  {
    return picture;
  }

  public double getAverageRating()
  {
    return averageRating;
  }
  /* Code from template association_GetOne */
  public ShortFormVideo getShortFormVideo()
  {
    return shortFormVideo;
  }

  public boolean hasShortFormVideo()
  {
    boolean has = shortFormVideo != null;
    return has;
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
  /* Code from template association_GetMany */
  public RecipeList getRecipeList(int index)
  {
    RecipeList aRecipeList = recipeLists.get(index);
    return aRecipeList;
  }

  public List<RecipeList> getRecipeLists()
  {
    List<RecipeList> newRecipeLists = Collections.unmodifiableList(recipeLists);
    return newRecipeLists;
  }

  public int numberOfRecipeLists()
  {
    int number = recipeLists.size();
    return number;
  }

  public boolean hasRecipeLists()
  {
    boolean has = recipeLists.size() > 0;
    return has;
  }

  public int indexOfRecipeList(RecipeList aRecipeList)
  {
    int index = recipeLists.indexOf(aRecipeList);
    return index;
  }
  /* Code from template association_GetOne */
  public NomNomUser getNomNomUser()
  {
    return nomNomUser;
  }
  /* Code from template association_GetMany */
  public Comment getComment(int index)
  {
    Comment aComment = comments.get(index);
    return aComment;
  }

  public List<Comment> getComments()
  {
    List<Comment> newComments = Collections.unmodifiableList(comments);
    return newComments;
  }

  public int numberOfComments()
  {
    int number = comments.size();
    return number;
  }

  public boolean hasComments()
  {
    boolean has = comments.size() > 0;
    return has;
  }

  public int indexOfComment(Comment aComment)
  {
    int index = comments.indexOf(aComment);
    return index;
  }
  /* Code from template association_SetOptionalOneToOne */
  public boolean setShortFormVideo(ShortFormVideo aNewShortFormVideo)
  {
    boolean wasSet = false;
    if (shortFormVideo != null && !shortFormVideo.equals(aNewShortFormVideo) && equals(shortFormVideo.getRecipe()))
    {
      //Unable to setShortFormVideo, as existing shortFormVideo would become an orphan
      return wasSet;
    }

    shortFormVideo = aNewShortFormVideo;
    Recipe anOldRecipe = aNewShortFormVideo != null ? aNewShortFormVideo.getRecipe() : null;

    if (!this.equals(anOldRecipe))
    {
      if (anOldRecipe != null)
      {
        anOldRecipe.shortFormVideo = null;
      }
      if (shortFormVideo != null)
      {
        shortFormVideo.setRecipe(this);
      }
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfRecipeIngredients()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public RecipeIngredients addRecipeIngredient(int aRecipeIngredientID, double aQuantity, String aUnit, Ingredient aIngredient)
  {
    return new RecipeIngredients(aRecipeIngredientID, aQuantity, aUnit, this, aIngredient);
  }

  public boolean addRecipeIngredient(RecipeIngredients aRecipeIngredient)
  {
    boolean wasAdded = false;
    if (recipeIngredients.contains(aRecipeIngredient)) { return false; }
    Recipe existingRecipe = aRecipeIngredient.getRecipe();
    boolean isNewRecipe = existingRecipe != null && !this.equals(existingRecipe);
    if (isNewRecipe)
    {
      aRecipeIngredient.setRecipe(this);
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
    //Unable to remove aRecipeIngredient, as it must always have a recipe
    if (!this.equals(aRecipeIngredient.getRecipe()))
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
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfRecipeLists()
  {
    return 0;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addRecipeList(RecipeList aRecipeList)
  {
    boolean wasAdded = false;
    if (recipeLists.contains(aRecipeList)) { return false; }
    recipeLists.add(aRecipeList);
    if (aRecipeList.indexOfRecipe(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aRecipeList.addRecipe(this);
      if (!wasAdded)
      {
        recipeLists.remove(aRecipeList);
      }
    }
    return wasAdded;
  }
  /* Code from template association_RemoveMany */
  public boolean removeRecipeList(RecipeList aRecipeList)
  {
    boolean wasRemoved = false;
    if (!recipeLists.contains(aRecipeList))
    {
      return wasRemoved;
    }

    int oldIndex = recipeLists.indexOf(aRecipeList);
    recipeLists.remove(oldIndex);
    if (aRecipeList.indexOfRecipe(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aRecipeList.removeRecipe(this);
      if (!wasRemoved)
      {
        recipeLists.add(oldIndex,aRecipeList);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addRecipeListAt(RecipeList aRecipeList, int index)
  {  
    boolean wasAdded = false;
    if(addRecipeList(aRecipeList))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRecipeLists()) { index = numberOfRecipeLists() - 1; }
      recipeLists.remove(aRecipeList);
      recipeLists.add(index, aRecipeList);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveRecipeListAt(RecipeList aRecipeList, int index)
  {
    boolean wasAdded = false;
    if(recipeLists.contains(aRecipeList))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRecipeLists()) { index = numberOfRecipeLists() - 1; }
      recipeLists.remove(aRecipeList);
      recipeLists.add(index, aRecipeList);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addRecipeListAt(aRecipeList, index);
    }
    return wasAdded;
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
      existingNomNomUser.removeRecipe(this);
    }
    nomNomUser.addRecipe(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfComments()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Comment addComment(int aCommentId, String aCommentContent, Date aCreationDate, double aAverageRating)
  {
    return new Comment(aCommentId, aCommentContent, aCreationDate, aAverageRating, this);
  }

  public boolean addComment(Comment aComment)
  {
    boolean wasAdded = false;
    if (comments.contains(aComment)) { return false; }
    Recipe existingRecipe = aComment.getRecipe();
    boolean isNewRecipe = existingRecipe != null && !this.equals(existingRecipe);
    if (isNewRecipe)
    {
      aComment.setRecipe(this);
    }
    else
    {
      comments.add(aComment);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeComment(Comment aComment)
  {
    boolean wasRemoved = false;
    //Unable to remove aComment, as it must always have a recipe
    if (!this.equals(aComment.getRecipe()))
    {
      comments.remove(aComment);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCommentAt(Comment aComment, int index)
  {  
    boolean wasAdded = false;
    if(addComment(aComment))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfComments()) { index = numberOfComments() - 1; }
      comments.remove(aComment);
      comments.add(index, aComment);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCommentAt(Comment aComment, int index)
  {
    boolean wasAdded = false;
    if(comments.contains(aComment))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfComments()) { index = numberOfComments() - 1; }
      comments.remove(aComment);
      comments.add(index, aComment);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCommentAt(aComment, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    ShortFormVideo existingShortFormVideo = shortFormVideo;
    shortFormVideo = null;
    if (existingShortFormVideo != null)
    {
      existingShortFormVideo.delete();
      existingShortFormVideo.setRecipe(null);
    }
    for(int i=recipeIngredients.size(); i > 0; i--)
    {
      RecipeIngredients aRecipeIngredient = recipeIngredients.get(i - 1);
      aRecipeIngredient.delete();
    }
    ArrayList<RecipeList> copyOfRecipeLists = new ArrayList<RecipeList>(recipeLists);
    recipeLists.clear();
    for(RecipeList aRecipeList : copyOfRecipeLists)
    {
      aRecipeList.removeRecipe(this);
    }
    NomNomUser placeholderNomNomUser = nomNomUser;
    this.nomNomUser = null;
    if(placeholderNomNomUser != null)
    {
      placeholderNomNomUser.removeRecipe(this);
    }
    while (comments.size() > 0)
    {
      Comment aComment = comments.get(comments.size() - 1);
      aComment.delete();
      comments.remove(aComment);
    }
    
  }


  public String toString()
  {
    return super.toString() + "["+
            "recipeID" + ":" + getRecipeID()+ "," +
            "title" + ":" + getTitle()+ "," +
            "description" + ":" + getDescription()+ "," +
            "instructions" + ":" + getInstructions()+ "," +
            "likes" + ":" + getLikes()+ "," +
            "picture" + ":" + getPicture()+ "," +
            "averageRating" + ":" + getAverageRating()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "creationDate" + "=" + (getCreationDate() != null ? !getCreationDate().equals(this)  ? getCreationDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "category" + "=" + (getCategory() != null ? !getCategory().equals(this)  ? getCategory().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "shortFormVideo = "+(getShortFormVideo()!=null?Integer.toHexString(System.identityHashCode(getShortFormVideo())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "nomNomUser = "+(getNomNomUser()!=null?Integer.toHexString(System.identityHashCode(getNomNomUser())):"null");
  }
}