Feature: View collection of recipes
    As a NomNom user,
    I want to view my lists of recipes in a separate "lists" view,
    so that I can revisit what recipes I saved in an organized manner.

    # normal flow
    Scenario: Successfully view collection of recipes (normal flow)
        Given the user is logged into the NomNom application
        And the user has created the "<name>" list, of category "<category>"
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
            | name                      | category |
            | Historical Cuisine To Try | Regular  |

    # alternative flow
    Scenario: Successfully view empty collection of recipes (alternative flow)
        Given the user is logged into the NomNom application
        And the user has created the "<name>" list, of category "<category>"
        And the user has no recipes saved their "<name>" list
        When the user navigates to the "<name>" list
        Then the NomNom application should display "<message>"

        Examples:
            | name                      | category | message                                |
            | Historical Cuisine To Try | Regular  | You have no recipes in this collection |

    # error flow
    Scenario: Unsuccessfully view another user's privated collection of recipes (error flow)
        Given the user is logged into the NomNom application
        And a different user has created the "<name>" list, of category "<category>"
        And the different user has privated the "<name>" list
        And the different user has saved the following recipes in the "<name>" list
            | name                    | description                          |
            | Victorian Hot Chocolate | A delicious old-timey cocoa brew     |
            | Medieval Fantasy Gruel  | A warm hearty stew to heal your soul |
        When the user navigates to the other user's "<name>" list
        Then the NomNom application should display "<message>"
        Examples:
            | name                      | category | message                    |
            | Historical Cuisine To Try | Regular  | This collection is private |
