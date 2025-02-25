Feature: Add/Update Comment
  As a NomNom user, I want to comment on a recipe, so that I can give feedback to the poster

  Background:
    Given the following Users exist in the system
       | username | emailAddress     | password |
        | Coco     | coco@gmail.com   | pass1    |
       | Tim      | tim@gmail.com    | pass2    |
      | Jim     | jim@gmail.com   | pass3    |

    Given the following Recipe exists in the system 
      | title | description                          | instructions  | creationDate | category | likes | picture  | averageRating | recipeNomNomUser |
      | Steak | The best steak you will ever make  | Cook the steak | 2025-02-23   | Lunch    | 0     | picture  | 0.0             | Coco             |

  Scenario Outline: Successfully add a comment to a recipe
    When user with username "<username>" attempts to add a new comment with rating "<rating>" and content "<commentContent>" to an existing recipe "<recipeTitle>"
    Then the rating of the recipe "<recipeTitle>" should be "<expectedRating>"
    Then the number of comments for recipe "<recipeTitle>" in the system shall be "<numberOfComments>"
    Then the comment created by user "<nomNomUser>" for recipe "<recipeTitle>",  content "<commentContent>", and rating "<rating>" shall exist in the system 

    Examples:
       | commentContent           | rating | username | recipeTitle | expectedRating| numberOfComments|
       | This is a great recipe    | 4.0    | Coco      | Steak        | 4.0          |1|
       | Love it!   | 3.0    | Jim      | Steak        |3.5                           |2|

  Scenario Outline: Fail to add a comment with invalid input
    When user with username "<username>" attempts to add a new comment with rating "<rating>" and content "<commentContent>" to an existing recipe "<recipeTitle>"
    Then an error should be returned with message "<errorMessage>"

    Examples:
       | commentContent |  rating | username | recipeTitle | errorMessage |
       |                |  4.0    | Coco      | Steak       | Comment cannot be empty. |
       | Nice dish!      |  -1.0   | Jim      | Steak        | Rating must be between 0 and 5. |

  Scenario Outline: Successfully update an existing comment
    Given a user with username "<username>" has a comment on recipe "<recipeTitle>"
    When the user "<username>" updates the comment of "<recipeTitle>" with new content "<newCommentContent>" and rating "<newRating>"
    Then the comment should be updated successfully
    And the recipe's average rating should be updated

    Examples:
      | username | newCommentContent | newRating | recipeTitle
      | Coco        | Even better!      | 5.0       | Steak

  Scenario Outline: Fail to update a non-existing comment
    Given no comment from user "<username>" exists
    When the user tries to update their comment with new content "<newCommentContent>" and rating "<newRating>"
    Then an error should be returned with message "<errorMessage>"

    Examples:
       | newCommentContent | newRating | errorMessage|
       | Updated comment   | 4.0       | Comment does not exist.|

  Scenario Outline: Successfully delete a comment
    Given a comment form user "<username>" in recipe "<recipeTitle>" exists
    When the user deletes their comment  
    Then the comment should be removed from the system

    Examples:
      | username | recipeTitle |
      | Coco       | Steak |

  Scenario Outline: Fail to delete a non-existing comment
    Given no comment exists from user "<username>" in recipe "<recipeTitle>"
    When the user tries to delete their non existent comment
    Then an error should be returned with message "Comment does not exist."

    Examples:
      | username | recipeTitle |
      | Tim    | Steak |
