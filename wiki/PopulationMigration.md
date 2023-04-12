#summary Population Migration
#labels Phase-Design

= Introduction =

Unemployed colonists will migrate from their colony to another colony at the following rate each turn:

Happiness level of target colony PopulationClass - Happiness level of current colony PopulationClass

This level is modified by the distance away:
  * x 2 if on same planet
  * x 1 if on a planet in same star system location
  * / 2 if on a planet in same star system
  * / 5 if on a planet in different star system

The unemployed will move at this rate to colonies in the following order:
  * Colonies on same planet
  * Colonies in same star system location
  * Colonies in same star system
  * All other colonies

The migration percentage is applied to the total unemployed after each migration to a specific colony. e.g.

Assume the following scenario:
  * There are 100 unemployed technicians at colony A with a happiness level of 50%
  * Colony B on same planet has technician happiness of 80%
  * Colony C in same star system location happiness of 80%
  * Colony D in same star system has happiness of 90%
  * Colony E in a different star system has happiness of 80%

Migration would happen as follows:
  * Migration of Colony A to B: 30% x 2 = 60%. Migrants: 60. Remaining unemployed: 40.
  * Migration of Colony A to C: 30% x 1 = 30%. Migrants: 12. Remaining unemployed: 28.
  * Migration of Colony A to D: 40% / 2 = 20%. Migrants: 6. Remaining unemployed: 22.
  * Migration of Colony A to E: 30% / 5 = 6%. Migrants: 1. Remaining unemployed: 21.
 