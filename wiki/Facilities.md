#summary Colony Facilities
#labels Phase-Design

= Introduction =

Facilities are immovable structures built within a colony that fall into the following categories:
  * ColonyHub
  * OrbitalDock
  * ServiceFacilities
  * [Factories]
  * ResourceGenerators

They are owned by a [Corporations corporation].

See also FacilityTypes.

= Building a facility =

To build a new facility at a colony a corporation must:
  * Own the appropriate FacilityLeases
  * Have the right number and type of BuildingModules at the colony

= Closing a facility =

A facility may be closed voluntary by its owner to avoid the cost of operating it in terms of power and employees (which are all fired). 

= Workers =

Facilities require a minimum number of [Population colonists] to operate at full efficiency.  They pay their workers a salary each turn, determined by the facility owner.  

Workers are automatically hired from the available pool of unemployed colonists at the colony.  They go to the facility paying the highest salary until the facility is full and then to the next highest paying and so on.

= Power =

In addition to workers all facilities require power to operate.  The amount of power required is determined by the type of facility.  If the facility doesn't meet its power requirements it operates at 0% efficiency (ie. is shut down until it acquires power).

Power is a TradeGoods produced by [Factories] from suitable [Resources].  When using power a facility will first attempt to meet its requirement from the available stockpile owned by the corporation at the colony.  If there is insufficient enough power in the corporation's private stockpile, the facility will automatically attempt to buy it on the ColonyMarkets for that colony at the cheapest available price.

= Service Charges =

Some facility types allow the owner to set a charge level (in credits) for their usage.

  * ColonyHub facilities service charge is per market transaction on the ColonyMarkets.
  * OrbitalDock facilities service charge is per market transaction via orbit
  * ServiceFacilities service charge is per colonist using it (see NeedsAndHappiness)
  * Factories and ResourceGenerators do no use a service charge.
 
 
 