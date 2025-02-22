/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package com.example.nomnomapp.model;
import jakarta.persistence.*;

import java.sql.Date;

// line 32 "model.ump"
// line 82 "model.ump"
@Entity
@Table(name = "notifications")
public class Notification
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Notification Attributes
  private String description;
  private Date date;

  //Notification Associations
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private NomNomUser nomNomUser;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Notification(String aDescription, Date aDate, NomNomUser aNomNomUser)
  {
    description = aDescription;
    date = aDate;
    boolean didAddNomNomUser = setNomNomUser(aNomNomUser);
    if (!didAddNomNomUser)
    {
      throw new RuntimeException("Unable to create notification due to nomNomUser. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  public Notification() {

  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDescription(String aDescription)
  {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public boolean setDate(Date aDate)
  {
    boolean wasSet = false;
    date = aDate;
    wasSet = true;
    return wasSet;
  }

  public String getDescription()
  {
    return description;
  }

  public Date getDate()
  {
    return date;
  }
  /* Code from template association_GetOne */
  public NomNomUser getNomNomUser()
  {
    return nomNomUser;
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
      existingNomNomUser.removeNotification(this);
    }
    nomNomUser.addNotification(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    NomNomUser placeholderNomNomUser = nomNomUser;
    this.nomNomUser = null;
    if(placeholderNomNomUser != null)
    {
      placeholderNomNomUser.removeNotification(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "description" + ":" + getDescription()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "date" + "=" + (getDate() != null ? !getDate().equals(this)  ? getDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "nomNomUser = "+(getNomNomUser()!=null?Integer.toHexString(System.identityHashCode(getNomNomUser())):"null");
  }
}