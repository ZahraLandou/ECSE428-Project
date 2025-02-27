Feature: Upload a new short form video
  As a NomNom user,
  I want to upload a short form video for my recipe,
  So that users can watch how the recipe is prepared.

  Background:
    Given the following recipes exist:
      | recipeId | title           | description         |
      |        1 | Pasta Carbonara | Traditional Italian |
      |        2 | Chicken Curry   | Spicy chicken dish  |
 
  # normal flow
  Scenario Outline: Successfully upload a new short form video
    Given a recipe with id "<recipeId>" exists
    And the recipe does not have a short form video
    When I upload a short form video with title "<title>", description "<description>", and video file "<videoFile>" to the recipe
    Then the short form video should be uploaded successfully
    And the recipe should have a short form video with title "<title>" and description "<description>"

    Examples:
      | recipeId | title                 | description                        | videoFile     |
      |        1 | How to make Carbonara | Step by step pasta carbonara guide | carbonara.mp4 |
      |        2 | Curry in 10 minutes   | Quick and easy curry recipe        | curry.mp4     |
  
  # error flow
  Scenario Outline: Attempt to upload a short form video to a recipe that already has one
    Given a recipe with id "<recipeId>" exists
    And the recipe already has a short form video
    When I try to upload a short form video with title "<title>", description "<description>", and video file "<videoFile>" to the recipe
    Then I should see an error message "<message>"

    Examples:
      | recipeId | title             | description                       | videoFile    | message                                        |
      |        1 | Carbonara Updated | Updated guide for pasta carbonara | carb_new.mp4 | Recipe already has a short form video attached | 