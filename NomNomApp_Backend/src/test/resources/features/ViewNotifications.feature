#Feature: View notifications
#    As a NomNom User,
#    I want to view all the notifications in my inbox,
#    so that I am informed about updates and interactions with my account.
#
#    Background:
#        Given the NomNom application service is running
#
#    # normal flow
#    Scenario Outline: Successfully view all notifications in the inbox
#        Given the user is logged into the NomNom application
#        And the user has the following notifications in their inbox
#            | description                | date       |
#            | John liked your recipe     | 2025-02-25 |
#            | Terms and Services updated | 2025-02-22 |
#        When the user navigates to their notifications inbox
#        Then the NomNom application should display the following notifications, their description, and their date sent
#            | description                | date       |
#            | John liked your recipe     | 2025-02-25 |
#            | Terms and Services updated | 2025-02-22 |
#
#    # alternate flow
#    Scenario Outline: Successfully view all notifications in an empty inbox
#        Given the user is logged into the NomNom application
#        And the user has no notifications in their inbox
#        When the user navigates to their notifications inbox
#        Then the NomNom application should display "<message>"
#        Examples:
#            | message              |
#            | No new notifications |
#
#    # error flow
#    Scenario: Unsuccessfully view notifications while logged out
#        Given the user is not logged into the NomNom application
#        When the user navigates to their notifications inbox
#        Then the system should redirect the user to the login page