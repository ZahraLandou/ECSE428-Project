Feature: Delete a user account
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
    When I send a DELETE request to "/users/<userId>"
    Then the response status should be 200
    And the user with ID <userId> should no longer exist in the system

    Examples:
      | userId |
      | 1      |
      | 2      |

  # Error Flow
  Scenario Outline: Attempt to delete a non-existing user
    Given no user with ID <userId> exists
    When I send a DELETE request to "/users/<userId>"
    Then the response status should be 404
    And I should see an error message "User with ID '<userId>' not found."

    Examples:
      | userId |
      | 999    |
      | 500    |

  # Edge Case: Invalid ID (negative or zero)
  Scenario Outline: Attempt to delete a user with an invalid ID
    When I send a DELETE request to "/users/<userId>"
    Then the response status should be 400
    And I should see an error message "Invalid user ID: <userId>"

    Examples:
      | userId |
      | -1     |
      | 0      |
