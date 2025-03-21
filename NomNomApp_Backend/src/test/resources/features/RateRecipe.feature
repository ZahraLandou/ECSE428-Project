Feature: US011 Rate Recipes
  As a NomNom user,
  I want to rate the recipes of other users from 0 to 5 stars
  so
  The average rating as well as the number of ratings need to be visible in the recipe selection menu and below the recipe name on its page.

   # normal flow
  Scenario: User successfully Rates a Recipe
    Given I am logged in and on a recipe's page
    When I select a rating between 0 and 5 stars and I submit my rating
    Then the rating should be saved
    And the average rating and number of ratings for the recipe should be updated and displayed accordingly on the page

    # alternate flow
  Scenario: User Updates an Existing Rating
    Given I am logged in and have already rated a recipe
    When I change my rating to a new value and submit my new rating
    Then my previous rating should be replaced with the new one
    And the average rating and number of ratings for the recipe should be updated and displayed accordingly on the page

   # error flow
  Scenario: User attempts to Rate a Recipe without being Logged In
    Given I am not logged in and on a recipe's page
    When I select a rating and try to submit my rating for the recipe
    Then the rating should not be saved
    And the error message "You must log in to rate recipes" should be displayed
