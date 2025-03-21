Feature: US008 Like a recipe
  As a NomNom user,
  I want to like a recipe
  to show the owner of the recipe my appreciation


  # normal flow
  Scenario Outline: Successfully like a recipe
    Given a recipe with title "<title>" and amount of likes "<likes>" exists
    When a user likes the recipe "<title>"
    Then the recipe with title "<title>" should have "<expected>" likes
    Examples:
      | title  | likes | expected |
      | Toast  | 32    | 33       |

  # alternate flow
  Scenario Outline: Successfully unlike a recipe
    Given a recipe with title "<title>" and amount of likes "<likes>" exists
    When a user unlikes the recipe "<title>"
    Then the recipe with title "<title>" should have "<expected>" likes
    Examples:
      | title  | likes | expected |
     | Toast  | 32    | 31      |