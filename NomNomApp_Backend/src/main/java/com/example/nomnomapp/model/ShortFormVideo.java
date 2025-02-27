/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


package com.example.nomnomapp.model;
// line 47 "model.ump"
// line 93 "model.ump"

import jakarta.persistence.*;

@Entity
public class ShortFormVideo
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ShortFormVideo Attributes

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int videoId;
  
  private String videoTitle;
  private String videoDescription;
  private String video;

  @OneToOne(mappedBy = "shortFormVideo")
  private Recipe recipe;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public ShortFormVideo(int aVideoId, String aVideoTitle, String aVideoDescription, String aVideo, Recipe aRecipe)
  {
    videoId = aVideoId;
    videoTitle = aVideoTitle;
    videoDescription = aVideoDescription;
    video = aVideo;
    boolean didAddRecipe = setRecipe(aRecipe);
    if (!didAddRecipe)
    {
      throw new RuntimeException("Unable to create shortFormVideo due to recipe. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setVideoId(int aVideoId)
  {
    boolean wasSet = false;
    videoId = aVideoId;
    wasSet = true;
    return wasSet;
  }

  public boolean setVideoTitle(String aVideoTitle)
  {
    boolean wasSet = false;
    videoTitle = aVideoTitle;
    wasSet = true;
    return wasSet;
  }

  public boolean setVideoDescription(String aVideoDescription)
  {
    boolean wasSet = false;
    videoDescription = aVideoDescription;
    wasSet = true;
    return wasSet;
  }

  public boolean setVideo(String aVideo)
  {
    boolean wasSet = false;
    video = aVideo;
    wasSet = true;
    return wasSet;
  }

  public int getVideoId()
  {
    return videoId;
  }

  public String getVideoTitle()
  {
    return videoTitle;
  }

  public String getVideoDescription()
  {
    return videoDescription;
  }

  public String getVideo()
  {
    return video;
  }
  /* Code from template association_GetOne */
  public Recipe getRecipe()
  {
    return recipe;
  }
  /* Code for the updated setRecipe method as the non-owner side */
  public boolean setRecipe(Recipe aRecipe)
  {
    boolean wasSet = false;
    
    // As the non-owner side, we only update our reference
    // and don't directly manipulate the other side's reference
    
    // If we're removing the association
    if (aRecipe == null) {
      // If we have a current recipe, have the recipe remove this shortFormVideo
      Recipe existingRecipe = recipe;
      if (existingRecipe != null && existingRecipe.getShortFormVideo() == this) {
        existingRecipe.setShortFormVideo(null);
      }
      recipe = null;
      wasSet = true;
      return wasSet;
    }
    
    // Setting to a new recipe
    recipe = aRecipe;
    
    // If the new recipe doesn't have this shortFormVideo as its shortFormVideo,
    // and we're not already being set by the recipe (to avoid infinite recursion)
    if (aRecipe.getShortFormVideo() != this) {
      aRecipe.setShortFormVideo(this);
    }
    
    // Clean up old recipe if needed (will happen through Recipe's setShortFormVideo)
    
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Recipe existingRecipe = recipe;
    recipe = null;
    if (existingRecipe != null)
    {
      existingRecipe.setShortFormVideo(null);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "videoId" + ":" + getVideoId()+ "," +
            "videoTitle" + ":" + getVideoTitle()+ "," +
            "videoDescription" + ":" + getVideoDescription()+ "," +
            "video" + ":" + getVideo()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "recipe = "+(getRecipe()!=null?Integer.toHexString(System.identityHashCode(getRecipe())):"null");
  }
}