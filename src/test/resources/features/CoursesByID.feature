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
      | happy_path_id1      |
      | happy_path_id2      |
      | happy_path_id3      |

    @Sad
  Scenario Outline: Single course search with invalid course id
    Given the courses by ID endpoint is available
    When the user sends a GET request for a single course using id "<id_key>" from "courses_by_id_data.json"
    Then the status code must match the file expectation

    Examples:
      | id_key             |
      | sad_path_wrong_id1  |

@Sad
  Scenario Outline: Single course search with invalid input
    Given the courses by ID endpoint is available
    When the user sends a GET request for a single course using id "<id_key>" from "courses_by_id_data.json"
    Then the status code must match the file expectation
    And the response body should contain error message that includes "is invalid"

    Examples:
      | id_key             |
      | sad_path_wrong_id2  |