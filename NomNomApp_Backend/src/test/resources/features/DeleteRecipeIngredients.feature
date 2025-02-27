Feature: US006 Delete a recipe ingredient
  As a NomNom user,
  I want to remove an ingredient from a recipe,
  So that my recipe remains accurate and up to date

  Background:
    Given a recipe "Spicy Shrimp Pasta" exists
    And the following ingredients exist:
      | name   | type    |
      | garlic | spice   |
      | basil  | herb    |

  # Normal flow: successfully remove an ingredient that the recipe already includes
  Scenario Outline: Successfully delete a recipe ingredient
    Given the recipe "Spicy Shrimp Pasta" already includes "<ingredientName>"
    When I remove "<ingredientName>" from the recipe "Spicy Shrimp Pasta"
    Then the recipe should not include "<ingredientName>"

    Examples:
      | ingredientName |
      | garlic         |
      | basil          |

  # Error flow: attempting to remove an ingredient that is not in the recipe
  Scenario Outline: Attempt to remove a non-existent ingredient
    Given the recipe "Spicy Shrimp Pasta" does not include "<ingredientName>"
    When I try to remove "<ingredientName>" from the recipe "Spicy Shrimp Pasta"
    Then I should see an error message "Ingredient '<ingredientName>' not found in recipe 'Spicy Shrimp Pasta'."

    Examples:
      | ingredientName |
      | basil          |
      | onion          |
      | parsley        |
