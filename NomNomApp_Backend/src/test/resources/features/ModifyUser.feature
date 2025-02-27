Feature: US003 Update profile
  As a NomNom user,
  I want to modify my profile information
  to keep it up to date

# Normal Flow
Scenario Outline: Successfully modify a user's username
Given User with username "<currentUsername>" exists
When I modify the username of user "<currentUsername>" to "<newUsername>"
Then User with username "<newUsername>" should exist in the system
And User with username "<currentUsername>" shoud not exist in the system
Examples:
  | currentUsername | newUsername      |
  | Jim             | JimBobTheSecond  |
  | Jack            | JackXD321        |
  | Bob             | BobLovesCucumber |

# Normal Flow
Scenario Outline: Successfully modify a user's email address
Given User with username "<userName>" exists
And User with username "<userName>" has email address "<currentEmail>"
When I modify the email address of user "<userName>" to "<newEmail>"
Then User with username "<userName>" should not have email address "<currentEmail>"
And User with username "<userName>" should have email address "<newEmail>"
Examples:
  | userName | currentEmail    | newEmail                 |
  | Jim      | Jim@gmail.com   | BigJim@gmail.com         |
  | Jack     | Jack@icloud.com | Jack@gmail.com           |
  | Bob      | Bob@outlook.ca  | BobTheBuilder@outlook.ca |

# Error Flow
Scenario Outline: Attempt to modify their username to an existing username
Given User with username "<userName1>" exists
And User with username "<userName2>" exists
When I modify username of user "<userName2>" to username "<userName1>"
Then I should see an error message "Username already exists." (user modif)
Examples:
  | userName1 | userName2 | message                    |
  | Jim       | Bob       | Username already exists.   |
  | Jack      | Jill      | Username already exists.   |