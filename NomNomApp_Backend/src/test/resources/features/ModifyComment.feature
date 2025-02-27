
Feature: Update Comment
  As a NomNom user, I want to update a comment on a recipe, so that I can give feedback to the poster

  Background:
    Given the following users exist in the system
      | username | emailAddress     | password |
      | Coco     | coco@gmail.com   | pass1    |
      | Tim      | tim@gmail.com    | pass2    |
      | Jim      | jim@gmail.com    | pass3    |

    Given the following recipe exists in the system
      | title | description                          | instructions  | category | likes | averageRating | recipeNomNomUser |
      | Steak | The best steak you will ever make    | Cook the steak| Lunch   | 0      | 0.0           | Coco             |



Scenario Outline: Successfully update an existing comment
Given a user with username "<username>" has a comment on recipe "<recipeTitle>"
When the user "<username>" updates the comment of "<recipeTitle>" with new content "<newCommentContent>" and rating "<newRating>"
Then the comment should be updated successfully
And the recipe's average rating should be updated to "<newRating>"

Examples:
| username | newCommentContent | newRating | recipeTitle|
| Coco        | Even better!      | 5.0       | Steak |

Scenario Outline: Fail to update a comment with invalid content
Given a user with username "<username>" has a comment on recipe "<recipeTitle>"
When the user tries to update their comment with new content "<newCommentContent>" and rating "<newRating>"
Then an error should be returned with message "<errorMessage>"

Examples:
|username| newCommentContent | newRating |     errorMessage       |recipeTitle|
|Tim     | | 4.0       | Comment cannot be empty.|Steak|
