Feature: US025 Update an ingredient
  As a NomNom user,
  I want to modify an ingredient,
  So that it can have the right information.

  Background:
    Given the following ingredients exist:
      | name   | type    |
      | shrimp | seafood |
      | garlic | spice   |
  
  # normal flow
  Scenario Outline: Successfully update an ingredient's name or type
    Given the ingredient "<name>" already exists
    When I update the ingredient with name "<name>" to have name "<new_name>" and type "<new_type>"
    Then the ingredient should be updated successfully
    And the updated ingredient should have name "<expected_name>" and type "<expected_type>"

    Examples:
      | name  | new_name | new_type  | expected_name | expected_type |
      | shrimp| prawn    |           | prawn         | seafood       |
      | garlic|          | vegetable | garlic        | vegetable     |


  # error flow
  Scenario Outline: Attempt to update an ingredient with an invalid name or type
    Given the ingredient "<name>" already exists
    When I try to update the ingredient with name "<name>" to have name "<new_name>" and type "<new_type>"
    Then I should see an error message "<error_message>"

    Examples:
      |name      | new_name | new_type | error_message                                |
      | shrimp   | garlic   |          | Ingredient with name 'garlic' already exists |
      |  garlic  |          |          | All ingredient fields are empty              |

  # error flow
  Scenario Outline: Attempt to update a non-existing ingredient
    Given the ingredient "<name>" does not exist
    When I try to update the ingredient with name "<name>" to have name "<new_name>" and type "<new_type>"
    Then I should see an error message "<error_message>"

    Examples:
     | name | new_name | new_type | error_message            |
     | kiwi | thyme    | herb     | Ingredient does not exist|
     | beets| basil    | herb     | Ingredient does not exist|
