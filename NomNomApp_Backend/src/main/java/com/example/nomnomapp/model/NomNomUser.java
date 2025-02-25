/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package com.example.nomnomapp.model;
import jakarta.persistence.*;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.sql.Date;

// line 2 "model.ump"
// line 121 "model.ump"
@Entity
@Table(name = "users")
public class NomNomUser implements Serializable
{
  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //NomNomUser Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int userId;

  @Column(nullable = false, unique = true)
  private String username;
  @Column(nullable = false, unique = true)
  private String emailAddress;
  @Column(nullable = false)
  private String password;
  private String biography;
  private String profilePicture;

  //NomNomUser Associations
  @OneToMany(mappedBy = "nomNomUser", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Notification> notifications = new ArrayList<>();
  @OneToMany(mappedBy = "nomNomUser", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Recipe> recipes = new ArrayList<>();

  @OneToMany(mappedBy = "nomNomUser", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RecipeList> recipeLists = new ArrayList<>();

  @OneToMany(mappedBy = "nomNomUser", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments;

  @ManyToMany
  @JoinTable(
      name = "user_followers",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "follower_id")
  )
  @JsonIgnore
private List<NomNomUser> followers;

@ManyToMany(mappedBy = "followers")
@JsonIgnore
private List<NomNomUser> following;
  //------------------------
  // CONSTRUCTOR
  //------------------------
  public NomNomUser() {

  }
  public NomNomUser(String aUsername,String aEmailAddress, String aPassword)
  {
    username=aUsername;
    emailAddress = aEmailAddress;
    password = aPassword;
    biography = null;
    profilePicture = null;
    notifications = new ArrayList<Notification>();
    recipes = new ArrayList<Recipe>();
    recipeLists = new ArrayList<RecipeList>();
    following = new ArrayList<NomNomUser>();
    followers = new ArrayList<NomNomUser>();
    comments = new ArrayList<Comment>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setEmailAddress(String aEmailAddress)
  {
    boolean wasSet = false;
    emailAddress = aEmailAddress;
    wasSet = true;
    return wasSet;
  }

  public boolean setPassword(String aPassword)
  {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }

  public boolean setBiography(String aBiography)
  {
    boolean wasSet = false;
    biography = aBiography;
    wasSet = true;
    return wasSet;
  }

  public boolean setProfilePicture(String aProfilePicture)
  {
    boolean wasSet = false;
    profilePicture = aProfilePicture;
    wasSet = true;
    return wasSet;
  }

  public String getEmailAddress()
  {
    return emailAddress;
  }

  public String getPassword()
  {
    return password;
  }
  public String getUsername()
  {
    return username;
  }

  public String getBiography()
  {
    return biography;
  }

  public String getProfilePicture()
  {
    return profilePicture;
  }
  /* Code from template association_GetMany */
  public Notification getNotification(int index)
  {
    Notification aNotification = notifications.get(index);
    return aNotification;
  }

  public List<Notification> getNotifications()
  {
    List<Notification> newNotifications = Collections.unmodifiableList(notifications);
    return newNotifications;
  }

  public int numberOfNotifications()
  {
    int number = notifications.size();
    return number;
  }

  public boolean hasNotifications()
  {
    boolean has = notifications.size() > 0;
    return has;
  }

  public int indexOfNotification(Notification aNotification)
  {
    int index = notifications.indexOf(aNotification);
    return index;
  }
  /* Code from template association_GetMany */
  public Recipe getRecipe(int index)
  {
    Recipe aRecipe = recipes.get(index);
    return aRecipe;
  }
 public void setUserId(int id){
  this.userId=id;
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
  /* Code from template association_GetMany */
  public NomNomUser getFollowing(int index)
  {
    NomNomUser aFollowing = following.get(index);
    return aFollowing;
  }

  public List<NomNomUser> getFollowing()
  {
    List<NomNomUser> newFollowing = Collections.unmodifiableList(following);
    return newFollowing;
  }

  public int numberOfFollowing()
  {
    int number = following.size();
    return number;
  }

  public boolean hasFollowing()
  {
    boolean has = following.size() > 0;
    return has;
  }

  public int indexOfFollowing(NomNomUser aFollowing)
  {
    int index = following.indexOf(aFollowing);
    return index;
  }
  /* Code from template association_GetMany */
  public NomNomUser getFollower(int index)
  {
    NomNomUser aFollower = followers.get(index);
    return aFollower;
  }

  public List<NomNomUser> getFollowers()
  {
    List<NomNomUser> newFollowers = Collections.unmodifiableList(followers);
    return newFollowers;
  }

  public int numberOfFollowers()
  {
    int number = followers.size();
    return number;
  }

  public boolean hasFollowers()
  {
    boolean has = followers.size() > 0;
    return has;
  }

  public int indexOfFollower(NomNomUser aFollower)
  {
    int index = followers.indexOf(aFollower);
    return index;
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
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfNotifications()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Notification addNotification(String aDescription, Date aDate)
  {
    return new Notification(aDescription, aDate, this);
  }

  public boolean addNotification(Notification aNotification)
  {
    boolean wasAdded = false;
    if (notifications.contains(aNotification)) { return false; }
    NomNomUser existingNomNomUser = aNotification.getNomNomUser();
    boolean isNewNomNomUser = existingNomNomUser != null && !this.equals(existingNomNomUser);
    if (isNewNomNomUser)
    {
      aNotification.setNomNomUser(this);
    }
    else
    {
      notifications.add(aNotification);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeNotification(Notification aNotification)
  {
    boolean wasRemoved = false;
    //Unable to remove aNotification, as it must always have a nomNomUser
    if (!this.equals(aNotification.getNomNomUser()))
    {
      notifications.remove(aNotification);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addNotificationAt(Notification aNotification, int index)
  {  
    boolean wasAdded = false;
    if(addNotification(aNotification))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfNotifications()) { index = numberOfNotifications() - 1; }
      notifications.remove(aNotification);
      notifications.add(index, aNotification);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveNotificationAt(Notification aNotification, int index)
  {
    boolean wasAdded = false;
    if(notifications.contains(aNotification))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfNotifications()) { index = numberOfNotifications() - 1; }
      notifications.remove(aNotification);
      notifications.add(index, aNotification);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addNotificationAt(aNotification, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfRecipes()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Recipe addRecipe(int aRecipeID, String aTitle, String aDescription, String aInstructions, Date aCreationDate, Recipe.RecipeCategory aCategory, int aLikes, String aPicture, double aAverageRating)
  {
    return new Recipe(aRecipeID, aTitle, aDescription, aInstructions, aCreationDate, aCategory, aLikes, aPicture, aAverageRating, this);
  }

  public boolean addRecipe(Recipe aRecipe)
  {
    boolean wasAdded = false;
    if (recipes.contains(aRecipe)) { return false; }
    NomNomUser existingNomNomUser = aRecipe.getNomNomUser();
    boolean isNewNomNomUser = existingNomNomUser != null && !this.equals(existingNomNomUser);
    if (isNewNomNomUser)
    {
      aRecipe.setNomNomUser(this);
    }
    else
    {
      recipes.add(aRecipe);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeRecipe(Recipe aRecipe)
  {
    boolean wasRemoved = false;
    //Unable to remove aRecipe, as it must always have a nomNomUser
    if (!this.equals(aRecipe.getNomNomUser()))
    {
      recipes.remove(aRecipe);
      wasRemoved = true;
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
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfRecipeLists()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public RecipeList addRecipeList(int aRecipeID, String aName, RecipeList.ListCategory aCategory)
  {
    return new RecipeList(aRecipeID, aName, aCategory, this);
  }

  public boolean addRecipeList(RecipeList aRecipeList)
  {
    boolean wasAdded = false;
    if (recipeLists.contains(aRecipeList)) { return false; }
    NomNomUser existingNomNomUser = aRecipeList.getNomNomUser();
    boolean isNewNomNomUser = existingNomNomUser != null && !this.equals(existingNomNomUser);
    if (isNewNomNomUser)
    {
      aRecipeList.setNomNomUser(this);
    }
    else
    {
      recipeLists.add(aRecipeList);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeRecipeList(RecipeList aRecipeList)
  {
    boolean wasRemoved = false;
    //Unable to remove aRecipeList, as it must always have a nomNomUser
    if (!this.equals(aRecipeList.getNomNomUser()))
    {
      recipeLists.remove(aRecipeList);
      wasRemoved = true;
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
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfFollowing()
  {
    return 0;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addFollowing(NomNomUser aFollowing)
  {
    boolean wasAdded = false;
    if (following.contains(aFollowing)) { return false; }
    following.add(aFollowing);
    if (aFollowing.indexOfFollower(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aFollowing.addFollower(this);
      if (!wasAdded)
      {
        following.remove(aFollowing);
      }
    }
    return wasAdded;
  }
  /* Code from template association_RemoveMany */
  public boolean removeFollowing(NomNomUser aFollowing)
  {
    boolean wasRemoved = false;
    if (!following.contains(aFollowing))
    {
      return wasRemoved;
    }

    int oldIndex = following.indexOf(aFollowing);
    following.remove(oldIndex);
    if (aFollowing.indexOfFollower(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aFollowing.removeFollower(this);
      if (!wasRemoved)
      {
        following.add(oldIndex,aFollowing);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addFollowingAt(NomNomUser aFollowing, int index)
  {  
    boolean wasAdded = false;
    if(addFollowing(aFollowing))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfFollowing()) { index = numberOfFollowing() - 1; }
      following.remove(aFollowing);
      following.add(index, aFollowing);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveFollowingAt(NomNomUser aFollowing, int index)
  {
    boolean wasAdded = false;
    if(following.contains(aFollowing))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfFollowing()) { index = numberOfFollowing() - 1; }
      following.remove(aFollowing);
      following.add(index, aFollowing);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addFollowingAt(aFollowing, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfFollowers()
  {
    return 0;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addFollower(NomNomUser aFollower)
  {
    boolean wasAdded = false;
    if (followers.contains(aFollower)) { return false; }
    followers.add(aFollower);
    if (aFollower.indexOfFollowing(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aFollower.addFollowing(this);
      if (!wasAdded)
      {
        followers.remove(aFollower);
      }
    }
    return wasAdded;
  }
  /* Code from template association_RemoveMany */
  public boolean removeFollower(NomNomUser aFollower)
  {
    boolean wasRemoved = false;
    if (!followers.contains(aFollower))
    {
      return wasRemoved;
    }

    int oldIndex = followers.indexOf(aFollower);
    followers.remove(oldIndex);
    if (aFollower.indexOfFollowing(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aFollower.removeFollowing(this);
      if (!wasRemoved)
      {
        followers.add(oldIndex,aFollower);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addFollowerAt(NomNomUser aFollower, int index)
  {  
    boolean wasAdded = false;
    if(addFollower(aFollower))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfFollowers()) { index = numberOfFollowers() - 1; }
      followers.remove(aFollower);
      followers.add(index, aFollower);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveFollowerAt(NomNomUser aFollower, int index)
  {
    boolean wasAdded = false;
    if(followers.contains(aFollower))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfFollowers()) { index = numberOfFollowers() - 1; }
      followers.remove(aFollower);
      followers.add(index, aFollower);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addFollowerAt(aFollower, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfComments()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Comment addComment(int aCommentId, String aCommentContent, Date aCreationDate, double aRating, Recipe aRecipe)
  {
    return new Comment(aCommentId, aCommentContent, aCreationDate, aRating, this, aRecipe);
  }

  public boolean addComment(Comment aComment)
  {
    boolean wasAdded = false;
    if (comments.contains(aComment)) { return false; }
    NomNomUser existingNomNomUser = aComment.getNomNomUser();
    boolean isNewNomNomUser = existingNomNomUser != null && !this.equals(existingNomNomUser);
    if (isNewNomNomUser)
    {
      aComment.setNomNomUser(this);
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
    //Unable to remove aComment, as it must always have a nomNomUser
    if (!this.equals(aComment.getNomNomUser()))
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
    while (notifications.size() > 0)
    {
      Notification aNotification = notifications.get(notifications.size() - 1);
      aNotification.delete();
      notifications.remove(aNotification);
    }
    
    for(int i=recipes.size(); i > 0; i--)
    {
      Recipe aRecipe = recipes.get(i - 1);
      aRecipe.delete();
    }
    for(int i=recipeLists.size(); i > 0; i--)
    {
      RecipeList aRecipeList = recipeLists.get(i - 1);
      aRecipeList.delete();
    }
    ArrayList<NomNomUser> copyOfFollowing = new ArrayList<NomNomUser>(following);
    following.clear();
    for(NomNomUser aFollowing : copyOfFollowing)
    {
      aFollowing.removeFollower(this);
    }
    ArrayList<NomNomUser> copyOfFollowers = new ArrayList<NomNomUser>(followers);
    followers.clear();
    for(NomNomUser aFollower : copyOfFollowers)
    {
      aFollower.removeFollowing(this);
    }
    for(int i=comments.size(); i > 0; i--)
    {
      Comment aComment = comments.get(i - 1);
      aComment.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "emailAddress" + ":" + getEmailAddress()+ "," +
            "password" + ":" + getPassword()+ "," +
            "biography" + ":" + getBiography()+ "," +
            "profilePicture" + ":" + getProfilePicture()+ "]";
  }  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 4 "model.ump"
  

  
}