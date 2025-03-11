Feature: US021 Modify collections of recipes
    As a NomNom user, I want to create collections of recipes
    that I can name in order to more easily access certain types
    of recipes I enjoy. For example, I can create a "deserts" collection
    for when I'm craving some sugary treat. This would save me from wasting
    time scrolling through all of my recipes marked as "favorite".

    # normal flow
    Scenario Outline: Successfully add a recipe to an existing collection
        Given a recipe with title "<recipeName1>" exists (for collections)
        And a recipe with title "<recipeName2>" exists (for collections)
        And a recipe with title "<recipeName3>" exists (for collections)
        When I create a collection with name "<collectionName>" that contains "<recipeName1>" and "<recipeName2>"
        And I add "<recipeName3>" to collection "<collectionName>"
        Then the collection with name "<collectionName>" should exist in the system with recipes "<recipeName1>", "<recipeName2>", and "<recipeName3>"
        Examples:
            | recipeName1          | recipeName2   | recipeName3          | collectionName       |
            | Papa's Crème brûlée  | Custard bread | Lemon pie            | Best deserts         |
            | New-York pizza       | Fried chicken | Joe's best hamburger | Junk food collection |

    # normal flow
    Scenario Outline: Successfully remove a recipe from an existing collection
        Given a recipe with title "<recipeName1>" exists (for collections)
        And a recipe with title "<recipeName2>" exists (for collections)
        And a recipe with title "<recipeName3>" exists (for collections)
        When I create a collection with name "<collectionName>" that contains "<recipeName1>", "<recipeName2>", and "<recipeName3>"
        And I delete recipe "<recipeName3>" from the collection "<collectionName>"
        Then the collection with name "<collectionName>" should exist in the system with recipes "<recipeName1>" and "<recipeName2>"
        And the collection with name "<collectionName>" should not contain recipe "<recipeName3>"
        Examples:
            | recipeName1          | recipeName2   | recipeName3          | collectionName       |
            | Papa's Crème brûlée  | Custard bread | Lemon pie            | Best deserts         |
            | New-York pizza       | Fried chicken | Joe's best hamburger | Junk food collection |

    # normal flow
    Scenario Outline: Successfully change a collection name
        Given a recipe with title "<recipeName1>" exists (for collections)
        And a recipe with title "<recipeName2>" exists (for collections)
        When I create a collection with name "<collectionNameOld>" that contains "<recipeName1>" and "<recipeName2>"
        And I modify the name of the collection with name "<collectionNameOld>" to "<collectionNameNew>"
        Then the collection with name "<collectionNameNew>" should exist in the system with recipes "<recipeName1>" and "<recipeName2>"
        And the collection with name "<collectionNameOld>" should not exist in the system
        Examples:
            | recipeName1          | recipeName2   | collectionNameOld    | collectionNameNew   |
            | Papa's Crème brûlée  | Custard bread | Best deserts         | Best French Deserts |
            | New-York pizza       | Fried chicken | Junk food collection | Junk with the boys  |

    # error flow
    Scenario Outline: Attempt to remove a recipe that does not exist from an existing collection
        Given a recipe with title "<recipeName1>" exists (for collections)
        And a recipe with title "<recipeName2>" exists (for collections)
        When I create a collection with name "<collectionName>" that contains "<recipeName1>" and "<recipeName2>"
        And I delete recipe "French Flan" from the collection "<collectionName>"
        Then I should see an error message "<message>" (collection modify error)
        Examples:
            | recipeName1          | recipeName2   | collectionName       | message                                              |
            | Papa's Crème brûlée  | Custard bread | Best deserts         | Error, this recipe does not exist in this collection |
            | New-York pizza       | Fried chicken | Junk food collection | Error, this recipe does not exist in this collection |