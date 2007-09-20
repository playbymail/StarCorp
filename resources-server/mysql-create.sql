DROP TABLE IF EXISTS `starcorp`.`anomolies`;
CREATE TABLE  `starcorp`.`anomolies` (
  `ENTITY_ID` int(11) NOT NULL,
  `description` varchar(255) default NULL,
  `event` varchar(255) default NULL,
  PRIMARY KEY  (`ENTITY_ID`),
  KEY `FK80D223EF2A5DA840` (`ENTITY_ID`),
  CONSTRAINT `FK80D223EF2A5DA840` FOREIGN KEY (`ENTITY_ID`) REFERENCES `star_systems` (`ENTITY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `starcorp`.`asteroids`;
CREATE TABLE  `starcorp`.`asteroids` (
  `ENTITY_ID` int(11) NOT NULL,
  PRIMARY KEY  (`ENTITY_ID`),
  KEY `FK833C0B182A5DA840` (`ENTITY_ID`),
  CONSTRAINT `FK833C0B182A5DA840` FOREIGN KEY (`ENTITY_ID`) REFERENCES `star_systems` (`ENTITY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `starcorp`.`cargo`;
CREATE TABLE  `starcorp`.`cargo` (
  `ENTITY_ID` int(11) NOT NULL,
  `type` varchar(255) default NULL,
  `quantity` int(11) default NULL,
  KEY `FK5A0E7BCF20FB1A1` (`ENTITY_ID`),
  CONSTRAINT `FK5A0E7BCF20FB1A1` FOREIGN KEY (`ENTITY_ID`) REFERENCES `starships` (`ENTITY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `starcorp`.`cash_transactions`;
CREATE TABLE  `starcorp`.`cash_transactions` (
  `ENTITY_ID` int(11) NOT NULL,
  `amount` int(11) default NULL,
  `description` varchar(255) default NULL,
  `year` int(11) default NULL,
  `month` int(11) default NULL,
  KEY `FKC0B982E1B6A4EE63` (`ENTITY_ID`),
  CONSTRAINT `FKC0B982E1B6A4EE63` FOREIGN KEY (`ENTITY_ID`) REFERENCES `corporations` (`ENTITY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `starcorp`.`colonies`;
CREATE TABLE  `starcorp`.`colonies` (
  `ENTITY_ID` int(11) NOT NULL,
  `government` int(11) default NULL,
  `planet` int(11) default NULL,
  `x` int(11) default NULL,
  `y` int(11) default NULL,
  `hazardLevel` double default NULL,
  `foundedYear` int(11) default NULL,
  `foundedMonth` int(11) default NULL,
  PRIMARY KEY  (`ENTITY_ID`),
  KEY `FKDA833718F02E456E` (`ENTITY_ID`),
  KEY `FKDA833718800C654C` (`planet`),
  KEY `FKDA833718FF6B695D` (`government`),
  CONSTRAINT `FKDA833718FF6B695D` FOREIGN KEY (`government`) REFERENCES `corporations` (`ENTITY_ID`),
  CONSTRAINT `FKDA833718800C654C` FOREIGN KEY (`planet`) REFERENCES `planets` (`ENTITY_ID`),
  CONSTRAINT `FKDA833718F02E456E` FOREIGN KEY (`ENTITY_ID`) REFERENCES `named_entities` (`ENTITY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `starcorp`.`colonist_grants`;
CREATE TABLE  `starcorp`.`colonist_grants` (
  `ENTITY_ID` int(11) NOT NULL,
  `colony` int(11) default NULL,
  `popClassType` varchar(255) default NULL,
  `credits` int(11) default NULL,
  `available` bit(1) default NULL,
  `issuedYear` int(11) default NULL,
  `issuedMonth` int(11) default NULL,
  PRIMARY KEY  (`ENTITY_ID`),
  KEY `FKD523BCCB9DF99BB0` (`ENTITY_ID`),
  KEY `FKD523BCCB540CFC10` (`colony`),
  CONSTRAINT `FKD523BCCB540CFC10` FOREIGN KEY (`colony`) REFERENCES `colonies` (`ENTITY_ID`),
  CONSTRAINT `FKD523BCCB9DF99BB0` FOREIGN KEY (`ENTITY_ID`) REFERENCES `entities` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `starcorp`.`colonists`;
CREATE TABLE  `starcorp`.`colonists` (
  `ENTITY_ID` int(11) NOT NULL,
  `colony` int(11) default NULL,
  `quantity` int(11) default NULL,
  `popClassType` varchar(255) default NULL,
  `cash` int(11) default NULL,
  `happiness` double default NULL,
  PRIMARY KEY  (`ENTITY_ID`),
  KEY `FK75E3E1089DF99BB0` (`ENTITY_ID`),
  KEY `FK75E3E108540CFC10` (`colony`),
  CONSTRAINT `FK75E3E108540CFC10` FOREIGN KEY (`colony`) REFERENCES `colonies` (`ENTITY_ID`),
  CONSTRAINT `FK75E3E1089DF99BB0` FOREIGN KEY (`ENTITY_ID`) REFERENCES `entities` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `starcorp`.`colony_items`;
CREATE TABLE  `starcorp`.`colony_items` (
  `ENTITY_ID` int(11) NOT NULL,
  `owner` int(11) default NULL,
  `colony` int(11) default NULL,
  `type` varchar(255) default NULL,
  `quantity` int(11) default NULL,
  PRIMARY KEY  (`ENTITY_ID`),
  KEY `FK2D31E43BE91D021F` (`owner`),
  KEY `FK2D31E43B9DF99BB0` (`ENTITY_ID`),
  KEY `FK2D31E43B540CFC10` (`colony`),
  CONSTRAINT `FK2D31E43B540CFC10` FOREIGN KEY (`colony`) REFERENCES `colonies` (`ENTITY_ID`),
  CONSTRAINT `FK2D31E43B9DF99BB0` FOREIGN KEY (`ENTITY_ID`) REFERENCES `entities` (`ID`),
  CONSTRAINT `FK2D31E43BE91D021F` FOREIGN KEY (`owner`) REFERENCES `corporations` (`ENTITY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `starcorp`.`corporations`;
CREATE TABLE  `starcorp`.`corporations` (
  `ENTITY_ID` int(11) NOT NULL,
  `playerName` varchar(255) default NULL,
  `playerEmail` varchar(255) default NULL,
  `playerPassword` varchar(255) default NULL,
  `credits` int(11) default NULL,
  `foundedYear` int(11) default NULL,
  `foundedMonth` int(11) default NULL,
  `lastTurnYear` int(11) default NULL,
  `lastTurnMonth` int(11) default NULL,
  PRIMARY KEY  (`ENTITY_ID`),
  KEY `FK74C4616BF02E456E` (`ENTITY_ID`),
  CONSTRAINT `FK74C4616BF02E456E` FOREIGN KEY (`ENTITY_ID`) REFERENCES `named_entities` (`ENTITY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `starcorp`.`employed`;
CREATE TABLE  `starcorp`.`employed` (
  `ENTITY_ID` int(11) NOT NULL,
  `facility` int(11) default NULL,
  `salary` int(11) default NULL,
  PRIMARY KEY  (`ENTITY_ID`),
  KEY `FK4722E6ADB744862` (`facility`),
  KEY `FK4722E6AD108ED0BA` (`ENTITY_ID`),
  CONSTRAINT `FK4722E6AD108ED0BA` FOREIGN KEY (`ENTITY_ID`) REFERENCES `colonists` (`ENTITY_ID`),
  CONSTRAINT `FK4722E6ADB744862` FOREIGN KEY (`facility`) REFERENCES `facilities` (`ENTITY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `starcorp`.`entities`;
CREATE TABLE  `starcorp`.`entities` (
  `ID` int(11) NOT NULL auto_increment,
  `lastModified` datetime NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `starcorp`.`facilities`;
CREATE TABLE  `starcorp`.`facilities` (
  `ENTITY_ID` int(11) NOT NULL,
  `owner` int(11) default NULL,
  `colony` int(11) default NULL,
  `type` varchar(255) default NULL,
  `powered` bit(1) default NULL,
  `serviceCharge` int(11) default NULL,
  `transactionCount` int(11) default NULL,
  `open` bit(1) default NULL,
  `builtYear` int(11) default NULL,
  `builtMonth` int(11) default NULL,
  PRIMARY KEY  (`ENTITY_ID`),
  KEY `FK1FFD2281E91D021F` (`owner`),
  KEY `FK1FFD22819DF99BB0` (`ENTITY_ID`),
  KEY `FK1FFD2281540CFC10` (`colony`),
  CONSTRAINT `FK1FFD2281540CFC10` FOREIGN KEY (`colony`) REFERENCES `colonies` (`ENTITY_ID`),
  CONSTRAINT `FK1FFD22819DF99BB0` FOREIGN KEY (`ENTITY_ID`) REFERENCES `entities` (`ID`),
  CONSTRAINT `FK1FFD2281E91D021F` FOREIGN KEY (`owner`) REFERENCES `corporations` (`ENTITY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `starcorp`.`facility_created`;
CREATE TABLE  `starcorp`.`facility_created` (
  `ENTITY_ID` int(11) NOT NULL,
  `type` varchar(255) default NULL,
  `quantity` int(11) default NULL,
  KEY `FKF901414CC171B8B6` (`ENTITY_ID`),
  CONSTRAINT `FKF901414CC171B8B6` FOREIGN KEY (`ENTITY_ID`) REFERENCES `facilities` (`ENTITY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `starcorp`.`facility_leases`;
CREATE TABLE  `starcorp`.`facility_leases` (
  `ENTITY_ID` int(11) NOT NULL,
  `colony` int(11) default NULL,
  `type` varchar(255) default NULL,
  `licensee` int(11) default NULL,
  `price` int(11) default NULL,
  `used` bit(1) default NULL,
  `issuedYear` int(11) default NULL,
  `issuedMonth` int(11) default NULL,
  `usedYear` int(11) default NULL,
  `usedMonth` int(11) default NULL,
  PRIMARY KEY  (`ENTITY_ID`),
  KEY `FK7181DE759DF99BB0` (`ENTITY_ID`),
  KEY `FK7181DE7516E913B0` (`licensee`),
  KEY `FK7181DE75540CFC10` (`colony`),
  CONSTRAINT `FK7181DE75540CFC10` FOREIGN KEY (`colony`) REFERENCES `colonies` (`ENTITY_ID`),
  CONSTRAINT `FK7181DE7516E913B0` FOREIGN KEY (`licensee`) REFERENCES `corporations` (`ENTITY_ID`),
  CONSTRAINT `FK7181DE759DF99BB0` FOREIGN KEY (`ENTITY_ID`) REFERENCES `entities` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `starcorp`.`facility_queue`;
CREATE TABLE  `starcorp`.`facility_queue` (
  `ENTITY_ID` int(11) NOT NULL,
  `type` varchar(255) default NULL,
  `quantity` int(11) default NULL,
  KEY `FK3F72475C171B8B6` (`ENTITY_ID`),
  CONSTRAINT `FK3F72475C171B8B6` FOREIGN KEY (`ENTITY_ID`) REFERENCES `facilities` (`ENTITY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `starcorp`.`gas_fields`;
CREATE TABLE  `starcorp`.`gas_fields` (
  `ENTITY_ID` int(11) NOT NULL,
  PRIMARY KEY  (`ENTITY_ID`),
  KEY `FK771736DF2A5DA840` (`ENTITY_ID`),
  CONSTRAINT `FK771736DF2A5DA840` FOREIGN KEY (`ENTITY_ID`) REFERENCES `star_systems` (`ENTITY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `starcorp`.`market_items`;
CREATE TABLE  `starcorp`.`market_items` (
  `ENTITY_ID` int(11) NOT NULL,
  `seller` int(11) default NULL,
  `colony` int(11) default NULL,
  `type` varchar(255) default NULL,
  `quantity` int(11) default NULL,
  `costPerItem` int(11) default NULL,
  `issuedYear` int(11) default NULL,
  `issuedMonth` int(11) default NULL,
  `soldYear` int(11) default NULL,
  `soldMonth` int(11) default NULL,
  PRIMARY KEY  (`ENTITY_ID`),
  KEY `FKEDDC1FD9DF99BB0` (`ENTITY_ID`),
  KEY `FKEDDC1FDACC85EEB` (`seller`),
  KEY `FKEDDC1FD540CFC10` (`colony`),
  CONSTRAINT `FKEDDC1FD540CFC10` FOREIGN KEY (`colony`) REFERENCES `colonies` (`ENTITY_ID`),
  CONSTRAINT `FKEDDC1FD9DF99BB0` FOREIGN KEY (`ENTITY_ID`) REFERENCES `entities` (`ID`),
  CONSTRAINT `FKEDDC1FDACC85EEB` FOREIGN KEY (`seller`) REFERENCES `corporations` (`ENTITY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `starcorp`.`named_entities`;
CREATE TABLE  `starcorp`.`named_entities` (
  `ENTITY_ID` int(11) NOT NULL,
  `name` varchar(255) default NULL,
  PRIMARY KEY  (`ENTITY_ID`),
  KEY `FK6699B2079DF99BB0` (`ENTITY_ID`),
  CONSTRAINT `FK6699B2079DF99BB0` FOREIGN KEY (`ENTITY_ID`) REFERENCES `entities` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `starcorp`.`planet_map`;
CREATE TABLE  `starcorp`.`planet_map` (
  `ENTITY_ID` int(11) NOT NULL,
  `x` int(11) default NULL,
  `y` int(11) default NULL,
  `terrain` varchar(255) default NULL,
  KEY `FK3728BA358EA9D2AB` (`ENTITY_ID`),
  CONSTRAINT `FK3728BA358EA9D2AB` FOREIGN KEY (`ENTITY_ID`) REFERENCES `planets` (`ENTITY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `starcorp`.`planets`;
CREATE TABLE  `starcorp`.`planets` (
  `ENTITY_ID` int(11) NOT NULL,
  `orbiting` int(11) default NULL,
  `atmosphereType` varchar(255) default NULL,
  `gravityRating` int(11) default NULL,
  PRIMARY KEY  (`ENTITY_ID`),
  KEY `FKE28FC1DB12FAF0C` (`orbiting`),
  KEY `FKE28FC1DB2A5DA840` (`ENTITY_ID`),
  CONSTRAINT `FKE28FC1DB2A5DA840` FOREIGN KEY (`ENTITY_ID`) REFERENCES `star_systems` (`ENTITY_ID`),
  CONSTRAINT `FKE28FC1DB12FAF0C` FOREIGN KEY (`orbiting`) REFERENCES `planets` (`ENTITY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `starcorp`.`resource_deposits`;
CREATE TABLE  `starcorp`.`resource_deposits` (
  `ENTITY_ID` int(11) NOT NULL,
  `systemEntity` int(11) default NULL,
  `x` int(11) default NULL,
  `y` int(11) default NULL,
  `type` varchar(255) default NULL,
  `totalQuantity` int(11) default NULL,
  `yield` int(11) default NULL,
  PRIMARY KEY  (`ENTITY_ID`),
  KEY `FKD8F5DAA69DF99BB0` (`ENTITY_ID`),
  KEY `FKD8F5DAA64BE3801B` (`systemEntity`),
  CONSTRAINT `FKD8F5DAA64BE3801B` FOREIGN KEY (`systemEntity`) REFERENCES `star_systems` (`ENTITY_ID`),
  CONSTRAINT `FKD8F5DAA69DF99BB0` FOREIGN KEY (`ENTITY_ID`) REFERENCES `entities` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `starcorp`.`ship_design_hulls`;
CREATE TABLE  `starcorp`.`ship_design_hulls` (
  `ENTITY_ID` int(11) NOT NULL,
  `type` varchar(255) default NULL,
  `quantity` int(11) default NULL,
  KEY `FK8978CE28B71C37FF` (`ENTITY_ID`),
  CONSTRAINT `FK8978CE28B71C37FF` FOREIGN KEY (`ENTITY_ID`) REFERENCES `ship_designs` (`ENTITY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `starcorp`.`ship_designs`;
CREATE TABLE  `starcorp`.`ship_designs` (
  `ENTITY_ID` int(11) NOT NULL,
  `owner` int(11) default NULL,
  `designYear` int(11) default NULL,
  `designMonth` int(11) default NULL,
  PRIMARY KEY  (`ENTITY_ID`),
  KEY `FKEB23FB72E91D021F` (`owner`),
  KEY `FKEB23FB72F02E456E` (`ENTITY_ID`),
  CONSTRAINT `FKEB23FB72F02E456E` FOREIGN KEY (`ENTITY_ID`) REFERENCES `named_entities` (`ENTITY_ID`),
  CONSTRAINT `FKEB23FB72E91D021F` FOREIGN KEY (`owner`) REFERENCES `corporations` (`ENTITY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `starcorp`.`star_system`;
CREATE TABLE  `starcorp`.`star_system` (
  `ENTITY_ID` int(11) NOT NULL,
  `type` varchar(255) default NULL,
  `x` int(11) default NULL,
  `y` int(11) default NULL,
  `z` int(11) default NULL,
  PRIMARY KEY  (`ENTITY_ID`),
  KEY `FK70C408BCF02E456E` (`ENTITY_ID`),
  CONSTRAINT `FK70C408BCF02E456E` FOREIGN KEY (`ENTITY_ID`) REFERENCES `named_entities` (`ENTITY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `starcorp`.`star_systems`;
CREATE TABLE  `starcorp`.`star_systems` (
  `ENTITY_ID` int(11) NOT NULL,
  `system` int(11) default NULL,
  `quadrant` int(11) default NULL,
  `orbit` int(11) default NULL,
  PRIMARY KEY  (`ENTITY_ID`),
  KEY `FKA7BD0F37F5FF656C` (`system`),
  KEY `FKA7BD0F37F02E456E` (`ENTITY_ID`),
  CONSTRAINT `FKA7BD0F37F02E456E` FOREIGN KEY (`ENTITY_ID`) REFERENCES `named_entities` (`ENTITY_ID`),
  CONSTRAINT `FKA7BD0F37F5FF656C` FOREIGN KEY (`system`) REFERENCES `star_system` (`ENTITY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `starcorp`.`starships`;
CREATE TABLE  `starcorp`.`starships` (
  `ENTITY_ID` int(11) NOT NULL,
  `owner` int(11) default NULL,
  `planet` int(11) default NULL,
  `colony` int(11) default NULL,
  `design` int(11) default NULL,
  `x` int(11) default NULL,
  `y` int(11) default NULL,
  `builtYear` int(11) default NULL,
  `builtMonth` int(11) default NULL,
  `timeUnitsUsed` int(11) default NULL,
  PRIMARY KEY  (`ENTITY_ID`),
  KEY `FK81108AA5E91D021F` (`owner`),
  KEY `FK81108AA52A5DA840` (`ENTITY_ID`),
  KEY `FK81108AA593AA1AC6` (`design`),
  KEY `FK81108AA5540CFC10` (`colony`),
  KEY `FK81108AA5800C654C` (`planet`),
  CONSTRAINT `FK81108AA5800C654C` FOREIGN KEY (`planet`) REFERENCES `planets` (`ENTITY_ID`),
  CONSTRAINT `FK81108AA52A5DA840` FOREIGN KEY (`ENTITY_ID`) REFERENCES `star_systems` (`ENTITY_ID`),
  CONSTRAINT `FK81108AA5540CFC10` FOREIGN KEY (`colony`) REFERENCES `colonies` (`ENTITY_ID`),
  CONSTRAINT `FK81108AA593AA1AC6` FOREIGN KEY (`design`) REFERENCES `ship_designs` (`ENTITY_ID`),
  CONSTRAINT `FK81108AA5E91D021F` FOREIGN KEY (`owner`) REFERENCES `corporations` (`ENTITY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `starcorp`.`unemployed`;
CREATE TABLE  `starcorp`.`unemployed` (
  `ENTITY_ID` int(11) NOT NULL,
  PRIMARY KEY  (`ENTITY_ID`),
  KEY `FKA21B4C46108ED0BA` (`ENTITY_ID`),
  CONSTRAINT `FKA21B4C46108ED0BA` FOREIGN KEY (`ENTITY_ID`) REFERENCES `colonists` (`ENTITY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;