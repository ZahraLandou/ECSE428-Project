Feature: US002 Delete a user account
  As a NomNom user,
  I want to delete my account,
  So that my profile and data are removed from the system.

  Background:
    Given the following userId exist:
      | userId | username   | email               |
      | 1      | johnDoe    | john@example.com    |
      | 2      | janeSmith  | jane@example.com    |

  # Normal Flow
  Scenario Outline: Successfully delete an existing user
    Given a user with ID <userId> exists
    When I delete the user with ID <userId>
    Then the user should be deleted successfully

    Examples:
      | userId |
      | 1      |
      | 2      |

  # Error Flow
  Scenario Outline: Attempt to delete a non-existing user
    Given no user with ID <userId> exists
    When I delete the user with ID <userId>
    Then I should see an error message "User with ID '<userId>' not found."

    Examples:
      | userId |
      | 999    |
      | 500    |

  # Error Case: Invalid ID (negative or zero)
  Scenario Outline: Attempt to delete a user with an invalid ID
    Given no user with ID <userId> exists
    When I delete the user with ID <userId>
    And I should see an error message "User with ID '<userId>' not found."


    Examples:
      | userId |
      | -1     |
      | 0      |