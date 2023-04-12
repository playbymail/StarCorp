#summary Project Release 1.0.0.0 features / plan
#labels Phase-Requirements

= Overview =

The main objective of this release is to finalize the features of [Release0_9_0_0] and fix reported bugs.

= Outstanding Tasks =

== Common ==
  * Recent cash transactions for player corporation.
  * Order: Repeal Law
  * Order: Clear Factory Queue
  * Order: Close Facility
  * Order: Scrap Item
  * Order: Scrap Ship
  * Order: Scrap Facility

== Client ==
  * Tree browser context menus:
    * Redo order for order reports
    * Build ship for designs
    * Ship orders for ships
    * Set salary, shutdown for facilities
    * Build for factories
  * Colony / Planet / Star System Entity pane should have a "Move Ship X to this location"
  * Move all displayed text to configuration file
  * Sorting by column for Search Items, Laws and Markets.
  * Search Items: Pickup items with starship

== Server ==
  * Configurable player setups
  * Configurable name generation (server.Util class)
  * Events and handling events for investigation order
  * Configure hibernate to use an object cache