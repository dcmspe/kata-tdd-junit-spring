Feature: As a passenger. I want to do the check-in of my flights by the app from my cellphone. Sho that I can make easier
  my check-in with my cellphone and use it as my boarding pass.

  @Withdraw
  Scenario Outline: I want to make a check-in
    Given the user has made a valid login with an '<user>' and '<password>'
    And the user has a flight to checkin
    When the user make a checkin validation
    Then he needs to receive a valid notification that he made a check-in

    Examples:
      | user           | password   |
      | marques_pombal | capararica |
