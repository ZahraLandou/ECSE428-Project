Feature: View collection of recipes
    As a NomNom user,
    I want to view my lists of recipes in a separate "lists" view,
    so that I can revisit what recipes I saved in an organized manner.

    # normal flow
    Scenario: Successfully view collection of recipes (normal flow)
        Given the user is logged into the NomNom application
        And the user has created the "<name>" list, of category "Regular"
        And the user has saved the following recipes in the "<name>" list
            | name                    | description                          |
            | Victorian Hot Chocolate | A delicious old-timey cocoa brew     |
            | Medieval Fantasy Gruel  | A warm hearty stew to heal your soul |
        When the user navigates to the "<name>" list
        Then the NomNom application should display the following recipes, their description, and their other attributes
            | name                    | description                          |
            | Victorian Hot Chocolate | A delicious old-timey cocoa brew     |
            | Medieval Fantasy Gruel  | A warm hearty stew to heal your soul |

        Examples:
            | name                      |
            | Historical Cuisine To Try |

    # alternative flow
    Scenario: Successfully view empty collection of recipes (alternative flow)
        Given the user is logged into the NomNom application
        And the user has created the "<name>" list, of category "Regular"
        And the user has no recipes saved in their "<name>" list
        When the user navigates to the "<name>" list
        Then the NomNom application should display the empty "<name>" list with no recipes

        Examples:
            | name                      |
            | Historical Cuisine To Try |

    # error flow
    Scenario: Unsuccessfully view collection of recipes while logged out (error flow)
        Given the user is not logged into the NomNom application
        When the user navigates to the "<name>" list
        Then the NomNom application should inform the user they are not logged in
        Examples:
            | name                      |
            | Historical Cuisine To Try |
