Feature: Add new recipe
  As a NomNom User, I want to post a recipe to my profile,
  so that all users can view and interact with it.

  # normal flow
  Scenario Outline: Successfully posting a recipe with title, ingredients, and instruction
    When I create a new recipe with name "<title>"
    Then the recipe with title "<title>" should exist in the system
    Examples:
      | title  |
      | Toast  |

  # alternate flow
  Scenario Outline: Successfully posting a recipe with title, ingredients, and instruction
    When the user wants to post a recipe with "<title>", "<ingredient>", "<instruction>"
    Then the recipe with title "<title>", ingredient "<ingredient>", and instruction "<instruction>" should exist in the system
    Examples:
      | title  | ingredient | instruction          |
      | Toast  | Bread      | Put bread in toaster |