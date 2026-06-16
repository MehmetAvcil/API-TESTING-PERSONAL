Feature: POST /api/Spartans

  User Story: As an Administrator
  I want to add a brand new Spartan profile to the system
  So that new students or staff members can be legally tracked in our database.



  Scenario Outline: Successfully create a new Spartan profile with valid data
    Given the administrator has an authorized session
    When a POST request is sent to the Spartan profile endpoint using the "<profile_key>" data from "post_spartans_data.json"
    Then the API response status code should be 201
    And the response body should contain the newly created Spartan's ID
    And the response body should reflect the submitted profile details

    Examples:
      | profile_key       |
      | happy_path_min_length      |
      | happy_path_max_length      |


  Scenario Outline: Fail to create a Spartan profile when submitted data is invalid
    Given the administrator has an authorized session
    When a POST request is sent to the Spartan profile endpoint using the "<profile_key>" data from "post_spartans_data.json"
    Then the API response status code should be 400
    And the response body should contain the expected validation error message

    Examples:
      | profile_key            |
      | sad_path_missing_firstname |
      | sad_path_missing_lastname  |
      | sad_path_missing_course    |
      | sad_path_firstname_below_min |
      | sad_path_lastname_below_min  |
      | sad_path_firstname_above_max |
      | sad_path_lastname_above_max  |