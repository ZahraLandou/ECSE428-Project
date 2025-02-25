Feature: Add new recipe
  As a NomNom User, I want to post a recipe to my profile,
  so that all users can view and interact with it.

  Background:
    Given the NomNom application service is running

  # normal flow
  Scenario Outline: Successfully posting a recipe with title, ingredients, and instruction
    When the user wants to post a recipe with "<title>", "<ingredient>", "<instruction>"
    Then the NomNom application should return the message "<message>"
    And the recipe with title "<title>" should exist in the system
    Examples:
      | title  | ingredient | instruction          | message         |
      | Toast  | Bread      | Put bread in toaster | Recipe posted!  |

  # alternate flow
  Scenario Outline: Successfully posting a recipe with title, ingredients, instruction, and duration
    When the user wants to post a recipe with "<title>", "<ingredient>", "<instruction>", "<duration>"
    Then the NomNom application should return the message "<message>"
    And the recipe with title "<title>" should exist in the system
    Examples:
      | title  | ingredient | instruction          | duration  | message        |
      | Toast  | Bread      | Put bread in toaster | 5 minutes | Recipe posted! |

  # error flow
  Scenario Outline: Posting a recipe without a title
    When the user tries to post a recipe without a title, but with "<ingredient>" and "<instruction>"
    Then the NomNom application should return the message "<message>"
    And the recipe should not be posted
    Examples:
      | ingredient | instruction          | message               |
      | Bread      | Put bread in toaster | Error! Missing title. |

  # error flow
  Scenario Outline: Posting a recipe without ingredients
    When the user tries to post a recipe with title "<title>" but without ingredients
    Then the NomNom application should return the message "<message>"
    And the recipe should not be posted
    Examples:
      | title  | message                      |
      | Toast  | Error! Missing ingredients. |