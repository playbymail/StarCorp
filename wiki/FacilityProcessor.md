#summary Server facility processor
#labels Phase-Design

= Introduction =

The FacilityProcessor performs the turns activities related to [Facilities].  The following actions are preferred in order:

  * For each [Facilities facility]
    * Set transaction count to 0
    * Set powered to false
    * Attempt use power (buying from the colony market if possible and needed)
    * If its a [Factories factory] work through the production queue
    * If its a [ResourceGenerators generator] produce applicable [Resources]
 
 