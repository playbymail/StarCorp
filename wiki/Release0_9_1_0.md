#summary Release 0.9.1.0 features / plan
#labels Phase-Requirements,Phase-Support

= Changelog from 0.9.0.0 =

== Common ==
  * Added version checking for turn submissions
  * Removed unnecessary printing to console of system properties when sending an email
  * Fixed order report scanned entities and client display
  * Fixed turn report method for determining ships at a specified colony or in the orbit of the same planet.
  * Added Buy Lease order
== Client ==
  * Added toolbar to search windows for paging through results.
  * Added application specific icon and splash screen
  * Added capability to send emails to other players from Corporation pane.
  * Fixed issue with items not showing in Corporation / Colony data panes if there was already an item of same type listed.
  * Added keyboard accelerators to menu
  * Search Items: Fixed display of items to filter out zero quantity items
  * Fixed issue with pressing cancel when prompted to save turn when closing window didn't result in closing being cancelled.
  * Added delete order from turn order window
  * Added sell item from search items window
  * Added buy item from search market window
  * Added buy lease from search laws window
  * Added filtering by licensee to search laws window
  * Added deliver item from starship pane to colony (if docked)
  * Order report: 
    * Fixed display issues with summary not shown and removed unnecessary label. 
    * Increased columns of list of scanned entities to 4
  * Planet maps: Fixed image loading to use class resource rather than direct file system so images in jar file are properly loaded
  * Order builder: Fixed missing starship field for pickup / deliver items
  * Government data pane: View unemployed for colony.
== Server ==
  * NonPlayerCorporationProcessor
  * Prospect order: Filter prospected resources by labs available in starship
  * Scan Galaxy order: Fixed row updated problem on corporation when adding system to known systems.
  * Deliver order: Changed target of order report to colony
  * Pickup order: 
    * Added error checking if a valid ship / colony not specified
    * Changed target of order report to colony
  * Corporate / Ship Buy order: Fixed issues where items weren't being bought.
  * Fixed corporate sell to pick right colony item to remove items from.
  * Import / export shell utility
  * Automatically close generators which cannot produce any resources as no suitable deposits available.
  * Turnprocessor: 
    * Fixed problem with authorisation of turns. 
    * Fixed class casting problem with authorization.  
    * Fixed problem with process method in TurnProcessor not loading the corporation
    * Fixed list of items in turn report to return only items with quantity of 1 or more.
  * Modified colony setup to inject leases and additional market items as well as more sensible types of resource generators.