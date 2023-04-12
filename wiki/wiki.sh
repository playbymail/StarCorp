#!/bin/bash

rm -f wiki.md
echo "# StarCorp Wiki" >> wiki.md

for file in \
	GalacticDate.md		\
	Items.md		\
	Planets.md		\
	ColonyMarkets.md		\
	InterplanetaryMovement.md		\
	StarshipDesign.md		\
	AtmosphereType.md		\
	Roadmap.md		\
	StellarAnomoly.md		\
	Population.md		\
	ClientApplication.md		\
	PopulationMigration.md		\
	SpaceMining.md		\
	FacilityTypes.md		\
	Starships.md		\
	Screenshots.md		\
	EntityPersistence.md		\
	StarSystem.md		\
	Orders.md		\
	BuildingModules.md		\
	Install.md		\
	Government.md		\
	Employment.md		\
	Corporations.md		\
	Build.md		\
	ResourceGenerators.md		\
	ColonyHub.md		\
	PopulationClass.md		\
	PlanetTemplate.md		\
	Factories.md		\
	ConsumerGoods.md		\
	Credits.md		\
	ServiceFacilities.md		\
	MassUnits.md		\
	BirthAndDeath.md		\
	Release1_0_0_0.md		\
	TradeGoods.md		\
	Facilities.md		\
	TurnProcessor.md		\
	Colonies.md		\
	SpaceCombat.md		\
	GasField.md		\
	ColonistGrants.md		\
	NonPlayerCorporationProcessor.md		\
	Docking.md		\
	TimeUnits.md		\
	Orbiting.md		\
	AsteroidField.md		\
	Resources.md		\
	SystemTemplate.md		\
	HazardLevel.md		\
	Release0_9_0_0.md		\
	GravityRating.md		\
	PlayersGuide.md		\
	Technicians.md		\
	ServerShell.md		\
	Terrain.md		\
	InterstellarMovement.md		\
	Release0_9_1_0.md		\
	ColonyTemplate.md		\
	DevelopmentGrants.md		\
	OrbitalDock.md		\
	CorporateDiplomacy.md		\
	IndustrialGoods.md		\
	NeedsAndHappiness.md		\
	PlanetaryExploration.md		\
	StarshipHulls.md		\
	StarSystemExploration.md		\
	PopulationProcessor.md		\
	FacilityProcessor.md		\
	Laws.md		\
	TurnFetcher.md		\
	FacilityLeases.md		; do

	echo "" >> wiki.md
	echo "" >> wiki.md
	echo "## ${file%.md}" >> wiki.md

	sed 's/^#/###/' "${file}" >> wiki.md
done
