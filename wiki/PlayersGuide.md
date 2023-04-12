#summary Player's Guide
#labels Phase-Support,Featured

= Introduction =

Star Corp is a play-by-email game set in space where you control a corporation with free reign to explore and exploit the resources of countless worlds.

[Screenshots]

= Getting Started =

To start playing [http://starcorp.googlecode.com/files/StarCorp%20Client-Installer%200.9.1.0.exe download the client installer].  Once [Install installed], choose File -> New Account from the menu.  You will be [http://starcorp.googlecode.com/svn/trunk/screenshots/credentials_dialog.png prompted] for information about your new setup:

  * Corporation - give your corporation a suitable name
  * Name - enter the name you want other players to know you by
  * Email - enter a valid email address to receive turn reports.  This email is also displayed to other players so they can contact you.
  * Password - enter a memorable password for your account.

Once the account details are entered you will be [http://starcorp.googlecode.com/svn/trunk/screenshots/email_configration.png prompted] one time for details about your email server for sending emails:

  * Host - hostname for your SMTP server. e.g. smtp.gmail.com
  * Username - your email account username.
  * Password - your email account password.
  * Port - the port of your SMT server. e.g. 21

Once your email account details are configured, an email will be sent to the Star Corp server and you will shortly receive your first turn report.

= Your First Turn Report =

Once you have received your first turn report, save it to a convenient location and open your Star Corp client.  The turn report will come in compressed zip format but there is no need to unarchive it as the client will happily read the file.  

Choose the "Open Report" button on the client or go File -> Load Report in the menu, navigate to where you saved the turn report and select open.

You will see a navigation tree on the left side of the client with details of your corporation, any starships and facilities you started with.  Take some time to review your initial position.

= Navigation =

You can select any point in the navigation tree for more detailed information which will be displayed on the right hand side.  You can also use the arrow buttons on the toolbar to return to a previously shown data window.

= Issuing Orders =

The [http://starcorp.googlecode.com/svn/trunk/screenshots/turn_window1.png Turn Order window] is where you can issue orders to your starships and facilities.  It can be accessed either via Turn -> Prepare on the menu, the New Turn button on the toolbar or via links in the data window from your controlled ships, facilities or governments.

The top of the Turn Order window displays a form for adding new orders to your current turn.  First you choose the order to be given, then it will allow you to pick suitable targets and enter other values pertaining to the order.  Once prepared press the "Add" button to add the order to the current list.

The bottom of the Turn Order window displays a table of prepared orders.  Any field here can be directly edited if needed.

Once your orders are ready you can press "Submit" to send your orders to the Star Corp server for processing.

= Your First Orders =

You will begin with at least one starship which can be used to explore your starting system.  Navigate to it in the [http://starcorp.googlecode.com/svn/trunk/screenshots/turn_report3.png main application window] using the navigation tree.  You will find it under Starships (as well as information about the [http://starcorp.googlecode.com/svn/trunk/screenshots/turn_report2.png ship's design]).

It will begin docked at a colony and so will first need to take off.  In the Starship's data pane there is a link to Take Off which will add a take off order to your current orders.  

Once the Turn Order window is open use the drop down to add a Probe Planet for your starship order to scan the currently orbiting planet.  

Then you should leave orbit and perform a Scan System order to obtain information about other planets, asteroids and gas fields in the current star system.  Finally, a Scan Galaxy system will report any nearby star systems and add their location to your databanks so your ships will be able to jump to them and you will receive market reports from them.

Once these simple orders have been issued, you should submit your turn and wait for your turn report.  

Remember, you can submit any number of turns in between game updates but your starships have a limited amount of time and most actions take some time to perform.

= Future Turns =

Star Corp is an open ended game and there are numerous possibilities and ways to play.  Its probably a good idea to set yourself some goals and work towards them.

== Exploration ==

Your starting ship is well suited to explore your starting and nearby star systems.  Exploration typically consists of:

  * *Scan System* for planets / asteroids / gas fields in the current system.
  * *Move* to the location of the planet / asteroid / gas field
  * *Probe System* on asteroids / gas fields to determine what resource deposits they contain.
  * *Orbit* a planet and then *Probe Planet* to find out more about the planet as well as get the planet's map.
  * *Dock* at different locations on the planet and *Prospect* to determine what resource deposits they contain.
  * *Scan Galaxy* for nearby star systems and *Jump* to them and repeat the process in an all new star system.

== Trading ==

Every turn you will be given a list of all items on sale in colony markets of systems you have knowledge of.  You can use the [http://starcorp.googlecode.com/svn/trunk/screenshots/search_market1.png Search Market window] to find specific items at specific locations.

Your corporation can buy items at any colony it knows about at any time.  Buying items from the market incurs a service charge per transaction from the government of the colony. Bought items are stored in a private warehouse until used, sold or shipped.  You can use the [http://starcorp.googlecode.com/svn/trunk/screenshots/search_items1.png Search Items window] to find items in your corporation's warehouses.

Your starships can carry a certain amount of items based on what cargo hulls they have fitted.  You can either pickup / deliver items directly from your own warehouses on each colony or interact directly with the market using your starship.  Additionally, if a colony has an [OrbitalDock Orbital Dock] you may trade with the colony (buy, sell, pickup or deliver) from orbit without having to land at the colony.

There are many trading strategies but they typically come down to buying something cheaper at one colony and shipping it to another where it can be sold at a higher price.  Finding and  setting up trade routes can be a challenging and rewarding experience.

Some advice on the various [Items item types] and typical demand:

  * [Resources] - extracted from planet's by farms, mines etc and by starships from asteroids / gas fields they are used in production by [Factories factories].  There is a lot of variation on what will be available on each [Planets planet] or [AsteroidField asteroid]/[GasField gas field].  All resources are used to make something else.  Trading resources typically involves buying them from far flung small colonies and shipping them to larger colonies with factories to turn them into useful goods.
  * [IndustrailGoods Industrial Goods] - built at heavy factories from resources these are used in further production.  Typically, smaller and medium colony's will produce these goods from available resources and sell them on their markets as they don't have the necessary larger factories or other components to make other items from them.  Buying them from these markets and shipping them to larger colonies is a viable money earner.
  * [ConsumerGoods Consumer Goods] - built at light factories they are an always in demand at colonies everywhere.  There are 4 categories and 3 quality levels of different consumer goods and each colonist at a colony will want to buy at least 1 of each category each game update.  If a colony's market doesn't have them or they can't afford to buy them, they will become unhappy. So supplying colony markets with consumer goods should keep any trader busy indefinitely as colonies will not necessarily be able to produce consumer goods locally.
  * [BuildingModules Building Modules] / [StarshipHulls Starship Hulls] - these are only useful to other players and require larger cargo hulls to carry.  They should be considered an advanced trading option once there you have established relationships with other corporations.

== Building new Starships ==

Soon you will want a second starship to expand your options of play.  The first step in building a starship is to design it.  Use the [http://starcorp.googlecode.com/svn/trunk/screenshots/design_ship1.png Design Ship window] (accessed from the main window's toolbar) to add hulls to a design in progress.  

Each hull adds certain capabilities to your starship design but will increase its overall mass and thus _increase_ its impulse speed (an increase in impulse speed means [# InterplanetaryMovement movement] will take longer and the levels of gravity the ship can handle are lower).  

Remember, each hull will have to be built or bought so tailoring the types of hulls in your design to those hulls you have or can get access to is a must.  For example, adding Warp V to your design when there are none available anywhere would make your design fairly useless (at least until you acquire Warp V hulls).

All ships need 1 command deck and 1 crew deck for every none command / crew deck.  The starship designer will automatically add crew decks to your design as you add other hulls.  Some form of engines for thrust and [InterstellarMovement jump movement] is necessary.  The type and quantity is up to you but monitor the design window to see what your impulse speed / jump rating is and what planet gravity's your design can handle.

It also makes sense to make your designs fit for a purpose:
  * A ship designed to trade should be fitted with suitable cargo hulls for the type of items you wish to trade.  
  * A ship designed for exploration should be fitted with scanners, probes and labs to allow it to Scan systems, probe planets / asteroids / gas fields and prospect a planet's surface.
  * A mining ship should be fitted with a Mining Platform (for mining asteroids) and / or a Gas Field Collector (for mining gas fields) and suitable cargo hulls for storing the mined resources.

When your design meets your requirements, give the design a name and press the Issue Design Order to have the design added to your current turns.  You will need to submit your turn and receive a successful report before you can use the design to build new ships using the design.

Once you've designed your ship you need to buy (or build) the appropriate hulls, gather them in a single colony and then issue a build ship order.  This will assemble the hulls needed for your design and create your new starship.  

== Asteroid / Gas Field Mining ==

A ship fitted with a Mining Platform or Gas Field Collector can mine asteroids and gas fields respectively.  This is a 50 TU action that can be performed when your mining ship is at the same location as the asteroid or gas field.  To determine what resources an asteroid / gasfield will produce each time it is mined you should use a ship fitted with a Probe to scan them.

Mined resources are put in your cargo hulls and you will only be able to mine as much as you can carry.

== Building new Facilities ==

Owning a facility will allow you to do own of the following:
  * Farms, Mines, Gas Plants, Liquid Pumps, Refineries: Gather resources from deposits located at a colony.
  * Light, Heavy, Super-Heavy Factories and Shipyards: Build items from resources.
  * Orbital Dock: Allow starships to trade with the colony market or their own warehouses from orbit (for a service charge).
  * Other: Provide services for colonists at a colony for a fee.

The first step in building a facility is acquiring a lease from the colony government.  These are put on sale and available leases can be found using the [http://starcorp.googlecode.com/svn/trunk/screenshots/search_laws1.png Search Laws window]. Each lease allows 1 of the specified type of facility to be built at the colony.

Its also a good idea to see if the colony government has issued a development grant for that facility type.  You can find them in the [http://starcorp.googlecode.com/svn/trunk/screenshots/search_laws1.png Search Laws window] as well.  A grant means you will be paid for building the specified type of facility.  You still need a suitable lease but it can offset the cost of the lease or even depending on the government's generosity the overall cost of building the facility.

Once you have a suitable lease, you need to buy the necessary building modules for the facility type and gather it at the colony.  Then you can issue a Build Facility order to construct your new facility.

After you have constructed a facility you should:
  * Set the salary you wish to pay each type of worker needed at your facility every game update.
  * Set a service charge if appropriate for your facility for colonists or ships to pay.
  * Issue build orders for your factories.

A newly built facility will begin with no employees and will hire from the available unemployed pool at the colony.  Depending on the colony, it may take a few updates to bring the number of employees to full capacity.  Also be aware employees will quit if their salary doesn't meet their living costs and some will die (a particular problem in high hazard level colonies).  The only control you have over your employees is how much you pay them, where you built the facility and how well stocked the colony market is with affordable consumer goods.  A facility's efficiency will suffer if it is not fully staffed.  This can mean lower capacity at factories, less resources gathered or less colonists / starships serviced.

To operate a facility will need to be powered.  There are various power [Items items] (made from factories from resources) and your facility will have a power requirement based on its type.  Each game update the facility will try to pick power from your colony warehouse or buy directly from the market to meet its need.  Failing that, it will remain unpowered.  Unpowered facilities do not gather resources, produce items or service colonists / starships.

Some further advice:
  * Leases and grants are a way for colony governments to limit or encourage growth at their colonies to meet certain demands from their colonists.  
  * Always prospect the colony's location before building resource gathering facilities.  No point building a farm if there are no organics in that location.
  * Build factories with a view of what they might produce based on available items at the colony.  For example: Building a shipyard in a remote colony with no easy access to suitable materials to build starship hulls is probably a bad idea.  However, a colony with coal and iron would be a good place to build factories to produce steel alloy.
  * Facilities that service colonists are a great way to earn a steady income.
  * Resource gathering is a great way to enter the production chain.  They can be sold directly on the colony's market, shipped to a more profitable location or saved and used in further production.  Plan which facilities produce with a view of moving from gathering to making simple industrial goods from a couple of gathered resources to building further complex items.

== Founding new Colonies ==

Once you've mastered running a number of ships and facilities, it might be time to try your hand at founding and running a colony.  The first step is to find a suitable location by exploring the star systems you know about and the planets they contain.

Some things to consider when picking a site for a new colony:
  * Obviously, a colony cannot be built in the same location as an existing colony.
  * The atmosphere of the planet determines its hazard level (determined by a Planet Probe).  The hazard level is the base for the number of colonists that will die each game update at the colony.  It is highly recommended to colonise Green or ideally Microbiotic atmosphere planets.  The former means no deaths from the atmosphere whilst the latter actually encourages growth!  A Thin atmosphere may be liveable enough if the planet contains something worth a few colonist deaths but other [AtmosphereType atmosphere types] will pretty much ensure your colony never takes off.
  * The planet's gravity rating will limit the ships that can trade with it so be wary of high gravity ratings.  A gravity rating of 1-3g is usually acceptable.
  * Once you have a planet map, pick locations on the planet's surface with different terrain types and prospect them to see what resources they have.  Each square of the map may contain different deposits with different yields so its probably worth the effort to prospect most or all of a planet to find an ideal location.
  * The type of resources available will heavily influence the expansion opportunities of your colony. A broad range of different resources will mean more items can be produced locally whilst a good yield of a rarer resource will mean specialisation and profit!
  * Do consider the [Terrain terrain] your colony will be built upon.
  * Also consider the location of the new colony to nearby colonies which can be used to supply it.  Having a clear idea where modules, consumer goods, power and other necessary items will be shipped from is important in planning a new colony.
  * Building colonies is expensive and best done with plenty of cooperation with other corporations.

Once a suitable location has been picked its time to load a starship with necessary modules to build a colony hub and send it to the location where you wish build the new colony.  Once docked, it issues a Found Colony order and gives the new colony a name.  Congratulations, you have built a new colony!

As governor of the new colony you can:
  * Issue leases to build new facilities.  You can restrict new leases so they are bought by yourself immediately.
  * Issue development grants to encourage or subsidize the production of a new facility.
  * Issue colonist grants to subsidize colonists at your colony so they can meet their needs and have a high happiness rating.  

Some advice in running a colony:
  * Early on your colony will need a lot of hands on development and trading.  Do not undertake this lightly!  Its likely you will have to devote all your efforts to building your colony sacrificing other avenues of expansion such as trade or production.
  * Attracting colonists to your colony is the only way to ensure your facilities (or those you allow to be built by others) are well staffed.  Meeting their needs is paramount.  Make sure the colony market is well stocked with consumer goods and you have sufficient facilities to service the colonists.  
  * Ship enough power to supply your facilities or they will remain idle.  Putting some on the colony market so other corporations have a reserve to draw upon for their facilities will make your colony more attractive to run a facility at.
  * Be prepared to subsidise development of new facilities.  If your focus is in gathering resources, outsourcing the facilities needed to keep your colonists happy might make more sense than trying to build everything yourself.  
  * Be careful with colonist grants.  Whilst probably necessary to keep a pool of unemployed colonists happy until facilities are built or to cover unusually high prices for consumer goods or services, they are a drain on your funds which would be better met by paying suitably high salaries at your facilities.

= Finally =

  * Star Corp is a multiplayer game so do talk to other players.  Often, they can be a great source of advice and perhaps you can make deals for supply of hard to find items or for new ventures.  
  * Join the [http://groups.google.com/group/starcorp-players Players Group]!
  * Feedback and contributions (particularly documentation) is greatly appreciated.