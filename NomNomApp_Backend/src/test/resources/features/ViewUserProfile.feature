Feature: US019 View User Profile
  As a NomNom user,
  I want to view a user's profile,
  So that I can see their biography and posted recipes.
  Background:
    Given the following users exist:
      | username      | email                | biography                 |
      | foodlover     | food@example.com     | I love cooking!           |
      | newbie        | newbie@example.com   | Just joined NomNom        |
      | dessertchef   | dessert@example.com  | Dessert specialist        |
    And the following recipes exist:
      | title           | description                  | username      |
      | Chocolate Cake  | Delicious homemade cake      | foodlover     |
      | Pasta Carbonara | Classic Italian pasta dish   | foodlover     |
      | Tiramisu        | Italian coffee dessert       | dessertchef   |
      | Apple Pie       | Traditional American dessert | dessertchef   |
      | Chocolate Mousse| Easy chocolate dessert       | dessertchef   |
    And the following favorite recipes exist:
      | username      | title           |
      | foodlover     | Chocolate Cake  |
      | dessertchef   | Tiramisu        |
      | dessertchef   | Apple Pie       |

  #normal flow
    Scenario Outline: Successfully view a user's profile
      Given a user with username "<username>" exists
      When I request to view the profile of user "<username>"
      Then the profile should be displayed successfully
      And the profile should show the biography "<biography>"
      And the profile should display the correct number of recipes
      Examples:
        | username      | biography                 |
        | foodlover     | I love cooking!           |
        | dessertchef   | Dessert specialist        |
        | newbie        | Just joined NomNom        |

  #normal flow
  Scenario Outline: View recipes posted by a user
    Given a user with username "<username>" exists
    When I request to view the recipes of user "<username>"
    Then the recipes should be displayed successfully
    And I should see <count> recipes in the list
    And the list should include a recipe titled "<recipe_title>"
  Examples:
  | username      | count | recipe_title     |
  | foodlover     | 2     | Chocolate Cake   |
  | dessertchef   | 3     | Tiramisu         |
  | newbie        | 0     |                  |

  #normal flow
  Scenario Outline: View a user's favorite recipes
    Given a user with username "<username>" exists
    When I request to view the favorite recipes of user "<username>"
    Then the favorite recipes should be displayed successfully
    And I should see <count> favorite recipes
    And the favorites should include "<favorite_recipe>" if count is not zero
  Examples:
  | username      | count | favorite_recipe  |
  | foodlover     | 1     | Chocolate Cake   |
  | dessertchef   | 2     | Tiramisu         |
  | newbie        | 0     |                  |

  #error flow
  Scenario Outline: Attempt to view a non-existent user profile
    Given no user exists with username "<username>"
    When I attempt to view the profile of user "<username>"
    Then I should see an error message "<message>"
  Examples:
  | username        | message                                      |
  | nonexistentuser | User with username 'nonexistentuser' not found |
  | deleteduser     | User with username 'deleteduser' not found     |


  #error flow
  Scenario Outline: Attempt to view recipes of a non-existent user
    Given no user exists with username "<username>"
    When I attempt to view the recipes of user "<username>"
    Then I should see an error message "<message>"
  Examples:
  | username        | message                                      |
  | nonexistentuser | User with username 'nonexistentuser' not found |
  | deleteduser     | User with username 'deleteduser' not found     |

  #error flow
  Scenario Outline: Attempt to view favorite recipes of a non-existent user
    Given no user exists with username "<username>"
    When I attempt to view the favorite recipes of user "<username>"
    Then I should see an error message "<message>"
  Examples:
  | username        | message                                      |
  | nonexistentuser | User with username 'nonexistentuser' not found |
  | deleteduser     | User with username 'deleteduser' not found     |
