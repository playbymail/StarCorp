#summary Non-Player Corporation (NPC) Prcessor
#labels Phase-Design

= Introduction =

The NPC Processor brings to life NPCs by making them do some actions with their various entities.  Initially, this will not be very intelligent but later it should be possibly to put some decision making into the process or even AI.

At this point NPCs are assumed not to have starships.

= Factories =

If their production queue is empty NPC factories will have items they can produce queued.

For light and heavy factories the exact items queued will be based on the available resources at the colony.  

Super-heavy factories have a building modules of each type added to their queue as well.

Shipyards have all the types of starship hulls added to their queue.

If a light or heavy factory has no suitable items in their queue, it will be shut down to save power.

= Markets =

NPCs will sell any consumer goods, building modules or starship hulls in their inventory on the market at 10% higher than the average price available (or a preset value if none are available).

They will also sell any resources and industrial goods in excess of 100 on the market.

They will attempt buy items listed on the colony market to meet any raw materials requirements of items queued in their factories.