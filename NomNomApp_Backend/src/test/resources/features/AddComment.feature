
   Feature: US007 Add Comment
  As a NomNom user, I want to comment on a recipe, so that I can give feedback to the poster

  Background:
    Given the following users exist in the system
       | username | emailAddress     | password |
       | Coco     | coco@gmail.com   | pass1    |
       | Tim      | tim@gmail.com    | pass2    |
       | Jim      | jim@gmail.com    | pass3    |

    Given the following recipe exists in the system 
      | title | description                          | instructions  | category | likes | averageRating | recipeNomNomUser |
      | Steak | The best steak you will ever make  | Cook the steak | Lunch    | 0     | 0.0             | Coco             |

  Scenario Outline: Successfully add a comment to a recipe
    When user with username "<username>" adds a new comment with rating "<rating>" and content "<commentContent>" to an existing recipe "<recipeTitle>"
    Then the rating of the recipe "<recipeTitle>" should be "<expectedRating>"
    Then the comment created by user "<username>" for recipe "<recipeTitle>", content "<commentContent>", and rating "<rating>" shall exist in the system

    Examples:
       | commentContent             | rating | username | recipeTitle | expectedRating|
       | This is a great recipe     | 4.0    | Coco     | Steak       | 4.0           |
       | Love it!                   | 3.0    | Jim      | Steak       |3.             |

  Scenario Outline: Fail to add a comment with invalid input
    When user with username "<username>" attempts to add a new comment with rating "<rating>" and content "<commentContent>" to an existing recipe "<recipeTitle>"
    Then an error should be returned with message "<errorMessage>"

    Examples:
       | commentContent |  rating | username | recipeTitle | errorMessage |
       |                |  4.0    | Coco      | Steak       | Comment cannot be empty. |
       | Nice dish!      |  -1.0   | Jim      | Steak        | Rating must be between 0 and 5. |