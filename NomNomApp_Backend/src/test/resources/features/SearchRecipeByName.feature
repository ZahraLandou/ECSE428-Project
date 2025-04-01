Feature: US013 Search for recipe by name
  As a NomNom user,
  I want to filter recipes by name,
  so that I can view all the recipes posted with a specific name.

  Background:
    Given the following users exist in the system:
      | username | emailAddress     | password |
      | Coco     | coco@gmail.com   | pass1    |
      | Tim      | tim@gmail.com    | pass2    |
      | Jim      | jim@gmail.com    | pass3    |

    Given the following recipes exist in the system:
      | recipeName   | description               |   category  |  recipeNomNomUser |  instructions  | likes | averageRating  |
      | Spaghetti    | Classic pasta dish        | Dinner      |     Coco          |  Cook the steak | 0     | 0.0           |
      | Pancakes     | Fluffy breakfast          | Breakfast   |     Tim            | Cook the steak | 0     | 0.0           |
      | Caesar Salad | Fresh salad with dressing | Lunch       |     Jim            | Cook the steak | 0     | 2             |
      | Sushi        | Japanese rice rolls       | Dinner      |     Tim            | Cook the steak | 0     | 4             |

  # normal flow
  Scenario Outline: User filters recipes by a valid name
    When I enter a valid recipe with title "<recipeName>" into the search bar
    Then I should see a list of recipes that match the specified name
    Examples:
      | recipeName    |
      | Spaghetti     |
      | Sushi         |

  # alternate flow
  Scenario Outline: User filters recipes by a valid name with no matching recipes
    When I enter a valid recipe with title "<recipeName>" into the search bar that does not match any existing recipes
    Then I should see an error message "<error_message>"
    Examples:
      | recipeName     | error_message                                  |
      | Chocolate Cake | No recipes found with title: Chocolate Cake    |
      | Tacos          | No recipes found with title: Tacos             |

  # error flow
  Scenario Outline: User attempts to filter recipes by an invalid name
    When I enter an invalid recipe with title "<recipeName>" into the search page
    Then I should see an error message "<error_message>"
    Examples:
      | recipeName   | error_message            |
      |  123456789   | Invalid recipe name      |
      |  #5hsja^Sk   | Invalid recipe name      |
