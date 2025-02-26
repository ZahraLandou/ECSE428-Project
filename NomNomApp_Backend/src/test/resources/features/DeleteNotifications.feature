Feature: Delete all notifications
    As a NomNom User,
    I want to delete all the notifications in my inbox,
    so that I can keep my inbox clean and organized.

    Background:
        Given the NomNom application service is running

    # normal flow
    Scenario: Successfully delete all notifications
        Given the user is logged into the NomNom application
        And the user has the following notifications in their inbox
            | description                | date       |
            | John liked your recipe     | 2025-02-25 |
            | Terms and Services updated | 2025-02-22 |
        When the user deletes all notifications in their inbox
        Then the NomNom application should display "<message>"
        And the user has no notifications in their inbox
        Examples:
            | message              |
            | No new notifications |

    # alternate flow
    Scenario: Successfully delete all notifications from an empty inbox
        Given the user is logged into the NomNom application
        And the user has no notifications in their inbox
        When the user deletes all notifications in their inbox
        Then the NomNom application should display "<message>"
        And the user has no notifications in their inbox
        Examples:
            | message              |
            | No new notifications |

    # error flow
    Scenario: Unuccessfully delete all notifications while logged out
        Given the user is not logged into the NomNom application
        And the user has the following notifications in their inbox
            | description                | date       |
            | John liked your recipe     | 2025-02-25 |
            | Terms and Services updated | 2025-02-22 |
        When the user deletes all notifications in their inbox
        Then the system should redirect the user to the login page
        And the user has the following notifications in their inbox
            | description                | date       |
            | John liked your recipe     | 2025-02-25 |
            | Terms and Services updated | 2025-02-22 |