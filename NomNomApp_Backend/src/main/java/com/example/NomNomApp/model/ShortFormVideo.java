/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


package com.example.nomnomapp.model;
// line 47 "model.ump"
// line 93 "model.ump"
public class ShortFormVideo
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ShortFormVideo Attributes
  private int videoId;
  private String videoTitle;
  private String videoDescription;
  private String video;

  //ShortFormVideo Associations
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
  /* Code from template association_SetOneToOptionalOne */
  public boolean setRecipe(Recipe aNewRecipe)
  {
    boolean wasSet = false;
    if (aNewRecipe == null)
    {
      //Unable to setRecipe to null, as shortFormVideo must always be associated to a recipe
      return wasSet;
    }
    
    ShortFormVideo existingShortFormVideo = aNewRecipe.getShortFormVideo();
    if (existingShortFormVideo != null && !equals(existingShortFormVideo))
    {
      //Unable to setRecipe, the current recipe already has a shortFormVideo, which would be orphaned if it were re-assigned
      return wasSet;
    }
    
    Recipe anOldRecipe = recipe;
    recipe = aNewRecipe;
    recipe.setShortFormVideo(this);

    if (anOldRecipe != null)
    {
      anOldRecipe.setShortFormVideo(null);
    }
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