Feature: Delete Recipe Ingredient
  In order to update my recipe's ingredients
  As a NomNom user
  I want to be able to delete an ingredient from a recipe

  # Scenario for successful deletion
  Scenario: Successfully delete a recipe ingredient
    Given a recipe "Spicy Shrimp Pasta" exists
    And the following ingredients exist:
      | name   | type    |
      | garlic | spice   |
    And the recipe "Spicy Shrimp Pasta" already includes "garlic"
    When I remove "garlic" from the recipe "Spicy Shrimp Pasta"
    Then the recipe should not include "garlic"

  # Scenario for deletion of a non-existent ingredient
  Scenario: Attempt to delete an ingredient not present in the recipe
    Given a recipe "Spicy Shrimp Pasta" exists
    And the following ingredients exist:
      | name   | type    |
      | basil  | herb    |
    And the recipe "Spicy Shrimp Pasta" does not include "basil"
    When I try to remove "basil" from the recipe "Spicy Shrimp Pasta"
    Then I should see an error message "Ingredient 'basil' is not present in recipe 'Spicy Shrimp Pasta'"
