Feature: GET /api/Spartans/{id}

  User Story:
  As an Administrator
  I want to search for a specific Spartan using their unique ID
  So that I can pull up their individual contact information and status instantly.

  Scenario Outline: Parameterized single Spartan record fetch
    Given the admin has credentials "<scenario>" from "get_spartans_by_id_data.json"
    When the framework requests a single profile using an id
    Then the profile response status code must match the file expectation
    And the response body should contain valid Spartan data if applicable

    Examples:
      | scenario |
      | valid_credentials_and_existing_spartan |
      | invalid_credentials_and_existing_spartan |
      | valid_credentials_and_non_existent_spartan |