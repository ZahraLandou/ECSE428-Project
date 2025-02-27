Feature: Delete a short form video
  As a NomNom user,
  I want to delete a short form video from my recipe,
  So that I can remove outdated or incorrect videos.

  Background:
    Given the following recipes exist:
      | recipeId | title           | description         |
      |        1 | Pasta Carbonara | Traditional Italian |
      |        2 | Chicken Curry   | Spicy chicken dish  |
    And the following short form videos exist:
      | videoId | videoTitle            | videoDescription                   | video         | recipeId |
      |       1 | How to make Carbonara | Step by step pasta carbonara guide | carbonara.mp4 |        1 |
      |       2 | Curry in 10 minutes   | Quick and easy curry recipe        | curry.mp4     |        2 |
  # normal flow

  Scenario Outline: Successfully delete a short form video
    Given a recipe with id "<recipeId>" exists
    And the recipe already has a short form video
    When I delete the short form video from the recipe
    Then the short form video should be deleted successfully
    And the recipe should not have a short form video

    Examples:
      | recipeId |
      |        1 |
      |        2 |
  # alternative flow

  Scenario Outline: Successfully delete a short form video by video ID
    Given there is a short form video with id "<videoId>" in the system
    When I delete the short form video with id "<videoId>"
    Then the short form video with id "<videoId>" should no longer exist in the system
    And the associated recipe should no longer have a video

    Examples:
      | videoId |
      |       1 |
      |       2 |
  # error flow

  Scenario Outline: Attempt to delete a short form video from a recipe that doesn't have one
    Given a recipe with id "<recipeId>" exists
    And the recipe does not have a short form video
    When I try to delete the short form video from the recipe
    Then I should see an error message "<message>"

    Examples:
      | recipeId | message                                    |
      |        3 | No short form video exists for this recipe |
