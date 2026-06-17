Feature: GET /api/Spartans

  User Story:
  As an Administrator
  I want to retrieve a complete roster of all registered Spartans
  So that I can audit the active database of users on the platform.

  @Happy
  Scenario: Get Spartans search with valid credentials
    Given the user has an authorized session
    When the user sends a GET request to the Spartans endpoint
    Then the Spartan registry response status code should be 200
    And the response body should contain valid Spartans data

    @Sad
    Scenario Outline: Get Spartans search with invalid credentials
      Given the user has an unauthorized session with token "<bearer_token>" from "get_spartans_data.json"
      When the user sends a GET request to the Spartans endpoint
      Then the Spartan registry response status code should be 401
      And the response header error message be expected file message
      Examples:
        | bearer_token   |
        | empty_token  |
        | wrong_token  |