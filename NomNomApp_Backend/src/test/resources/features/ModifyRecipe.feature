Feature: Modify recipe information
  As a NomNom user, I want to modify a recipe I created,
  so I can update or correct my recipe as needed.

  Background:
    Given I am logged in as user "<username>"
    And "<recipeName>" exists as an existing recipe of user "<username>"

  # Normal Flow: Modify ingredients and ingredient count
  Scenario Outline: Successfully add a new ingredient with quantity
    When I add "<ingredient>" to the ingredient list of "<recipeName>"
    And I set "<ingredient>"'s quantity to "<quantity>"
    And I submit modifications
    Then "<ingredient>" should appear in the ingredients list of "<recipeName>" and in the database
    And "<quantity>" should appear next to "<ingredient>" in the ingredients list of "<recipeName>" and in the database

    Examples:
      | username | recipeName | ingredient | quantity |
      | SimonDT  | Recipe 1   | flour      | 150g     |
      | SimonDT  | Recipe 2   | sugar      | 200g     |

  # Normal Flow: Modify cooking step
  Scenario Outline: Successfully add a new cooking step
    When I add a cooking step with information "<stepDescription>" to the end of "<recipeName>"
    And I submit modifications
    Then the new cooking step should appear in the cooking step list of "<recipeName>"
    And the number of the new cooking step should be "<previousStepNumber> + 1"
    And the step number and step information should appear in the database

    Examples:
      | username | recipeName | stepDescription               | previousStepNumber |
      | SimonDT  | Recipe 1   | Pour the water into the bowl  | 3                  |
      | SimonDT  | Recipe 2   | Add sugar and stir well       | 2                  |

  # Edge Case: Cancel modifications
  Scenario Outline: Cancel all ongoing modifications
    Given I have "<ingredient>" as a new ingredient modification
    And I have "<stepDescription>" as a new cooking step modification
    When I cancel modifications
    Then cooking step "<stepDescription>" should not appear in the recipe page of "<recipeName>"
    And ingredient "<ingredient>" should not appear in the ingredient list of "<recipeName>"

    Examples:
      | username | recipeName | ingredient | stepDescription              |
      | SimonDT  | Recipe 1   | flour      | Pour the water into the bowl |
      | SimonDT  | Recipe 2   | sugar      | Add sugar and stir well      |