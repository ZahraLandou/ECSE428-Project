Feature: US018 Viewing Favourite Recipes
    As a NomNom user,
    I want to view my list of favourite recipes,
    So that I can go back to recipes I like and read them.

    Background:
        Given the following users exist in the system
            | username | emailAddress     | password |
            | Coco     | coco@gmail.com   | pass1    |
            | Tim      | tim@gmail.com    | pass2    |
            | Jim      | jim@gmail.com    | pass3    |

        And the following recipes exist in the system
            | title           | description              | category  | instructions                                      | ingredients                         |
            | Pasta Carbonara | Classic Italian dish     | Dinner    | Cook pasta, add eggs and cheese                   | Pasta, Eggs, Cheese                 |
            | Avocado Toast   | Healthy breakfast option | Breakfast | Toast bread, mash avocado, and spread on toast    | Bread, Avocado, Lemon               |
            | Chicken Curry   | Spicy and flavorful      | Lunch     | Cook chicken in coconut milk with curry powder    | Chicken, Coconut milk, Curry Powder |

        And the following recipe lists exist in the system
            | username | listName        | category   | recipes                    |
            | Coco     | My Favorites    | Favorites  | Pasta Carbonara, Avocado Toast |
            | Tim      | Favorite Meals  | Favorites  | Chicken Curry                 |

    Scenario Outline: User views their favourite recipes
        Given I am the user "<username>"
        When I request to view my Favorites recipe list
        Then I should see the recipes "<recipes>" in the list

        Examples:
            | username | recipes                         |
            | Coco     | Pasta Carbonara, Avocado Toast  |
            | Tim      | Chicken Curry                   |

    Scenario Outline: User with no favourite recipes sees an empty list
        Given I am the user "<username>"
        When I attempt to view my Favorites recipe list
        Then I should see an error message "<message>"
        Examples:
            | username| message                                       |
            | Jim     |No favorites lists found for the provided user |