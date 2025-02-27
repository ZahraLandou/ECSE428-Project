Feature: Add a recipe ingredient
  As a NomNom user,
  I want to create a new comment to be associated with a recipe

  Background:
    Given the following users exist:
      | username   | email               | password       |
      | johnDoe    | john@example.com    | pswd           |
      | janeSmith  | jane@example.com    | pswd           |
    And a recipe with recipeID "1" exists

  # Normal flow: successfully add a new comment to a recipe
  Scenario Outline: Successfully add a comment
    When user with username "johnDoe" attempts to add a new comment with content "Love this!" and rating "5" to an existing recipe with recipeID "1"
    Then the comment with content "Love this!" and rating "5" created by user "johnDoe" for recipe with recipeId "1" should exist
    And the rating of the recipe with recipeID "1" should be "5"
    And the number of comments for recipe with recipeID "1" in should be "1"

#    Examples:
#      | userId    | commentContent | creationDate   | rating   |
#      | 1         | Love this.     | 946080000000   | 10       |
#      | 2         | Hate this.     | 947080024000   | 1        |

#  # Error flow: attempting to add a comment with negative rating
#  Scenario Outline: Attempt to add a comment with negatie rating
#  When user with userId {string} attempts to add a new comment with a rating "-1"
#   Then I should see an error message "<errorMessage>"

#    Examples:
#     | ingredientName | quantity | unit   | errorMessage                                |
#     | garlic         |          | cloves | Recipe ingredient quantity cannot be empty  |
#     | garlic         | 2        |        | Recipe ingredient unit cannot be empty      |
