@bar
Feature: My feature forbidden tag...
  Blabla...

  @foo
  Scenario: Scenario 1 forbidden tag
    Given Blabla given...
    When Blabla when...
    Then Blabla then...

  @mytag
  Scenario: Scenario 2 forbidden tag
    Given Blabla given...
    When Blabla when...
    Then Blabla then...
