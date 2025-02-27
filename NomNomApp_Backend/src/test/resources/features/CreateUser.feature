Feature: US001 Create a user account
  As a NomNom user,
  I want to create an account,
  So that I can use the app.

  # Normal Flow
  Scenario Outline: Successfully create a new user account
    Given no user exists with username "<username>" or email "<email>"
    When I attempt to register with username "<username>", email "<email>", and password "<password>"
    Then a new user account should be created with username "<username>" and email "<email>"

    Examples:
      | username      | email              | password    |
      | testUser      | test@example.com   | Password123 |
      | anotherUser   | another@example.com| SecurePass  |

  # Error Flow: Trying to register with an already used email
  Scenario Outline: Fail to create a user with an existing email
    Given a user exists with username "<existingUsername>" and email "<existingEmail>"
    When I attempt to register with username "<newUsername>", email "<existingEmail>", and password "<password>"
    Then I should see an error message "User with email '<existingEmail>' already exists"

    Examples:
      | existingUsername | existingEmail       | newUsername   | password    |
      | johnDoe          | john@example.com    | differentJohn | Pass123     |
      | janeSmith        | jane@example.com    | differentJane | securePass  |

  # Error Flow: Trying to register with an already used username
  Scenario Outline: Fail to create a user with an existing username
    Given a user exists with username "<existingUsername>" and email "<existingEmail>"
    When I attempt to register with username "<existingUsername>", email "<newEmail>", and password "<password>"
    Then I should see an error message "User with username '<existingUsername>' already exists"

    Examples:
      | existingUsername | existingEmail    | newEmail               | password    |
      | johnDoe          | john@example.com | differentJohn@test.com | Pass123     |
      | janeSmith        | jane@example.com | differentJane@test.com | securePass  |

  # Error Flow: Missing required fields during registration
  Scenario Outline: Fail to create a user with missing required fields
    When I attempt to register with username "<username>", email "<email>", and password "<password>"
    Then I should see an error message "<errorMessage>"

    Examples:
      | username | email              | password | errorMessage                   |
      |          | missing@example.com| Pass123  | Username cannot be empty       |
      | missing  |                    | Pass123  | Email address cannot be empty  |
      | missing  | missing@example.com|          | Password cannot be empty       |
      |          |                    |          | Username cannot be empty       |

  # Error Flow: Invalid information during registration
  Scenario Outline: Fail to create a user with invalid information
    When I attempt to register with username "<username>", email "<email>", and password "<password>"
    Then I should see an error message "<errorMessage>"

    Examples:
      | username     | email              | password    | errorMessage                    |
      | invalid@#$   | invalid@example.com| Password123 | Invalid username format.        |
      | valid        | invalid.com        | Password123 | Invalid email format.           |
