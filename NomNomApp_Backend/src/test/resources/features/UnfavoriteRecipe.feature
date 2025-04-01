Feature: US031 Unfavorite a Recipe
  As a NomNom user,
  I want to remove recipes from my favorites,
  So that I can keep my favorites list organized with only recipes I currently like.

  Background:
    Given the following users exist in the system
      | username | emailAddress     | password |
      | Alex     | alex@gmail.com   | pass123  |
      | Jamie    | jamie@gmail.com  | pass456  |
    
    And the following recipes exist in the system
      | title           | description              | category  | instructions                                      | ingredients                         | username |
      | Pasta Carbonara | Classic Italian dish     | Dinner    | Cook pasta, add eggs and cheese                   | Pasta, Eggs, Cheese                 | Alex     |
      | Avocado Toast   | Healthy breakfast option | Breakfast | Toast bread, mash avocado, and spread on toast    | Bread, Avocado, Lemon               | Alex     |
      | Chicken Curry   | Spicy and flavorful      | Lunch     | Cook chicken in coconut milk with curry powder    | Chicken, Coconut milk, Curry Powder | Jamie    |
    
    And the following recipes are in users' favorites
      | username | recipeTitle      |
      | Alex     | Chicken Curry    |
      | Jamie    | Pasta Carbonara  |
      | Jamie    | Avocado Toast    |

  # normal flow
  Scenario Outline: Successfully unfavorite a recipe
    Given I am logged in as user "<username>"
    When I unfavorite the recipe with title "<recipeTitle>"
    Then the recipe "<recipeTitle>" should not be in my favorites list
    
    Examples:
      | username | recipeTitle      |
      | Alex     | Chicken Curry    |
      | Jamie    | Pasta Carbonara  |

  # error flow
  Scenario Outline: Attempt to unfavorite a recipe that is not in favorites
    Given I am logged in as user "<username>"
    When I attempt to unfavorite a recipe with title "<notFavoritedRecipe>"
    Then I should receive an error message "Recipe not found in favorites"
    
    Examples:
      | username | notFavoritedRecipe |
      | Alex     | Pasta Carbonara    |
      | Alex     | Avocado Toast      | 