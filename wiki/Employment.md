#summary Colonist employment
#labels Phase-Design

= Introduction =

Colonists are employed by facilities where they are paid a salary which goes towards their [NeedsAndHappiness needs].

= Unemployed =

Unemployed colonists of each PopulationClass sit waiting for work at a colony until they are hired by a facility, the [PopulationMigration migrate] or they [BirthAndDeath die].

= Hiring =

[Facilities] which are understaffed automatically hire unemployed colonists. Once hired they are removed from the colonies' unemployed pool and added to the facilities employee pool.

= Quitting =

The chance an employee of a facility may quit is: 

100 % - [NeedsAndHappiness happiness] level

Once an employee quits a facility they join the colonies unemployed pool (see above).

= Firing =

Employees are fired if:
  * The facility owner closes the facility.
  * If a facility owner cannot pay the colonists
  * If a facility owner changes the salary level to a REDUCED level
 
 
 
 
 