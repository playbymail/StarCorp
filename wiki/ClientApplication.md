#summary Client Application
#labels Phase-Requirements,Phase-Design

= Introduction =

The client is a Java / SWT application which performs the following functions:

  * Load turn report
  * View turn report
  * Search colony markets
  * Search available leases and grants
  * Design starships
  * Prepare turn orders
  * Submit turn orders

= Main Window =

The main app window consists of a two column panes: a tree explorer (LHS) for navigating through the turn report and a data panel (RHS) for viewing details of entities within the report.

== Toolbar ==

The app window has a toolbar with the following buttons:
  * Load turn report
  * New turn
  * Search items
  * Search markets
  * Search leases and grants
  * Design Starship

== Menu ==

The app window has a menu for quickly accessing certain functions (keyboard shortcuts in brackets):
  * File
    * Load turn report (F1)
    * Save current turn (F2)
    * Exit (F12)
  * Turn
    * Prepare (F3)
    * Submit (F4)
  * Search
    * Search market (F6)
    * Search items (F7)
    * Search leases and grants (F8)
  * Help
    * About
    

== Tree Browser ==

The tree browser is structured like the turn report:

  * Corporation - loads player's corporation data pane
  * Turn Report 
    * Orders - each sub-node links to a report for a specific order committed in the last turn.
  * Entities
    * Ships - sub-nodes link to ship data pane for that starship
    * Facilities - sub-nodes link to facility data pane for that facilities

== Entity Data Pane ==

The entity data pane changes the displayed information based on what has been selected in the tree browser or via a hyperlink in an order report.

  * Order Report - displays the order given, the text message of success / failure and a list of any scanned entities, each hyperlinked to reload the data pane.
  * Generic Entity - displays specified fields and values for that entity (e.g. ID, name, location etc.)
  * Planet - planets will display data about the planet and a grid map of the planet map (if available)

== Load turn report ==

Opens a file browser (initially set to current working directory / reports) to pick up the report file which is then loaded and parsed. 

= Turn Order Window =

The turn order window has two horizontal components:
  * Order Builder
  * Current Orders

== Order Builder ==

Consists of a drop down of the orders available, plus fields for entering values for the arguments.

The order fields will be text inputs or drop downs of suitable entities.

== Current Orders ==

This is an editable table of current orders with options to edit fields or delete rows.

= Search Colony Markets Window =

This consists of several drop downs to filter a table of matching market orders.  Filtering by:
  * Item type (drop-down)
  * Colony (drop-down)
  * Seller (drop-down)
  * Item category (e.g. Resource, Industrial Goods etc.) - filters item types too (drop-down)
  * Quantity (text input)
  * Price (text input)

= Search Colony Items Window =

This consists of several drop downs to filter a table of matching colony items.  Filtering by:
  * Item type (drop-down)
  * Colony (drop-down)
  * Item category (e.g. Resource, Industrial Goods etc.) - filters item types too (drop-down)
  * Quantity (text input)

= Search Leases and Grants Window =

This consists of several drop downs to filter a table of matching leases and grants.  Filtering by:
  * Facility type (drop-down)
  * Colony (drop-down)
  * Price / Value (text input)
  * Include Leases (checkbox)
  * Include Grants (checkbox)



 
 
 