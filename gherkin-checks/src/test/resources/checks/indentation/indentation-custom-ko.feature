 # Noncompliant {{Indent this line at column 0 (currently indented at column 1).}}
 @tag
 # Noncompliant {{Indent this line at column 0 (currently indented at column 1).}}
 Feature: My feature...
    blabla...
    # Noncompliant {{Indent this line at column 4 (currently indented at column 5).}}
     blabla...

     # Noncompliant {{Indent this line at column 4 (currently indented at column 5).}}
     Background: blabla...
        blabla...
       # Noncompliant {{Indent this line at column 8 (currently indented at column 7).}}
       blabla...
          # Noncompliant {{Indent this line at column 8 (currently indented at column 10).}}
          Given blabla...

     # Noncompliant {{Indent this line at column 4 (currently indented at column 5).}}
     @tag
   # Noncompliant {{Indent this line at column 4 (currently indented at column 3).}}
   Scenario: Scenario 1
         # Noncompliant {{Indent this line at column 8 (currently indented at column 9).}}
         blabla...
        blabla...
        Given blabla...
        When blabla...
        Then blabla...

    @tag
     # Noncompliant {{Indent this line at column 4 (currently indented at column 5).}}
     @tag @tag
   # Noncompliant {{Indent this line at column 4 (currently indented at column 3).}}
   Scenario Outline: Scenario 2
       # Noncompliant {{Indent this line at column 8 (currently indented at column 7).}}
       blabla...
        blabla...
        Given blabla...
        When blabla...
            | data |
            # Noncompliant {{Indent this line at column 12 (currently indented at column 13).}}
             | 2    |
        Then blabla...
             # Noncompliant {{Indent this line at column 12 (currently indented at column 13).}}
             """string
            blabla...
              blabla...
           """
           # Noncompliant [[sl=-1]] {{Indent this line at column 12 (currently indented at column 11).}}

         # Noncompliant {{Indent this line at column 8 (currently indented at column 9).}}
         @tag @tag
         # Noncompliant {{Indent this line at column 8 (currently indented at column 9).}}
         Examples: blabla...
             # Noncompliant {{Indent this line at column 12 (currently indented at column 13).}}
             blabla...
            blabla...
           # Noncompliant {{Indent this line at column 12 (currently indented at column 11).}}
           | data |
             # Noncompliant {{Indent this line at column 12 (currently indented at column 13).}}
             | 1    |
            | 2    |