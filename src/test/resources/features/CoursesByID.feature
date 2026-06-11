Feature: GET /api/Courses/{id}

  User Story:
  As a Student
  I want to request the details of a specific course by providing its unique ID
  So that I can review the description, syllabus, and specific details of that single course.

  @Happy
Scenario Outline: Single course search with valid course id
  Given the courses by ID endpoint is available
  When the user sends a GET request for a single course using id "<id_key>" from "courses_by_id_data.json"
  Then the status code must match the file expectation
  And the response body should contain expected course data

  Examples:
      | id_key             |
      | valid_id_1      |
      | valid_id_2      |
      | valid_id_3      |

    @Sad
  Scenario Outline: Single course search with nonexistent course id
    Given the courses by ID endpoint is available
    When the user sends a GET request for a single course using id "<id_key>" from "courses_by_id_data.json"
    Then the status code must match the file expectation

    Examples:
      | id_key             |
      | invalid_id_1  |
      | invalid_id_2  |

@Sad
  Scenario Outline: Single course search with invalid input
    Given the courses by ID endpoint is available
    When the user sends a GET request for a single course using id "<id_key>" from "courses_by_id_data.json"
    Then the status code must match the file expectation
    And the error message should indicate the value is invalid

    Examples:
      | id_key             |
      | invalid_input_1  |
      | invalid_input_2  |