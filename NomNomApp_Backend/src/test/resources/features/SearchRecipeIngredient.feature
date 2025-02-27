Feature: US012 Search for a recipe ingredient
  As a NomNom user,
  I want to search for a specific recipe ingredient,
  So that I can quickly locate it for my cooking.

  Background:
    Given the following recipe ingredients exist:
      | name      | quantity | unit   |
      | shrimp    | 200      | grams  |
      | garlic    | 2        | cloves |
      | basil     | 10       | leaves |

  # Normal flow: Successfully find an existing recipe ingredient by name
  Scenario Outline: Successfully find an existing recipe ingredient
    When I search for the recipe ingredient "<name>"
    Then I should see the recipe ingredient details with name "<name>", quantity "<quantity>", and unit "<unit>"

    Examples:
      | name    | quantity | unit   |
      | shrimp  | 200      | grams  |
      | garlic  | 2        | cloves |

  # Alternate flow: Retrieve all recipe ingredients
  Scenario Outline: Retrieve all recipe ingredients
    When I request a list of all recipe ingredients
    Then I should see a recipe ingredient with name "<name>", quantity "<quantity>", and unit "<unit>"

    Examples:
      | name    | quantity | unit   |
      | shrimp  | 200      | grams  |
      | garlic  | 2        | cloves |
      | basil   | 10       | leaves |

  # Error flow: Attempt to find a non-existing recipe ingredient
  Scenario Outline: Attempt to search for a non-existing recipe ingredient
    Given the recipe ingredient "<name>" does not exist
    When I try to search for the recipe ingredient "<name>"
    Then I should see an error message "<message>"

    Examples:
      | name   | message                                               |
      | onion  | Recipe ingredient with name 'onion' not found         |
      | sugar  | Recipe ingredient with name 'sugar' not found         |
