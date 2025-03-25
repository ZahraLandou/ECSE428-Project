Feature: US010 View a recipe  
  As a NomNom user, I would like to view a recipe so that I can use its content

  Background:
    Given the following recipes exist in the system
      | title            | description              | category    | ingredients                         |
      | Pasta Carbonara  | Classic Italian dish     | Dinner      | Pasta, Eggs, Cheese                 |
      | Avocado Toast    | Healthy breakfast option | Breakfast   | Bread, Avocado, Lemon               |
      | Chicken Curry    | Spicy and flavorful      | Lunch       | Chicken, Coconut milk, Curry Powder |


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


  # error flow
  Scenario Outline: Failing to retrieve a non-existent recipe
    When I request to view a recipe with the title "<invalidTitle>"
    Then I should see an error message "<message>"

    Examples:
      | invalidTitle       | message                                     |
      | Chocolate Cake     | No recipes found with title: Chocolate Cake |
      | Banana Smoothie    | No recipes found with title: Banana Smoothie|
      | Grilled Salmon     | No recipes found with title: Grilled Salmon |


  # error flow
  Scenario Outline: Failing to retrieve recipes with invalid ingredients
    When I attempt to view a recipe that contains "<ingredient>"
    Then I should see an error message "<message>"

    Examples:
      | ingredient         | message                          |
      |                    | Ingredient names cannot be empty |
      | Banana             | No recipes found containing the specified ingredients: Banana|
      | Salmon             | No recipes found containing the specified ingredients: Salmon |