Feature: Login


User Story:
As a Registered User
I want to log into the application using my credentials
So that I can receive a secure authentication token to access restricted features.


  Scenario Outline: Validate authentication boundaries
    Given the user credentials are "<test_case_key>" from "auth_data.json"
    When the user sends a POST request to the login endpoint
    Then the response status code must match the expected value


    Examples:
      | test_case_key           |
      | happy_path_login        |
      | sad_path_wrong_password |

