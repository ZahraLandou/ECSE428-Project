/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package com.example.nomnomapp.model;
import jakarta.persistence.*;

import java.util.*;
import java.sql.Date;

// line 2 "model.ump"
// line 121 "model.ump"
@Entity
@Table(name = "users")
public class NomNomUser
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

  @ManyToMany
  @JoinTable(
          name = "user_roles",
          joinColumns = @JoinColumn(name = "user_username"),
          inverseJoinColumns = @JoinColumn(name = "role_username")
  )
  private List<NomNomUser> roleName = new ArrayList<>();
  @ManyToMany
  private List<NomNomUser> nomNomUsers;

  //------------------------
  // CONSTRUCTOR
  //------------------------


  public NomNomUser(String aUsername, String aEmailAddress, String aPassword)
  {
    username = aUsername;
    emailAddress = aEmailAddress;
    password = aPassword;
    biography = null;
    profilePicture = null;
    notifications = new ArrayList<Notification>();
    recipes = new ArrayList<Recipe>();
    recipeLists = new ArrayList<RecipeList>();
    roleName = new ArrayList<NomNomUser>();
    nomNomUsers = new ArrayList<NomNomUser>();
  }

  public NomNomUser() {

  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setUsername(String aUsername)
  {
    boolean wasSet = false;
    username = aUsername;
    wasSet = true;
    return wasSet;
  }

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

  public String getUsername()
  {
    return username;
  }

  public String getEmailAddress()
  {
    return emailAddress;
  }

  public String getPassword()
  {
    return password;
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
  public NomNomUser getRoleName(int index)
  {
    NomNomUser aRoleName = roleName.get(index);
    return aRoleName;
  }

  public List<NomNomUser> getRoleName()
  {
    List<NomNomUser> newRoleName = Collections.unmodifiableList(roleName);
    return newRoleName;
  }

  public int numberOfRoleName()
  {
    int number = roleName.size();
    return number;
  }

  public boolean hasRoleName()
  {
    boolean has = roleName.size() > 0;
    return has;
  }

  public int indexOfRoleName(NomNomUser aRoleName)
  {
    int index = roleName.indexOf(aRoleName);
    return index;
  }
  /* Code from template association_GetMany */
  public NomNomUser getNomNomUser(int index)
  {
    NomNomUser aNomNomUser = nomNomUsers.get(index);
    return aNomNomUser;
  }

  public List<NomNomUser> getNomNomUsers()
  {
    List<NomNomUser> newNomNomUsers = Collections.unmodifiableList(nomNomUsers);
    return newNomNomUsers;
  }

  public int numberOfNomNomUsers()
  {
    int number = nomNomUsers.size();
    return number;
  }

  public boolean hasNomNomUsers()
  {
    boolean has = nomNomUsers.size() > 0;
    return has;
  }

  public int indexOfNomNomUser(NomNomUser aNomNomUser)
  {
    int index = nomNomUsers.indexOf(aNomNomUser);
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
  public RecipeList addRecipeList(int aRecipeListID, String aName, RecipeList.ListCategory aCategory)
  {
    return new RecipeList(aRecipeListID, aName, aCategory, this);
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
  public static int minimumNumberOfRoleName()
  {
    return 0;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addRoleName(NomNomUser aRoleName)
  {
    boolean wasAdded = false;
    if (roleName.contains(aRoleName)) { return false; }
    roleName.add(aRoleName);
    if (aRoleName.indexOfNomNomUser(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aRoleName.addNomNomUser(this);
      if (!wasAdded)
      {
        roleName.remove(aRoleName);
      }
    }
    return wasAdded;
  }
  /* Code from template association_RemoveMany */
  public boolean removeRoleName(NomNomUser aRoleName)
  {
    boolean wasRemoved = false;
    if (!roleName.contains(aRoleName))
    {
      return wasRemoved;
    }

    int oldIndex = roleName.indexOf(aRoleName);
    roleName.remove(oldIndex);
    if (aRoleName.indexOfNomNomUser(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aRoleName.removeNomNomUser(this);
      if (!wasRemoved)
      {
        roleName.add(oldIndex,aRoleName);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addRoleNameAt(NomNomUser aRoleName, int index)
  {  
    boolean wasAdded = false;
    if(addRoleName(aRoleName))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRoleName()) { index = numberOfRoleName() - 1; }
      roleName.remove(aRoleName);
      roleName.add(index, aRoleName);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveRoleNameAt(NomNomUser aRoleName, int index)
  {
    boolean wasAdded = false;
    if(roleName.contains(aRoleName))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRoleName()) { index = numberOfRoleName() - 1; }
      roleName.remove(aRoleName);
      roleName.add(index, aRoleName);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addRoleNameAt(aRoleName, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfNomNomUsers()
  {
    return 0;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addNomNomUser(NomNomUser aNomNomUser)
  {
    boolean wasAdded = false;
    if (nomNomUsers.contains(aNomNomUser)) { return false; }
    nomNomUsers.add(aNomNomUser);
    if (aNomNomUser.indexOfRoleName(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aNomNomUser.addRoleName(this);
      if (!wasAdded)
      {
        nomNomUsers.remove(aNomNomUser);
      }
    }
    return wasAdded;
  }
  /* Code from template association_RemoveMany */
  public boolean removeNomNomUser(NomNomUser aNomNomUser)
  {
    boolean wasRemoved = false;
    if (!nomNomUsers.contains(aNomNomUser))
    {
      return wasRemoved;
    }

    int oldIndex = nomNomUsers.indexOf(aNomNomUser);
    nomNomUsers.remove(oldIndex);
    if (aNomNomUser.indexOfRoleName(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aNomNomUser.removeRoleName(this);
      if (!wasRemoved)
      {
        nomNomUsers.add(oldIndex,aNomNomUser);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addNomNomUserAt(NomNomUser aNomNomUser, int index)
  {  
    boolean wasAdded = false;
    if(addNomNomUser(aNomNomUser))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfNomNomUsers()) { index = numberOfNomNomUsers() - 1; }
      nomNomUsers.remove(aNomNomUser);
      nomNomUsers.add(index, aNomNomUser);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveNomNomUserAt(NomNomUser aNomNomUser, int index)
  {
    boolean wasAdded = false;
    if(nomNomUsers.contains(aNomNomUser))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfNomNomUsers()) { index = numberOfNomNomUsers() - 1; }
      nomNomUsers.remove(aNomNomUser);
      nomNomUsers.add(index, aNomNomUser);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addNomNomUserAt(aNomNomUser, index);
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
    ArrayList<NomNomUser> copyOfRoleName = new ArrayList<NomNomUser>(roleName);
    roleName.clear();
    for(NomNomUser aRoleName : copyOfRoleName)
    {
      aRoleName.removeNomNomUser(this);
    }
    ArrayList<NomNomUser> copyOfNomNomUsers = new ArrayList<NomNomUser>(nomNomUsers);
    nomNomUsers.clear();
    for(NomNomUser aNomNomUser : copyOfNomNomUsers)
    {
      aNomNomUser.removeRoleName(this);
    }
  }

  public int getUserId() {
    return userId;
  }
  public void setUserId(int userId) {
    this.userId = userId;
  }
  public String toString()
  {
    return super.toString() + "["+
            "username" + ":" + getUsername()+ "," +
            "emailAddress" + ":" + getEmailAddress()+ "," +
            "password" + ":" + getPassword()+ "," +
            "biography" + ":" + getBiography()+ "," +
            "profilePicture" + ":" + getProfilePicture()+ "]";
  }
}