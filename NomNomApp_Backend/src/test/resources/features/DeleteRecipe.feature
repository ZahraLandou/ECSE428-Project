Feature: Remove recipe
  As a NomNom user, I want to remove a recipe I created,
  so I can keep my collection of recipes clean and relevant.

  # normal flow
  Scenario Outline: Successfully deleting a recipe
    Given a recipe with title "<recipeName>" exists
    When the user wants to delete a recipe with title "<title>"
    Then the recipe with title "<title>" should not exist in the system
    Examples:
      | title  |
      | Toast  |

  # error flow
  Scenario Outline: Deleting a non-existing recipe
    When the user wants to delete a recipe with title "Toast"
    Then I should see an error message "<message>" (not common)
    Examples:
      | title  | message                  |
      | Chips  | Error! Recipe not found. |
