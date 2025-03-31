Feature: US010 View a recipe  
  As a NomNom user, I would like to view a recipe so that I can use its content

  Background:
    Given the following recipes exist in the system
      | title           | description              | category  | instructions                                      | ingredients                         |
      | Pasta Carbonara | Classic Italian dish     | Dinner    | Cook pasta, add eggs and cheese                   | Pasta, Eggs, Cheese                 |
      | Avocado Toast   | Healthy breakfast option | Breakfast | Toast bread, mash avocado, and spread on toast    | Bread, Avocado, Lemon               |
      | Chicken Curry   | Spicy and flavorful      | Lunch     | Cook chicken in coconut milk with curry powder    | Chicken, Coconut milk, Curry Powder |



  # normal flow
  Scenario Outline: Successfully retrieving all the recipes
    When I request to view all recipes
    Then I should get a list of "<totalRecipes>" recipes
    And the list should contain "<recipe1>", "<recipe2>", and "<recipe3>"

    Examples:
      | totalRecipes | recipe1          | recipe2       | recipe3       |
      | 3            | Pasta Carbonara  | Avocado Toast | Chicken Curry |


  # alternate flow
  Scenario Outline: Viewing a recipe containing specific ingredients
    When I request to view a recipe that contains "<ingredient>"
    Then I should receive the recipe "<title>"
    And the recipe description should be "<description>"
    And the category should be "<category>"

    Examples:
      | ingredient | title           | description              | category  |
      | Pasta      | Pasta Carbonara | Classic Italian dish     | Dinner    |
      | Avocado    | Avocado Toast   | Healthy breakfast option | Breakfast |
      | Chicken    | Chicken Curry   | Spicy and flavorful      | Lunch     |

    #alternate flow
  Scenario Outline: Viewing instructions of a specific recipe
    When I request to view a recipe with the title "<title>"
    Then I should receive the recipe "<title>"
    And the recipe instructions should be "<instructions>"

    Examples:
      | title           | instructions                                   |
      | Pasta Carbonara | Cook pasta, add eggs and cheese                |
      | Avocado Toast   | Toast bread, mash avocado, and spread on toast |
      | Chicken Curry   | Cook chicken in coconut milk with curry powder |


  # error flow
  Scenario Outline: Failing to retrieve recipes with invalid ingredients
    When I attempt to view a recipe that contains "<ingredient>"
    Then I should see an error message "<message>"

    Examples:
      | ingredient         | message                                                       |
      |                    | Ingredient names cannot be empty                              |
      | Banana             | No recipes found containing the specified ingredients: Banana |
      | Salmon             | No recipes found containing the specified ingredients: Salmon |