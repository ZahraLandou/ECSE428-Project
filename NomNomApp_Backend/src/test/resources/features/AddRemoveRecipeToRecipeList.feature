Feature: Update RecipeList
As a NomNom User, I want to add and remove recipes in a recipe list
So that the recipes added to the recipe list can be accessed later form the list

Scenario: Successfully add a recipe to a list
	Given a recipe list "Lunch Ideas" exists
    And a recipe "Pasta Aglio, Olio e Peperoncino" exists
	And I am on the recipe page
	When I click the “Add to List” button
	And I select an existing recipe list
	Then the recipe is added to the selected list

Scenario: Attempt to add already-added recipe to list
	Given I have an existing recipe list A
	And I am on the recipe page
	And the recipe on the recipe page already exists in the list A
	When I click the “Add to List” button
	And I select the list A
	Then the recipe is not added to list A
	And an error message “recipe is already in this list” is shown

Scenario: Successfully remove a recipe to a list
	Given I have an existing recipe list
	And a recipe A is contained in a recipe list
	And I am on the recipe list page
	When I click the “remove recipe from list” button
	Then the recipe is removed from the list
	And the updated recipe list is shown



