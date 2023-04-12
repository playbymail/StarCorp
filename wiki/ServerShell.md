#summary Server Shell Program
#labels Phase-Design

= Introduction =

The server is run as a shell application.  The game host may issue commands to run turns and edit the game environment.

= Commands =

== help (command) ==

Prints command help or a list of commands if command is blank or not found.

== echo ==

Prints out whatever is provided as an argument.

== ps ==

Prints out a list of running tasks.

== quit ==

Exits the server shell.

== date (Month) (Year) ==

Displays the current game date or sets it to the specified values.

== update (Type) ==

Runs one of the game processor based on type argument.  Valid types:
  * inc - increments the galactic date and resets ship / facility used time / transactions
  * pop - runs PopulationProcessor
  * npc - runs NonPlayerCorporationProcessor 
  * fac - runs FacilityProcessor
  * fetch - runs TurnFetcher
  * turns - runs TurnProcesor
  * all - runs all of above in sequence.

== turn corp (Corporation ID) ==

Starts a new turn submission for the given corporation.

== turn order (Order) (Order Arguments) ==

Adds a new order to the current turn.

== turn submit ==

Submits the current turn.

== turn save ==

Saves current turn (and report if submitted) to file.

== turn load ==

Loads current turn from saved file.

== items (filter) ==

Prints out item types. Filter is optional and filters items to contain the filter string.  An item is listed if:
  * The item class matches the filter (case insensitive). e.g. resources will return all resources.
  * If the item name or key contains the filter (case insensitive).

== facilities ==

Prints out facility types.

== population ==

Prints out PopulationClass names.

== name (Entity Class) (ID) (Name) ==

Sets the name of an entity which has a name field.  Valid classes are:
  * Colony
  * Corporation
  * StarshipDesign
  * StarSystem
  * StarSystemEntity
  * Planet
  * Starship

== view (Entity Class) (ID) (xml?) ==

Prints out the specified entity.  If the third parameter is supplied and is "true" then the output is done in xml format.

Valid entity classes are:
  * AsteroidField
  * ColonistGrant
  * Colony
  * ColonyItem
  * Corporation
  * DevelopmentGrant
  * Facility
  * FacilityLease
  * GasField
  * MarketItem
  * Planet
  * ResourceDeposit
  * Starship
  * StarshipDesign
  * StarSystem
  * StellarAnomoly
  * Unemployed
  * Workers

== list (Entity Class) (Page) ==

Lists entities of specified class. 10 entities are listed per page.

== del (Entity Class) (ID) ==

Deletes the specified entity (and all child entities associated with it).

== export (Entity Class) (ID) ==

Exports the specified entity (or all of a class if no ID is specified) to an XML file called export.xml. If no entity class or ID is specified everything is exported.

== import (Filename) ==

Imports the entities specified in the file.  If there is an ID conflict, the entity will NOT be imported.

== hql (Query) ==

Performs a Hibernate Query using the provided query string.

== survey system (System ID) (Type) ==

Searches star system for planets suitable for colonisation. A planet is suitable if:
  * Gravity Rating <= 5
  * Atmosphere Type hazard level <= 1

It will list the 10 most suitable locations to colonise.  Type is optional and may correspond to:
  * Metal
  * Organic
  * Fuel
  * Gas
  * Fissile
  * Liquid
  * Mineral

== survey planet (Planet ID) (Type) ==

Searches a planet map for suitable locations for colonisation and displays the top 10 locations.  Type is optional (see above for suitable values).

== create planet (Template) (System ID) (quadrant) (orbit) (Name) ==

Creates a planet based on the PlanetTemplate at the specified location with the given name.

== create moon (Template) (Planet ID) (Name) ==

Creates a planet based on the PlanetTemplate orbiting the specified planet with the given name.

== create system (Template) (x) (y) (z) (Name) ==

Creates a new star system on the SystemTemplate at the specified location.

== create corp (Credits) (Name) ==

Creates a new corporation.

== create colony (Template) (Corporation ID) (Planet ID) (x) (y) (Name) ==

Creates a new colony based on the ColonyTemplate owned by the specified corporation at the specified location.

== create design (Corporation ID) (Name) (Hull type 1) (Hull type 2) ... (Hull type n) ==

Creates a new starship design owned by the specified corporation.  The name should not have any spaces.

== create ship (Corporation ID) (Design ID) (Colony ID) (Name) ==

Creates a new starship.

== create facility (Corporation ID) (Colony ID) (Type) ==

Creates a new facility.

== inject market (Corporation ID) (Colony ID) (Type) ==

Injects a random selection of items of the specified type put on sale by the corporation specified.  Type may be:
  * [Resources]
  * ConsumerGoods
  * IndustrialGoods
  * BuildingModules
  * StarshipHulls

== inject items (Corporation ID) (Colony ID) (Type) ==

Injects a random selection of items of the specified type into the inventory of the corporation specified.  Type may be:
  * [Resources]
  * ConsumerGoods
  * IndustrialGoods
  * BuildingModules
  * StarshipHulls

== script (Script File) ==

Runs the specified script file.  A script contains any of the listed commands, one each per line.  You can put # symbols to mark comments.  Anything after the # is ignored.
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 