Feature: US020 Add collections of recipes
    As a NomNom user, I want to create collections of recipes
    that I can name in order to more easily access certain types
    of recipes I enjoy. For example, I can create a "deserts" collection
    for when I'm craving some sugary treat. This would save me from wasting
    time scrolling through all of my recipes marked as "favorite".

    # normal flow
    Scenario Outline: Successfully create a collection with a collection name, two recipes
        Given a recipe with title "<recipeName1>" exists
        And a recipe with title "<recipeName2>" exists
        When I create a collection with name "<collectionName>" that contains "<recipeName1>" and "<recipeName2>"
        Then the collection with name "<collectionName>" should exist in the system with recipes "<recipeName1>" and "<recipeName2>"
        Examples:
            | recipeName1          | recipeName2   | collectionName       |
            | Papa's Crème brûlée  | Custard bread | Best deserts         |
            | New-York pizza       | Fried chicken | Junk food collection |

    # error flow
    Scenario Outline: Attempt to create a collection no collection name
        Given a recipe with title "<recipeName1>" exists
        And a recipe with title "<recipeName2>" exists
        When I create a collection with name "<collectionName>" that contains "<recipeName1>" and "<recipeName2>"
        Then the collection with name "<collectionNameOld>" should not exist in the system
        And I should see an error message "<message>" (not common)
        Examples:
            | recipeName1          | recipeName2   | collectionName | message                                        |
            | Papa's Crème brûlée  | Custard bread |                | Error, cannot create a collection with no name |
            | New-York pizza       | Fried chicken |                | Error, cannot create a collection with no name |

