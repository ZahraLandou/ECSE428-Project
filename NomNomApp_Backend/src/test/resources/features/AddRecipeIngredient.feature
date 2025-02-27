Feature: US006 Add a recipe ingredient
  As a NomNom user,
  I want to add an ingredient to a recipe,
  So that my recipe is complete

  Background:
    Given a recipe "Spicy Shrimp Pasta" exists
    And the following ingredients exist:
      | name   | type    |
      | shrimp | seafood |
      | garlic | spice   |
      | basil  | herb    |

  # Normal flow: successfully add a new ingredient to a recipe
  Scenario Outline: Successfully add a recipe ingredient
    Given the recipe "Spicy Shrimp Pasta" does not already include "<ingredientName>"
    When I add "<ingredientName>" to the recipe "Spicy Shrimp Pasta" with quantity "<quantity>" and unit "<unit>"
    Then the recipe should include "<ingredientName>" with quantity "<quantity>" and unit "<unit>"

    Examples:
      | ingredientName | quantity | unit   |
      | basil          | 10       | grams  |
      | garlic         | 2        | cloves |

  # Error flow: attempting to add an ingredient that is already part of the recipe
  #Scenario Outline: Attempt to add a duplicate recipe ingredient
  #  Given the recipe "Spicy Shrimp Pasta" already includes "<ingredientName>"
  #  When I try to add "<ingredientName>" to the recipe "Spicy Shrimp Pasta" with quantity "<quantity>" and unit "<unit>"
   # Then I should see an error message "<errorMessage>"

   # Examples:
  #    | ingredientName | quantity | unit   | errorMessage                                |
  #    | shrimp         | 200      | grams  | Ingredient with name 'shrimp' already exists|

  # Error flow: attempting to add a recipe ingredient with invalid fields
  Scenario Outline: Attempt to add a recipe ingredient with an invalid field
    When I try to add a recipe ingredient with name "<ingredientName>" to the recipe "Spicy Shrimp Pasta" with quantity "<quantity>" and unit "<unit>"
    Then I should see an error message "<errorMessage>"

    Examples:
      | ingredientName | quantity | unit   | errorMessage                                |
      | garlic         |          | cloves | Recipe ingredient quantity cannot be empty  |
      | garlic         | 2        |        | Recipe ingredient unit cannot be empty      |
