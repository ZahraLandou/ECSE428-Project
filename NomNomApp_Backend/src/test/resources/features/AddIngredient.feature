Feature: US024 Add a new ingredient
  As a NomNom user,
  I want to add a new ingredient,
  So that I can use it in my recipes.

  Background:
    Given the following ingredients exist:
      | name   | type    |
      | shrimp | seafood |
      | garlic | spice   |

  # normal flow
  Scenario Outline: Successfully add a new ingredient
    Given the ingredient "<name>" does not exist
    When I add an ingredient with name "<name>" and type "<type>"
    Then the ingredient should be created successfully
    And I should see "<name>" in the list of ingredients

    Examples:
      | name  | type   |
      | apple | fruit  |
      | basil | herb   |

  # error flow
  Scenario Outline: Attempt to add an ingredient with a duplicate name
    Given the ingredient "<name>" already exists
    When I try to add an ingredient with name "<name>" and type "<type>"
    Then I should see an error message "<message>"

    Examples:
      | name   | type   | message                                    |
      | shrimp | fruit  |Ingredient with name 'shrimp' already exists|

  # error flow
  Scenario Outline: Attempt to add an ingredient with an invalid field
    When I try to add an ingredient with name "<name>" and type "<type>"
    Then I should see an error message "<message>"

    Examples:
      | name    | type     |    message                     |
      |         | seafood  | Ingredient name cannot be empty|
      | lobster |          | Ingredient type cannot be empty|
