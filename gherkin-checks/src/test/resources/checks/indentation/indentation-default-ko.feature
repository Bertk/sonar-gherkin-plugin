# Noncompliant [[sc=2;ec=3]] {{Indent this token at column 1 (currently indented at column 2).}}
 @tag
@abc
  # Noncompliant [[sc=3;ec=10]] {{Indent this token at column 1 (currently indented at column 3).}}
  Feature: My feature indentation default KO
    # Noncompliant [[sc=5;ec=14]] {{Indent this token at column 3 (currently indented at column 5).}}
    Blabla...
  Blabla...

 # Noncompliant [[sc=2;ec=12]] {{Indent this token at column 3 (currently indented at column 2).}}
 Background: Blabla background indentation default KO
  # Noncompliant [[sc=3;ec=12]] {{Indent this token at column 5 (currently indented at column 3).}}
  Blabla...
    Blabla...
    # Noncompliant [[sc=6;ec=12]] {{Indent this token at column 5 (currently indented at column 6).}}
     Given Blabla given1...

    # Noncompliant [[sc=5;ec=6]] {{Indent this token at column 3 (currently indented at column 5).}}
    @def
  # Noncompliant [[sc=4;ec=12]] {{Indent this token at column 3 (currently indented at column 4).}}
   Scenario: Scenario 1 indentation default KO
    Blabla...
    # Noncompliant [[sc=8;ec=17]] {{Indent this token at column 5 (currently indented at column 8).}}
       Blabla...
    Given Blabla given...
    When Blabla when...
    Then Blabla then...

  @ghi
    # Noncompliant [[sc=5;ec=6]] {{Indent this token at column 3 (currently indented at column 5).}}
    @jkl
   # Noncompliant [[sc=4;ec=20]] {{Indent this token at column 3 (currently indented at column 4).}}
   Scenario Outline: Scenario 2 indentation default KO
    # Noncompliant [[sc=6;ec=15]] {{Indent this token at column 5 (currently indented at column 6).}}
     Blabla...
    Blabla...
    Given Blabla given...
    When Blabla when...
      | data |
       # Noncompliant [[sc=8;ec=16]] {{Indent this token at column 7 (currently indented at column 8).}}
       | 2    |
    Then Blabla then...<data>
      # Noncompliant [[sc=8;ec=11]] {{Indent this token at column 7 (currently indented at column 8).}}
       """string
      Blabla...
        Blabla...
     """
    # Noncompliant [[sl=-1;sc=6;ec=9]] {{Indent this token at column 7 (currently indented at column 6).}}

     # Noncompliant [[sc=6;ec=7]] {{Indent this token at column 5 (currently indented at column 6).}}
     @mno
     # Noncompliant [[sc=6;ec=14]] {{Indent this token at column 5 (currently indented at column 6).}}
     Examples: Blabla examples indentation default KO
      Blabla...
      # Noncompliant [[sc=8;ec=17]] {{Indent this token at column 7 (currently indented at column 8).}}
       Blabla...
     # Noncompliant [[sc=6;ec=14]] {{Indent this token at column 7 (currently indented at column 6).}}
     | data |
       # Noncompliant [[sc=8;ec=16]] {{Indent this token at column 7 (currently indented at column 8).}}
       | 1    |
      | 2    |

  Scenario: Scenario 3 -- indentation default KO
    Blabla...
    Given Blabla given...
    When Blabla when
    Then Blabla then...

  Scenario Outline: Scenario 4 -- indentation default KO
    Blabla...
    Given Blabla given...
    When Blabla when...<data>
    Then Blabla then...

    Examples: Blabla examples indentation default KO
      | data |
      | 1    |
      | 2    |
