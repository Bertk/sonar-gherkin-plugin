Feature: My feature only given steps to add to background...
  Blabla...

  # Noncompliant [[sc=3;ec=13;secondary=9,10,15,16]] {{Add all common Given steps to Background.}}
  Background:
    Given My car is blue

  Scenario: Scenario 1 only given steps to add to background
    Given My bike is green
    And My book is black
    When Blabla when...
    Then Blabla then...

  Scenario: Scenario 2 only given steps to add to background
    Given My bike is green
    And My book is black
    When Blabla when...
    Then Blabla then...
