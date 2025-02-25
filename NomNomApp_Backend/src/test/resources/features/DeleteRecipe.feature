Feature: Remove recipe
  As a NomNom user, I want to remove a recipe I created,
  so I can keep my collection of recipes clean and relevant.

  Background:
    Given the NomNom application service is running
    And the following recipes exist in the system
      | title    | ingredient     | instruction          |
      | Toast    | Bread          | Put bread in toaster |
      | Lemonade | Water, Lemons  | Make lemonade       |

  # normal flow
  Scenario Outline: Successfully deleting a recipe
    When the user wants to delete a recipe with title "<title>"
    Then the NomNom application should return the message "<message>"
    And the recipe with title "<title>" should not exist in the system
    Examples:
      | title  | message         |
      | Toast  | Recipe deleted! |

    # alternate flow
  Scenario Outline: Attempting to delete another user's recipe
    When the user tries to delete a recipe with title "<title>" created by another user
    Then the NomNom application should return the message "<message>"
    And the recipe with title "<title>" should still exist in the system
    Examples:
      | title    | message                                      |
      | Lemonade | Error! You can only delete your own recipes. |

  # error flow
  Scenario Outline: Deleting a non-existing recipe
    When the user wants to delete a recipe with title "<title>"
    Then the NomNom application should return the message "<message>"
    And no recipe should be deleted
    Examples:
      | title  | message                  |
      | Chips  | Error! Recipe not found. |
