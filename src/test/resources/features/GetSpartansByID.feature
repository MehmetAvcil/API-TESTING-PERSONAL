Feature: GET /api/Spartans/{id}

  User Story:
  As an Administrator
  I want to search for a specific Spartan using their unique ID
  So that I can view their valid individual contact information and status instantly.

  @Happy
  Scenario Outline: Fetch an existing Spartan with valid credentials
    Given the admin is authenticated with valid credentials
    When the admin requests a Spartan profile with id "<id_key>" from "get_spartans_by_id_data.json"
    Then the status code must be 200
    And the response body should contain the expected Spartan data

    Examples:
      | id_key           |
      | existing_id_1    |
      | existing_id_2    |

  @Happy
  Scenario Outline: Spartan names must meet minimum length requirements
    Given the admin is authenticated with valid credentials
    When the admin requests a Spartan profile with id "<id_key>" from "get_spartans_by_id_data.json"
    Then the first name length should be at least 6 characters
    And the last name length should be at least 6 characters

    Examples:
      | id_key        |
      | existing_id_1 |
      | existing_id_2 |



  @Sad
  Scenario Outline: Attempt to fetch a Spartan profile with invalid credentials
    Given the admin is authenticated with invalid credentials
    When the admin requests a Spartan profile with id "<id_key>" from "get_spartans_by_id_data.json"
    Then the status code must be 401
    And the WWW-Authenticate header should contain the expected error message

    Examples:
      | id_key           |
      |existing_id_1_invalid_auth|
      |existing_id_2_invalid_auth|

  @Sad
  Scenario Outline: Fetch a Spartan profile that does not exist
    Given the admin is authenticated with valid credentials
    When the admin requests a Spartan profile with id "<id_key>" from "get_spartans_by_id_data.json"
    Then the status code must be 404
    And the response body should be empty

    Examples:
      | id_key              |
      | non_existent_id_1   |
      | non_existent_id_2   |