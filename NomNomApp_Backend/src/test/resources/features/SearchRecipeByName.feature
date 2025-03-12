Feature: US013 Search for recipe by name
  As a NomNom user,
  I want to filter recipes by name,
  so that I can view all the recipes posted with a specific name.

  Background:
    Given the following recipes exist in the system:
      | recipeName   | description          | category    |
      | Spaghetti    | Classic pasta dish  | Italian     |
      | Pancakes     | Fluffy breakfast    | Breakfast   |
      | Caesar Salad | Fresh salad with dressing | Salad |
      | Sushi        | Japanese rice rolls | Japanese    |

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
    When When I enter a valid recipe with title "<recipeName>" into the search bar that does not match any existing recipes
    Then I should see an error message "<error_message>"
    Examples:
      | recipeName     | error_message            |
      | Chocolate Cake | Recipe does not exist    |
      | Tacos          | Recipe does not exist    |

  # error flow
  Scenario Outline: User attempts to filter recipes by an invalid name
    When I enter an invalid recipe with title "<recipeName>" into the search page
    Then I should see an error message "<error_message>"
    Examples:
      | recipeName   | error_message            |
      |  123456789   | Invalid recipe name      |
      |  #5hsja^Sk   | Invalid recipe name      |
