Feature: Update an ingredient
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
    Given an ingredient with ID <id> exists
    When I update the ingredient with ID <id> to have name "<new_name>" and type "<new_type>"
    Then the ingredient should be updated successfully
    And the updated ingredient should have name "<expected_name>" and type "<expected_type>"

    Examples:
      | id | new_name | new_type  | expected_name | expected_type |
      | 1  | prawn    |           | prawn         | seafood       |
      | 2  |          | vegetable | garlic        | vegetable     |


  # error flow
  Scenario Outline: Attempt to update an ingredient with an invalid name or type
    Given an ingredient with ID <id> exists
    When I try to update the ingredient with ID <id> to have name "<new_name>" and type "<new_type>"
    Then I should see an error message "<error_message>"

    Examples:
      | id | new_name | new_type | error_message                                |
      | 1  | garlic   |          | Ingredient with name 'garlic' already exists |
      | 2  |          |          | All ingredient fields are empty|

  # error flow
  Scenario Outline: Attempt to update a non-existing ingredient
    Given no ingredient with ID <id> exists
    When I try to update the ingredient with ID <id> to have name "<new_name>" and type "<new_type>"
    Then I should see an error message "<error_message>"

    Examples:
      | id  | new_name | new_type | error_message            |
      | 999 | thyme    | herb     | Ingredient does not exist|
      | 500 | basil    | herb     | Ingredient does not exist|
