Feature: US030 Favorite a Recipe
  As a NomNom user,
  I want to add recipes to my favorites,
  So that I can easily find and access recipes I like.

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

  # normal flow
  Scenario Outline: Successfully favorite a recipe
    Given I am logged in as user "<username>"
    When I favorite the recipe with title "<recipeTitle>"
    Then the recipe "<recipeTitle>" should be in my favorites list
    
    Examples:
      | username | recipeTitle      |
      | Alex     | Chicken Curry    |
      | Jamie    | Pasta Carbonara  |

  # error flow
  Scenario Outline: Attempt to favorite a non-existent recipe
    Given I am logged in as user "<username>"
    When I attempt to favorite a recipe with title "<nonExistentRecipe>"
    Then I should receive an error message "Recipe not found"
    
    Examples:
      | username | nonExistentRecipe |
      | Alex     | Chocolate Cake    |
      | Jamie    | Garden Salad      | 