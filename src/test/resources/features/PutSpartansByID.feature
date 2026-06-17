Feature: PUT /api/Spartans/{id}

  As an Administrator
  I want to update all information fields on an existing Spartan record
  So that any complete changes to their personal profile are securely saved.

  Scenario Outline: Successfully update an existing Spartan profile with valid data
    Given the admin has an authorized session
    When a PUT request is sent to the Spartan profile endpoint using the "<profile_key>" data from "post_spartans_data.json"
    Then the API response code should be 200
    And the response body should reflect the updated profile details

    Examples:
      | profile_key       |
      | happy_path_min_length      |
      | happy_path_max_length      |


  Scenario Outline: Fail to update a Spartan profile when submitted data is invalid
    Given the admin has an authorized session
    When a PUT request is sent to the Spartan profile endpoint using the "<profile_key>" data from "post_spartans_data.json"
    Then the API response code should be 400
    And the response body should contain a validation error message

    Examples:
      | profile_key            |
      | sad_path_missing_firstname |
      | sad_path_missing_lastname  |
      | sad_path_missing_course    |
      | sad_path_firstname_below_min |
      | sad_path_lastname_below_min  |
      | sad_path_firstname_above_max |
      | sad_path_lastname_above_max  |