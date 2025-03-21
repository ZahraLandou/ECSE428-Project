Feature: US019 View User Profile
  As a NomNom user,
  I want to view a user's profile,
  So that I can see their biography and posted recipes.

  #background
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

  #normal_flow
  Scenario Outline: Successfully view a user's profile
    Given a user with username "<username>" exists
    When I request to view the profile of user "<username>"
    Then the profile should be displayed successfully
    And the profile should show the biography "<biography>"
    And the profile should display a total of "<expectedCount>" posted recipes

    Examples:
      | username      | biography                 | expectedCount |
      | foodlover     | I love cooking!           | 2             |
      | dessertchef   | Dessert specialist        | 3             |
      | newbie        | Just joined NomNom        | 0             |

  @normal_flow
  Scenario Outline: View recipes posted by a user
    Given a user with username "<username>" exists
    When I request to view the recipes of user "<username>"
    Then the recipes should be displayed successfully
    And I should see "<count>" recipes in the list

    # Use separate columns for individual titles if you want
    # to verify each recipe. Or a single title if you just
    # want to confirm at least one recipe is present.
    And the list should include the recipe(s):
      | <recipeTitle1> |
      | <recipeTitle2> |
      | <recipeTitle3> |

    Examples:
      | username      | count | recipeTitle1         | recipeTitle2      | recipeTitle3       |
      | foodlover     | 2     | Chocolate Cake       | Pasta Carbonara   |                    |
      | dessertchef   | 3     | Tiramisu            | Apple Pie         | Chocolate Mousse   |
      | newbie        | 0     |                      |                   |                    |

  @normal_flow
  Scenario Outline: View a user's favorite recipes
    Given a user with username "<username>" exists
    When I request to view the favorite recipes of user "<username>"
    Then the favorite recipes should be displayed successfully
    And I should see "<count>" favorite recipes

    # If a user has zero favorites, you can check an empty list;
    # otherwise verify specific titles.
    And the list of favorites should include:
      | <favTitle1>   |
      | <favTitle2>   |

    Examples:
      | username      | count | favTitle1        | favTitle2      |
      | foodlover     | 1     | Chocolate Cake   |                |
      | dessertchef   | 2     | Tiramisu         | Apple Pie      |
      | newbie        | 0     |                  |                |

  @error_flow
  Scenario Outline: Attempt to view a non-existent user profile
    Given no user exists with username "<username>"
    When I attempt to view the profile of user "<username>"
    Then I should see a profile error message "<message>"

    Examples:
      | username        | message                                       |
      | nonexistentuser | User with username 'nonexistentuser' not found|
      | deleteduser     | User with username 'deleteduser' not found    |

  @error_flow
  Scenario Outline: Attempt to view recipes of a non-existent user
    Given no user exists with username "<username>"
    When I attempt to view the recipes of user "<username>"
    Then I should see a profile error message "<message>"

    Examples:
      | username        | message                                       |
      | nonexistentuser | User with username 'nonexistentuser' not found|
      | deleteduser     | User with username 'deleteduser' not found    |

  @error_flow
  Scenario Outline: Attempt to view favorite recipes of a non-existent user
    Given no user exists with username "<username>"
    When I attempt to view the favorite recipes of user "<username>"
    Then I should see a profile error message "<message>"

    Examples:
      | username        | message                                       |
      | nonexistentuser | User with username 'nonexistentuser' not found|
      | deleteduser     | User with username 'deleteduser' not found    |
