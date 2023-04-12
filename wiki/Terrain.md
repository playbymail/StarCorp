#summary Planetary Terrain
#labels Phase-Design

= Introduction =

Each planetary map square has a terrain which besides being a descriptive tag for the location determines the HazardLevel of any colony built at that location (in addition to the AtmosphereType of the planet).

= Types =

|| *Key* || *Name* || *Hazard Level* ||
|| desert || Desert || 0.08 || oil 50% || fissile 15% || rare-minerals 25% || hydrocarbon 50% || precious 25% || salt 100% ||
|| forest || Forest || 0.02 || carbonite 20% || funghi 50% || water 75% || fruit 75% || herb 50% || vegatables 100% ||
|| grass || Grassland || 0.01 || grains 60% || funghi 20% || livestock 60% || water 25% || fruit 20% || vegatables 40% || herb 30% ||
|| hill || Hill || 0.02 || iron 15% || silicon 10% || carbonite 25% || precious 5% || coal 15% || copper 15% || aluminium 10% || water 25% ||
|| ice || Ice || 0.05 || seafood 50% || oil 25% || rare-minerals 10% || hydrocarbon 25% || salt 100% || water 100% ||
|| jungle || Jungle || 0.05 || funghi 100% || water 100% || fruit 100% || vegatables 100% || herb 100% ||
|| marsh || Marsh || 0.05 || silicon 10% || oil 25% || noblegas 10% || hydrocarbon 25% || water 100% || herb 50% ||
|| mountain || Mountain || 0.05 || iron 25% || carbonite 15% || fissile 10% || precious 10% || copper 25% || water 50% || aluminium 15% ||
|| plain || Plain || 0.01 || grains 60% || funghi 20% || livestock 50% || water 25% || fruit 25% || vegatables 60% ||
|| swamp || Swamp || 0.05 || silicon 10% || oil 25% || noblegas 10% || hydrocarbon 25% || water 100% || herb 50% ||
|| tundra || Tundra || 0.02 || funghi 25% || livestock 50% || copper 10% || fruit 20% || herb 25% ||
|| volcanic || Volcanic || 0.08 || iron 40% || fissile 10% || rare-minerals 10% || hydrocarbon 25% || precious 15% || copper 15% || aluminium 10% ||
|| water || Water || 0.05 || seafood 50% || oil 25% || rare-minerals 10% || hydrocarbon 25% || salt 100% || water 100% ||