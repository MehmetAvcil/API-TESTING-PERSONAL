Feature: GET /api/Courses

  User Story:
As a Student
I want to request a list of all courses available
So that I can browse the academic catalog and see what is offered.

  @Happy
  Scenario: Validate course catalog retrieval
  Given the courses endpoint is available
  When the user sends a GET request to the courses endpoint
  Then the response status code should be 200
  And the response body should contain valid course data
