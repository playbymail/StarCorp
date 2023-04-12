#summary Population Needs and Happiness
#labels Phase-Design

= Introduction =

Colonists have needs which must be met out of their salary which determines how happy they are.

= Colonist Income =

The income available to each colonist is equal to sum of any ColonistGrants available and their salary from working at a facility.

= Consumer Needs =

Each colonist will attempt to buy from the ColonyMarkets 1 unit of each of the following types of TradeGoods at a quality determined by their PopulationClass.  

ConsumerGoods bought in order of priority:
  * Food - 10% happiness
  * Drink - 10% happiness
  * Intoxicant - 15% happiness
  * Clothes - 15% happiness

Preferred quality by PopulationClass:
  * Luxury: [Scientists], [Technicians]
  * Basic: [Administrators], [Security]
  * Bargain: [Labourers], [CSR]

= Service Needs =

Each colonist will attempt to buy from ServiceFacilities 1 usage of each of the following types of services at a quality determined by their PopulationClass.  

ServiceFacilities types in order of priority:
  * Medical - 10% happiness
  * Fitness - 10% happiness
  * Entertainment - 15% happiness
  * Education - 15% happiness

Preferred quality by PopulationClass:
  * Cutting Edge: [Scientists], [Technicians]
  * Advanced: [Administrators], [CSR]
  * Basic:  [Security], [Labourers]

= Happiness =

Happiness is calculated per population class (employed and unemployed) and then averaged to determine the happiness level per class for the colony.  Happiness cannot exceed 100%.
 
 
 
 
 