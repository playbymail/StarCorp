#summary Server population processor
#labels Phase-Design

= Introduction =

The PopulationProcessor handles changes and activities performed by colony population.  It performs the following actions in the specified order:

  * For each [Colonies colony]
    * For each PopulationClass
      * For each [Facilities facility] (sorted by highest paying salary)
        * Hire workers if needed
        * Quit workers if failed quit check
        * Pay workers
      * For all workers
        * Reset happiness to 0
        * Pay any available colonist grants
        * Buy consumer goods
        * Use ServiceFacilities
      * For unemployed
        * Reset happiness to 0
        * Pay any available colonist grants
        * Buy consumer goods
        * Use ServiceFacilities
        * Migrate
    * BirthAndDeath
 