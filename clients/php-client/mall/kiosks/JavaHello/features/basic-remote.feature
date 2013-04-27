Feature: Hello Worlds
  In order to see the kiosk system work with a remote kiosk
  As a kiosk client developer
  I want to be able to call a remote kiosk and get a response

  Background:
    Given the kiosk found at 'http:///Sample'
      And the resource 'JavaHello' in the kiosk

  Scenario: Basic Remote List Call
    When the resource's list method is called
    Then the results include 'greeting' with value 'Hello World'

  Scenario: Basic Remote Read Call
    When the resource's read resource '4' method is called
    Then the results include 'greeting' with value 'Hello Reader 4'

  Scenario: Basic Remote Create Call
    When the resource's create method is called with input '{"box": "Top"}'
    Then the results include 'greeting' with value 'Hello Top'

  Scenario: Basic Remote Update Call
    When the resource's update resource '4' method is called with input '{"box": "Lambs"}'
    Then the results include 'greeting' with value 'Hello Lambs'

  Scenario: Basic Delete Call
    When the resource's delete resource '6' method is called
    Then the results include 'status' with value 'ok'
