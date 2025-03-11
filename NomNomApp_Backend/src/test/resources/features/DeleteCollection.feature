Feature: US020 Delete collections of recipes
    As a NomNom user, I want to create collections of recipes
    that I can name in order to more easily access certain types
    of recipes I enjoy. For example, I can create a "deserts" collection
    for when I'm craving some sugary treat. This would save me from wasting
    time scrolling through all of my recipes marked as "favorite".  
    
    # normal flow
    Scenario Outline: Successfully delete an existing recipe collection
        Given a recipe with title "<recipeName1>" exists (for collections)
        And a recipe with title "<recipeName2>" exists (for collections)
        When I create a collection with name "<collectionName>" that contains "<recipeName1>" and "<recipeName2>"
        And I delete a collection with name "<collectionName>"
        Then the collection with name "<collectionName>" should not exist in the system
        Examples:
            | recipeName1          | recipeName2   | collectionName       |
            | Papa's Crème brûlée  | Custard bread | Best deserts         |
            | New-York pizza       | Fried chicken | Junk food collection |

    # error flow
    Scenario Outline: Attempt to delete a non existent recipe collection
        When I delete a collection with name "<collectionName>"
        Then I should see an error message "<message>" (collection delete error)
        Examples:
            | collectionName       | message                     |
            | Best deserts         | Error, collection not found |
            | Junk food collection | Error, collection not found |