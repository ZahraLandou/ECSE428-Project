Feature: Recipe List Management
  As a user of NomNomApp
  I want to manage my recipe lists
  So that I can organize my favorite recipes

  Scenario: Create a new recipe list
    Given a user exists
    When the user creates a recipe list with name "My Favorites" and category "X"
    Then the recipe list should be created successfully

  Scenario: Add a recipe to a recipe list
    Given a user has a recipe list named "My Favorites"
    And a recipe "Pasta" exists
    When the user adds the recipe "Pasta" to "My Favorites"
    Then the recipe list "My Favorites" should contain "Pasta"

  Scenario: Remove a recipe from a recipe list
    Given a user has a recipe list named "My Favorites" containing "Pasta"
    When the user removes "Pasta" from recipe list "My Favorites"
    Then the recipe list "My Favorites" should not contain "Pasta"
