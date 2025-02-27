Feature: US006 Modify recipe ingredient
  As a NomNom user,
  I want to modify the ingredient details of a recipe,
  So that I can keep the recipe correct and up to date

  Background:
    # Your code has a step definition: @Given("a recipe {string} exists")
    Given a recipe "Spicy Shrimp Pasta" exists

  # Normal flow: "Updating" an ingredient's quantity by removing and re-adding it
  Scenario Outline: Successfully change an ingredient's quantity
    Given the recipe "<recipeName>" already includes "<ingredient>"
    When I remove "<ingredient>" from the recipe "<recipeName>"
    And I add "<ingredient>" to the recipe "<recipeName>" with quantity "<newQuantity>" and unit "<unit>"
    Then the recipe should include "<ingredient>" with quantity "<newQuantity>" and unit "<unit>"

    Examples:
      | recipeName          | ingredient | newQuantity | unit   |
      | Spicy Shrimp Pasta  | sugar      | 150        | grams  |
      | Spicy Shrimp Pasta  | flour      | 250        | grams  |

  # Error flow: trying to "update" (remove) an ingredient not in the recipe
  Scenario Outline: Attempt to change an ingredient that does not exist in the recipe
    Given the recipe "<recipeName>" does not include "<ingredient>"
    When I try to remove "<ingredient>" from the recipe "<recipeName>"
    Then I should see an error message "Ingredient '<ingredient>' not found in recipe '<recipeName>'."

    Examples:
      | recipeName          | ingredient |
      | Spicy Shrimp Pasta  | basil      |
      | Spicy Shrimp Pasta  | paprika    |
