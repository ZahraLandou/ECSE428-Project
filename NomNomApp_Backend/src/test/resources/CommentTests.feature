Feature: Add/Update Comment
  As a User, I want to create, update and delete my Comment on a Recipe.

  Background:
    Given the following Users exist in the system
      | userId | username | emailAddress     | password |
      | 1      | Coco     | coco@gmail.com   | pass1    |
      | 2      | Tim      | tim@gmail.com    | pass2    |
      | 3      | Jim     | jim@gmail.com   | pass3    |

    Given the following Recipe exists in the system (f10)
      | recipeId | title | description                          | instructions  | creationDate | category | likes | picture  | averageRating | recipeNomNomUser |
      | 1        | Steak | The best steak you will ever make  | Cook the steak | 2025-02-23   | Lunch    | 0     | picture  | 0             | 1              |

  Scenario Outline: Successfully add a comment to a recipe
    When user with userId "<userId>" attempts to add a new comment with "<commentId>", date "<creationDate>" and content "<commentContent>" to an existing recipe "<recipeId>"
    Then the rating of the recipe "<averageRating>" should be "<expectedRating>"
    Then the number of comments for recipe "<recipeId>" in the system shall be "<numberOfComments>"
    Then the comment "<commentId>" created by user "<nomNomUser>" for recipe "<recipeId>", date "<creationDate>", content "<commentContent>", and rating "<rating>" shall exist in the system (f10)

    Examples:
      | commentId | commentContent          | creationDate | rating | nomNomUser | recipeId | expectedRating|
      | 1        | This is a great recipe  | 2025-02-23  | 4.0    | 1      | 1        | 4.0                 |
      | 2        | Love it!  | 2025-02-23  | 3.0    | Jim      | 3        |3.5                              |

  Scenario Outline: Fail to add a comment with invalid input
    When user with username "<nomNomUser>" attempts to add a comment with "<commentId>", date "<creationDate>", content "<commentContent>", and rating "<rating>" to an existing recipe "<recipeId>"
    Then an error should be returned with message "<errorMessage>"

    Examples:
      | commentId | commentContent | creationDate | rating | nomNomUser | recipeId | errorMessage |
      | 3        |                | 2025-02-23  | 4.0    | Coco      | 1        | Comment cannot be empty. |
      | 4        | Nice dish!      | 2025-02-23  | -1.0   | Coco      | 1        | Rating must be between 0 and 5. |

  Scenario Outline: Successfully update an existing comment
    Given a comment with ID "<commentId>" exists
    When the user updates the comment with new content "<newCommentContent>" and rating "<newRating>"
    Then the comment should be updated successfully
    And the recipe's average rating should be updated

    Examples:
      | commentId | newCommentContent | newRating |
      | 1        | Even better!      | 5.0       |

  Scenario Outline: Fail to update a non-existing comment
    Given no comment with ID "<commentId>" exists
    When the user tries to update the comment with new content "<newCommentContent>" and rating "<newRating>"
    Then an error should be returned with message "<errorMessage>"

    Examples:
      | commentId | newCommentContent | newRating | errorMessage|
      | 999      | Updated comment   | 4.0       | Comment does not exist.|

  Scenario Outline: Successfully delete a comment
    Given a comment with ID "<commentId>" exists
    When the user deletes the comment
    Then the comment should be removed from the system

    Examples:
      | commentId |
      | 1        |

  Scenario Outline: Fail to delete a non-existing comment
    Given no comment with ID "<commentId>" exists
    When the user tries to delete the comment
    Then an error should be returned with message "Comment does not exist."

    Examples:
      | commentId |
      | 999      |
