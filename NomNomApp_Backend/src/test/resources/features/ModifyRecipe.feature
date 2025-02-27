Feature: US006 Modify recipe information
  As a NomNom user, I want to modify a recipe I created,
  so I can update or correct my recipe as needed.

  # Normal Flow: Modify ingredients and ingredient count
  Scenario Outline: Successfully add a new ingredient with quantity
    Given a recipe with title "<recipeName>" exists
    When I add ingredient "<ingredient>" with quantity "<quantity>" to recipe "<recipeName>"
    Then the recipe with title "<recipeName>", ingredient "<ingredient>" with quantity "<quantity>" should exist in the system
    Examples:
      | ingredient | quantity | recipeName |
      | flour      | 150.0    | Recipe 1   |
      | sugar      | 200.0    | Recipe 2   |