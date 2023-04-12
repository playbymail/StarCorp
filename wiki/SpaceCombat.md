#summary Space Combat Engine
#labels Phase-Design

= Introduction =

_Space Combat is not a definite feature and certainly won't be for 1.0 release. These are some preliminary ideas however_

The Space Combat Engine handles combat between two star ships. 

= New Starship Hulls =

== Weapons ==

Weapons are fired during combat to cause damage to the opposing ship. Weapons have several attributes:

  * Type: either mass, energy or radiation.
  * Accuracy: the accuracy rating is base chance to hit the target with the weapon.
  * Rate Of Fire: The number of combat rounds before each time the weapon can be fired.
  * Damage: The amount of damage done on a successful hit.
  * Special Damage Type: may be one of the following:
    * Kill Crew
    * Disable Impulse
    * Disable Warp
    * Disable Shields
    * Disable Weapons
  * Special Damage Chance: chance the weapon's special damage occurs upon a successful hit.
  * Ammo Type: if set the ship will need to consume 1 of the type specified (held in the ship's cargo) each time it fires.  If none available, the weapon will not fire.

== Shields / Armour ==

Shields and armour add to a ship's shield factor in each of the weapon types (mass, energy or radiation).  Shield attributes:
  * Type: either mass, energy or radiation
  * Total: total value of the shield / armour.
  * Restore Rate: the amount of the value restored to the ship's shield factor each round of combat.

== Countermeasures ==

Countermeasures make certain types of weapons less effective against the ship using them.  Countermeasures have the following attributes:

  * Weapon Type: the type of weapon they counter.
  * Effectiveness: the chance of countering a successful hit from weapons of this type.

== Marine Barracks ==

Marine barracks on ships allow a ship to board opposing ships.

== Repair ==

Repair hulls allow a ship to repair damage or remove special flags placed upon them.  Repair hulls have the following attributes:
  * Effectiveness: The number of MUs of damage they repair each usage.
  * Special: Any flags they remove from the starship.

= New Facility Types =

== Repairyard ==

= Starship Changes =

== Hull Damage ==

Each starship will track the total damage taken by them from combat.  Damage can be repaired but if it equals or exceeds the ships total hull mass, the ship is destroyed.

== Shield Factor ==

Each starship has a shield factor which is the amount of damage is absorbed from weapons.  

Shield Factor is split into three categories (similar to weapons):
  * Energy
  * Mass
  * Radiation

Shield factor is equal to the total shield factors of all suitable hulls.  It is reset to full each update, unless the ship's shields have been disabled.

== Starship Flags ==

Starships will need flags to denote special conditions placed upon them by combat.  The default is NONE (denoting no special circumstances).  Other valid flags:

  * IMPULSE_IMMOBILIZED - cannot move, orbit, leave orbit or dock.
  * WARP_IMMOBILIZED - cannot jump
  * WEAPONS_DISABLED - cannot fire weapons
  * SHIELDS_DISABLED - cannot regerenate shields
  * CREW_MINIMAL - all TU costing actions take twice as long
  * CREW_NONE - cannot perform any actions

= Starship Actions =

== Repair Ship Hulls ==

If a repair hull is fitted, the ship will attempt to repair itself or a ship in the same location.  TU Cost: 20.

== Resupply Crew ==

The ship will resupply a ship with its own crew, removing the CREW_NONE flag but giving both ships the CREW_MINIMAL flag.

== Board Ship ==

If a ship has a marine barracks hull will board a ship with a CREW_NONE flag, Boarded ships take on new owners but remain in the same state (ie. damage and flags remain).

== Destroy Derelict ==

= Starship Combat Preferences =

Each starship has a number of preferences which determines whether it will initiate combat and what actions it will perform during combat:

  * Attack: Max Hull Size
  * Attack: Max Impulse Speed
  * Target: Hull type (command, engine, cargo, any)
  * Flee: Percentage of damage before attempting to flee combat

= Trigger =

Combat is triggered when a ship scans another ship on its hate list (see CorporateDiplomacy) that is not excluded by virtue of its combat preferences.

= Combat Rounds =

Combat is fought in rounds with each round the ship with the lowest (best) impulse speed going first.  

== Fire Weapons ==

A weapon is available if its rate of fire determines it is and the ship doesn't have a WEAPONS_DISABLED flag.  If the weapon uses ammo 1 of the specified type must be in the ship's cargo.

The chance to hit with the weapon is:

  * Accuracy - (100 / Impulse Speed) %

If the weapon hits:
  * Ammo of the type used is removed from cargo.
  * Any suitable countermeasures are tested to see if they nullify the hit.
  * Normal damage is applied of the weapon's type to:
    * The Shield Factor if it is greater than the weapon's damage
    * Any remaining damage is applied to the ship's hulls
  * Special damage effects (if any) are checked for.

== Restore Shields ==

Shields attempt to restore shield factors based on their Restore Rating.

== Flee ==

The chance a ship will attempt to flee is equal to:

  * (Hull Damage / Total Hull Mass) x 100% vs Flee Comat Preference

If the ship is not IMPULSE_IMMOBILISED it will attempt to flee.  The chance of successfully fleeing combat:
  * (Fleeing Ship's Impulse Speed / Pursuing Ship's Impulse Speed) x 100%
