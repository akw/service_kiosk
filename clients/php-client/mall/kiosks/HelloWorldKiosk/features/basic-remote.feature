Feature: Basic Kiosk Resource Operations
  In order to test and validate the basic operations of a resource in a kiosk
  As a kiosk client developer
  I want to be able to call a kiosk and get responses

  Note: Kiosk resource service calls always return a JSON map.
    Any exceptions thrown come from the infrastructure not working properly.

  Background:
    Given the kiosk found at 'http://localhost:9901/Toolshed'
      And the resource 'HelloWorldKiosk' in the kiosk

  Scenario: Basic List Call
    When the resource's list method is called
    Then the results include 'greeting' with value 'Hello World'

  Scenario: Error List Call
    When the resource's list method is called with input '{"attempt": "fail"}'
    Then the results include '_ERROR_' with value 'TEST ERROR'

  Scenario: Basic Read Call
    When the resource's read resource '4' method is called
    Then the results include 'greeting' with value 'Hello Reader 4'

  Scenario: Basic Create Call
    When the resource's create method is called with input '{"box": "Top"}'
    Then the results include 'greeting' with value 'Hello Top'

  Scenario: Basic Update Call
    When the resource's update resource '4' method is called with input '{"box": "Lambs"}'
    Then the results include 'greeting' with value 'Hello Lambs'

  Scenario: Basic Delete Call
    When the resource's delete resource '6' method is called
    Then the results include 'status' with value 'ok'
