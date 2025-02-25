Feature: Search for an ingredient
  As a NomNom user,
  I want to search for a specific ingredient,
  So that I can use it in my recipe.

  Background:
    Given the following ingredients exist:
      | name   | type    |
      | shrimp | seafood |
      | garlic | spice   |

  # normal flow
  Scenario Outline: Successfully find an existing ingredient using its name
    When I search for "<name>"
    Then I should see the ingredient details with name "<name>" and type "<type>"
    Examples:
      | name   | type    |
      | shrimp | seafood |
      | garlic | spice   |

  # alternate flow
  Scenario Outline: Retrieve all ingredients
    Then I should see an ingredient with name "<name>" and type "<type>"
    Examples:
      | name   | type    |
      | shrimp | seafood |
      | garlic | spice   |
      
  # error flow
  Scenario Outline: Attempt to search for a non-existing ingredient
    Given the ingredient "<name>" does not exist
    When I search for "<name>"
    Then I should see an error message "<message>"
    Examples:
      | name    |   message                            |
      |  onion  |Ingredient with name 'onion' not found|

