#summary Star System Exploration
#labels Phase-Design

= Introduction =

Starships may perform a number of actions once per turn to explore the current StarSystem they are in or find near by stars.

= Galaxy Scan =

This will return a list of [StarSystem]s within the range of the ship's Long Range Scanners.  The name, type and location will be detailed.  This takes 10 TimeUnits.

= System Scan =

This will return a map of the star system detailing what asteroid / gas fields, planets or anomolies at each (quadrant, orbit).  For planets it will also detail their gravity rating.  This takes 10 TimeUnits.

= Probe Asteroid / Gas Field =

This will return a list of [Resources] available at the asteroid field.  Can only be done at the same location within the star system map.  This takes 10 TimeUnits.

= Investigate Anomoly =

This will interact with a StellarAnomoly triggering the event associated with it.  This takes 50 TimeUnits.
 
 
 