#summary Project Release 0.9.0.0 features / plan
#labels Phase-Requirements

This is the initial public release.

= Features =

== Applications ==

  * ClientApplication
  * ServerShell

== Game Map ==

  * StarSystem
  * [Planets]
  * [AsteroidField]
  * [GasField]

== Game Environment ==
  * [Colonies]
  * [Population]
    * PopulationClass
    * [Employment]
    * NeedsAndHappiness
    * PopulationMigration
    * BirthAndDeath

== Player Entities ==

  * [Corporations]
  * [Facilities]
  * [Starships]

== Game Mechanics ==

  * [Items]
    * [Resources]
    * TradeGoods
      * ConsumerGoods
      * IndustrialGoods
    * BuildingModules
    * StarshipHulls
  * [FacilityTypes]
    * ColonyHub
    * OrbitalDock
    * ServiceFacilities
    * [Factories]
    * ResourceGenerators
  * [Orders]
  * Measurements
    * [Credits]
    * MassUnits
    * GalacticDate
    * TimeUnits
  * [Government]
    * FacilityLeases
    * DevelopmentGrants
    * ColonistGrants
  * ColonyMarkets
  * [Orbiting]
  * [Docking]
  * InterplanetaryMovement
  * InterstellarMovement
  * StarSystemExploration
  * PlanetaryExploration
  * SpaceMining

== Server Components ==
  * EntityPersistence
  * TurnFetcher
  * TurnProcessor
  * PopulationProcessor
  * FacilityProcessor