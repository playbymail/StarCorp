#summary Star System Entities: Planets
#labels Phase-Design

= Introduction =

Planets are entities orbiting a star system.  Some are habitable but most are inhospitable to life.

= Gravity Rating =

Planets have a gravity rating which limits the [Starships ships] that can orbit or dock on them.  

Gas Giants typically have a very high gravity and no planetary map.

= Atmosphere =

Each planet has a [AtmosphereType type of atmosphere] which determines how life-sustaining it is.

= Moons =

Some planets may orbit another planet thus making them a moon of the planet in question. 

= Planetary Map =

Each planet (except Gas Giants) has a 2-dimensional map where each square has its own [Terrain] and potentially a number of [Resources resource] deposits which can be gathered by suitable [Facilities facilities].

Each map square can contain a single [Colonies colony] and any number of [Starships ships] which have docked there.