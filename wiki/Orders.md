#summary List of order types
#labels Phase-Design,Featured

Order types found in version [Release0_9_1_0 0.9.1.0]

= Order Types =
|| *Key* || *Name* || *Arguments* ||
|| build || Factory Build || (Facility ID) (Item Type) (Quantity) ||
|| build-facility || Build Facility || (Colony ID) (Facility Type) ||
|| build-starship || Build Starship || (Colony ID) (Design ID) (Name) ||
|| buy-lease || Buy Lease || (Colony ID) (Law ID) ||
|| corp-buy || Buy (Corporation) || (Colony ID) (Item Type) (Quantity) ||
|| design-ship || Design Ship || (Name) (Hull Type) ... (Hull Type) ||
|| dock-colony || Dock At Colony || (Ship ID) (Colony ID) ||
|| dock-planet || Dock At Planet || (Ship ID) (X) (Y) ||
|| found || Found Colony || (Ship ID) (Hub Facility Type) (Name) ||
|| grant-colonist || Issue Colonist Grant || (Colony ID) (Population Class) (Credits) ||
|| grant-development || Issue Development Grant || (Colony ID) (Facility Type) (Credits) ||
|| investigate || Investigate Anomoly || (Ship ID) (Anomoly ID) ||
|| jettison || Jettison || (Ship ID) (Item Type) (Quantity) ||
|| jump || Jump || (Ship ID) (Star System ID) ||
|| lease || Issue Lease || (Colony ID) (Facility Type) (Price) (Licensee) ||
|| mine-asteroid || Mine Asteroid || (Ship ID) (Asteroid Field ID) ||
|| mine-gasfield || Mine Gas Field || (Ship ID) (Gas Field ID) ||
|| move || Move || (Ship ID) (Quadrant) (Orbit) ||
|| orbit || Orbit || (Ship ID) (Planet ID) ||
|| orbit-leave || Leave Orbit || (Ship ID) ||
|| probe-planet || Probe Planet || (Ship ID) (Planet ID) ||
|| probe-system || Probe Asteroid / Gas Field || (Ship ID) (Asteroid / Gas Field ID) ||
|| prospect || Prospect || (Ship ID) ||
|| salary || Set Facility Salary || (Facility ID) (Population Class) (Salary) ||
|| scan-galaxy || Scan Galaxy || (Ship ID) ||
|| scan-system || Scan Star System || (Ship ID) ||
|| sell-item || Sell (Corporation) || (Colony ID) (Item Type) (Quantity) (Price) ||
|| ship-buy || Buy (Starship) || (Ship ID) (Colony ID) (Item Type) (Quantity) ||
|| ship-deliver || Deliver Items || (Ship ID) (Colony ID) (Item Type) (Quantity) ||
|| ship-pickup || Pickup Items || (Ship ID) (Colony ID) (Item Type) (Quantity) ||
|| ship-sell || Sell (Starship) || (Ship ID) (Colony ID) (Item Type) (Quantity) (Price) ||
|| take-off || Take Off || (Ship ID) ||
 ||