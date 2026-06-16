Feature: DELETE /api/Spartans/{id}
User Story:
As an Administrator
I want to delete an existing Spartan record
So that their profile is permanently removed from the database


  Scenario Outline: Successfully delete an existing Spartan profile
    Given the administrators has an authorized session
    And an existing Spartan profile is available to delete using the "<profile_key>" data from "delete_spartans_data.json"
    When a DELETE request is sent to the Spartan profile endpoint
    Then the response code should be 204

    Examples:
      | profile_key                |
      | delete_happy_path          |