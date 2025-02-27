Feature: Update a recipe ingredient
  As a NomNom user,
  I want to modify a recipe ingredient,
  So that it can have the right information.

  Background:
    # We assume the table below populates some test data. Adjust field names as needed.
    Given the following recipe ingredients exist:
      | name   | quantity | unit   |
      | onion  | 1.0      | piece  |
      | garlic | 2.0      | clove  |

  # ------------------------------------------------------------------------------
  # NORMAL FLOW
  # ------------------------------------------------------------------------------
  Scenario Outline: Successfully update a recipe ingredient's quantity or unit
    Given the recipe "<recipeTitle>" already includes "<ingredientName>"
    When I update the recipe ingredient with name "<ingredientName>" in recipe "<recipeTitle>" to have quantity "<newQuantity>" and unit "<newUnit>"
    Then the recipe ingredient should be updated successfully
    And the updated recipe ingredient should have name "<expectedName>", quantity "<expectedQuantity>", and unit "<expectedUnit>"

    Examples:
      | recipeTitle | ingredientName | newQuantity | newUnit | expectedName | expectedQuantity | expectedUnit |
      | Pasta       | onion          | 3.0         | piece   | onion        | 3.0             | piece        |
      | Pasta       | garlic         | 4.0         | clove   | garlic       | 4.0             | clove        |

  # ------------------------------------------------------------------------------
  # ERROR FLOW #1
  # ------------------------------------------------------------------------------
  Scenario Outline: Attempt to update an existing recipe ingredient with invalid data
    Given the recipe "<recipeTitle>" already includes "<ingredientName>"
    When I try to update the recipe ingredient with name "<ingredientName>" in recipe "<recipeTitle>" to have quantity "<newQuantity>" and unit "<newUnit>"
    Then I should see an error message "<errorMessage>"

    Examples:
      | recipeTitle | ingredientName | newQuantity | newUnit | errorMessage                                       |
      | Pasta       | onion          | -1.0        | piece   | Invalid quantity: cannot be negative              |
      | Pasta       | garlic         | 2.0         |         | Invalid unit: cannot be empty                     |
      | Pasta       | onion          | 3.0         | cLoVe   | Unit mismatch for ingredient 'onion' (example)     |

  # ------------------------------------------------------------------------------
  # ERROR FLOW #2
  # ------------------------------------------------------------------------------
  Scenario Outline: Attempt to update a non-existing recipe ingredient
    Given the recipe "<recipeTitle>" does not include "<ingredientName>"
    When I try to update the recipe ingredient with name "<ingredientName>" in recipe "<recipeTitle>" to have quantity "<newQuantity>" and unit "<newUnit>"
    Then I should see an error message "<errorMessage>"

    Examples:
      | recipeTitle | ingredientName | newQuantity | newUnit | errorMessage                                        |
      | Pasta       | pepper         | 1.0         | tsp     | Recipe ingredient with name 'pepper' not found      |
      | Pasta       | bacon          | 2.0         | slice   | Recipe ingredient with name 'bacon' not found       |
