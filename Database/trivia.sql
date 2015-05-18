-- phpMyAdmin SQL Dump
-- version 4.4.6.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 14, 2015 at 04:49 PM
-- Server version: 5.6.20
-- PHP Version: 5.5.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `trivia`
--
CREATE DATABASE IF NOT EXISTS `trivia` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `trivia`;

-- --------------------------------------------------------

--
-- Table structure for table `question`
--
-- Creation: May 14, 2015 at 02:48 PM
--

CREATE TABLE IF NOT EXISTS `question` (
  `QuestionId` int(25) NOT NULL,
  `Question` varchar(255) DEFAULT NULL,
  `Difficulty` varchar(255) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;

--
-- RELATIONS FOR TABLE `question`:
--

--
-- Dumping data for table `question`
--

INSERT INTO `question` (`QuestionId`, `Question`, `Difficulty`) VALUES
(1, 'Wanneer begon de Eerste Wereldoorlog?', 'Easy'),
(2, 'Welk land won het WK voetbal 2014?', 'Easy'),
(3, 'Wat is de moderne naam van het vroegere “Caledonia”?', 'Hard'),
(4, 'Voor welk scheikundig element staat het symbool Cu?', 'Hard'),
(5, 'Wie staat er met zijn makkers in brons vereeuwigd buiten het kasteel van Nottingham in Engeland?', 'Hard'),
(6, 'In welk jaar kreeg Moeder Theresa de Nobelprijs voor de Vrede?', 'Hard'),
(7, 'Wie was de langst zittende premier van Nederland?', 'Easy'),
(8, 'Wat is de naam van het legendarische monster dat half mens, half stier was en door Theseus werd gedood?', 'Easy'),
(9, 'In welk jaar was de eerste Elfstedentocht?', 'Easy'),
(10, 'Waar is iemand die lijdt aan xenofobie bang voor?', 'Hard'),
(11, 'Welke Amerikaanse president besloot tot het inzetten van de atoombom tegen Japan?', 'Hard'),
(12, 'Wie bevrijdde Cuba in 1959 van de corrupte dictator Batista?', 'Hard'),
(13, 'Welk land is geen onderdeel van Scandinavië?', 'Easy'),
(14, 'Wat is de hoofdstad van Turkije?', 'Easy'),
(15, 'Welke kleur hebben de smurfen?', 'Easy'),
(16, 'In welk jaar stierf Elvis Presley?', 'Hard'),
(17, 'Hoeveel seconden zitten er in 2 uur?', 'Easy'),
(18, 'Wat is de hoofdstad van Frankrijk?', 'Easy'),
(19, 'Hoeveel chromosomen heeft een mens?', 'Hard'),
(20, 'Hoeveel bulten heeft een dromedaris?', 'Easy'),
(21, 'In welk jaar werd Willem van Oranje gedood?', 'Hard'),
(22, 'In welk jaartal won Feyenoord Rotterdam voor het laatst de landstitel?', 'Easy'),
(23, 'Hoeveel poten heeft een spin?', 'Easy'),
(24, 'Wat is de grootste planeet van ons zonnestelsel?', 'Easy'),
(25, 'Uit welk land komt de DJ Armin van Buuren?', 'Easy'),
(26, 'Hoeveel milligram is 1 kilogram?', 'Easy'),
(27, 'Welke datum is Valentijnsdag?', 'Easy'),
(28, 'Welke datum is de Tweede Kerstdag?', 'Easy'),
(29, 'Wat betekent de afkorting "GUI"?', 'Hard'),
(30, 'Wat is de voornaam van tekenfiguur Homer Simpson zijn zoon?', 'Easy'),
(31, 'Hoeveel minuten duurt een hele voetbal wedstrijd?', 'Easy'),
(32, 'Hoe heet de hoogste berg van Afrika?', 'Easy'),
(33, 'Hoeveel is 156+37?', 'Easy'),
(34, 'Waar staat de afkorting van supermarkt AH voor?', 'Easy'),
(35, 'Hoe groot is de Atlantische oceaan? ', 'Hard'),
(36, 'Wat is het diepste punt in de Atlantische Oceaan?', 'Hard'),
(37, 'Wat is het oppervlakte van Antarctica(de Zuidpool)?', 'Hard'),
(38, 'Wat is het Nederlandse alarmnummer?', 'Easy'),
(39, 'Met welk bestek eet men normaliter soep?', 'Easy'),
(40, 'Wat is de helderste stad op aarde vanuit de ruimte?', 'Easy'),
(41, 'Hoeveel teams bevat de Nederlandse Eredivisie Voetbal?', 'Hard'),
(42, 'In welke provincie bevindt Leeuwarden zich?', 'Easy'),
(43, 'De moeder van de moeder van jouw moeder is je...?', 'Easy'),
(44, 'Welke kleur krijg je als je blauw en geel bij elkaar voegt?', 'Easy'),
(45, 'Wat is het getal 12 in Romeinse cijfers?', 'Easy');

-- --------------------------------------------------------

--
-- Table structure for table `rightanswer`
--
-- Creation: May 14, 2015 at 02:48 PM
--

CREATE TABLE IF NOT EXISTS `rightanswer` (
  `RightAnswerId` int(25) NOT NULL DEFAULT '0',
  `RightAnswer` varchar(255) DEFAULT NULL,
  `QuestionId` int(25) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;

--
-- RELATIONS FOR TABLE `rightanswer`:
--   `QuestionId`
--       `question` -> `QuestionId`
--

--
-- Dumping data for table `rightanswer`
--

INSERT INTO `rightanswer` (`RightAnswerId`, `RightAnswer`, `QuestionId`) VALUES
(1, '1914', 1),
(2, 'Duitsland', 2),
(3, 'Schotland', 3),
(4, 'Koper', 4),
(5, 'Robin Hood', 5),
(6, '1979', 6),
(7, 'Ruud Lubbers', 7),
(8, 'Minotaurus', 8),
(9, '1909', 9),
(10, 'Vreemden, vreemdelingen', 10),
(11, 'Harry Truman', 11),
(12, 'Fidel Castro', 12),
(13, 'Polen', 13),
(14, 'Ankara', 14),
(15, 'Blauw', 15),
(16, '1977', 16),
(17, '7200', 17),
(18, 'Parijs', 18),
(19, '46', 19),
(20, 'één', 20),
(21, '1584', 21),
(22, '1999', 22),
(23, '8', 23),
(24, 'Jupiter', 24),
(25, 'Nederland', 25),
(26, '1.000.000', 26),
(27, '14 februari', 27),
(28, '26 december', 28),
(29, 'Graphical User Interface', 29),
(30, 'Bart', 30),
(31, '90 minuten', 31),
(32, 'Kilimanjaro', 32),
(33, '193', 33),
(34, 'Albert Heijn', 34),
(35, 'ongeveer 100.000.000 km² ', 35),
(36, '+- 8.648 meter', 36),
(37, '14.000.000 km²', 37),
(38, '112', 38),
(39, 'Lepel', 39),
(40, 'Las Vegas', 40),
(41, '18', 41),
(42, 'Friesland', 42),
(43, 'Overgrootmoeder', 43),
(44, 'Groen', 44),
(45, 'XII', 45);

-- --------------------------------------------------------

--
-- Table structure for table `wronganswer`
--
-- Creation: May 14, 2015 at 02:48 PM
--

CREATE TABLE IF NOT EXISTS `wronganswer` (
  `WrongAnswerId` int(25) NOT NULL DEFAULT '0',
  `WrongAnswer` varchar(255) DEFAULT NULL,
  `QuestionId` int(9) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- RELATIONS FOR TABLE `wronganswer`:
--   `QuestionId`
--       `question` -> `QuestionId`
--

--
-- Dumping data for table `wronganswer`
--

INSERT INTO `wronganswer` (`WrongAnswerId`, `WrongAnswer`, `QuestionId`) VALUES
(1, '1918', 1),
(1, 'Nederland', 2),
(1, 'Cola', 3),
(1, 'Calcium', 4),
(1, 'Batman', 5),
(1, '1900', 6),
(1, 'Mark Rutte', 7),
(1, 'Olifant', 8),
(1, '1939', 9),
(1, 'Spinnen', 10),
(1, 'George Walker Bush', 11),
(1, 'Carlos Manuel Piedra', 12),
(1, 'Finland', 13),
(1, 'Istanbul', 14),
(1, 'Geel', 15),
(1, '1984', 16),
(1, '3600', 17),
(1, 'Lyon', 18),
(1, '48', 19),
(1, 'twee', 20),
(1, '1678', 21),
(1, '2004', 22),
(1, '4', 23),
(1, 'Pluto', 24),
(1, 'Duitsland', 25),
(1, '1.000', 26),
(1, '14 januari', 27),
(1, '25 december', 28),
(1, 'Graphical User Injection', 29),
(1, 'Bas', 30),
(1, '60 minuten', 31),
(1, 'Kibo', 32),
(1, '183', 33),
(1, 'Appie Heijn', 34),
(1, 'ongeveer 100.000 km²', 35),
(1, '± 1.635 meter', 36),
(1, '1.400.000 km²', 37),
(1, '911', 38),
(1, 'Vork', 39),
(1, 'New York', 40),
(1, '20', 41),
(1, 'Groningen', 42),
(1, 'Grootmoeder', 43),
(1, 'Blauw', 44),
(1, 'IXX', 45),
(2, '1940', 1),
(2, 'Spanje', 2),
(2, 'Calcium', 3),
(2, 'IJzer', 4),
(2, 'Willem van Oranje', 5),
(2, '1868', 6),
(2, 'Willem ALexander', 7),
(2, 'Centaur', 8),
(2, '1967', 9),
(2, 'Oude mensen', 10),
(2, 'Barack Obama', 11),
(2, 'Fulgencio Batista', 12),
(2, 'Noorwegen', 13),
(2, 'Izmir', 14),
(2, 'Groen', 15),
(2, '1972', 16),
(2, '9000', 17),
(2, 'Bordeaux', 18),
(2, '140', 19),
(2, 'drie', 20),
(2, '1548', 21),
(2, '1995', 22),
(2, '6', 23),
(2, 'Aarde', 24),
(2, 'Zweden', 25),
(2, '1.000.000.000', 26),
(2, '14 maart', 27),
(2, '24 december', 28),
(2, 'Graphic Universal Interface', 29),
(2, 'Boris', 30),
(2, '120 minuten', 31),
(2, 'Mount Everest', 32),
(2, '188', 33),
(2, 'Albert Hoofd', 34),
(2, 'ongeveer 1.000.000 km² ', 35),
(2, '± 583 meter', 36),
(2, '140.000 km²', 37),
(2, '122', 38),
(2, 'Mes', 39),
(2, 'Singapore', 40),
(2, '22', 41),
(2, 'Gelderland', 42),
(2, 'Overgrootoma', 43),
(2, 'Paars', 44),
(2, 'DII', 45),
(3, '1814', 1),
(3, 'Brazilië', 2),
(3, 'Europa', 3),
(3, 'Zuurstof', 4),
(3, 'Geen van deze antwoorden', 5),
(3, '1970', 6),
(3, 'Jan Peter Balkenende', 7),
(3, 'Sfinx', 8),
(3, '1819', 9),
(3, 'Kinderen', 10),
(3, 'John Fitzgerald Kennedy', 11),
(3, 'Osvaldo Dorticós Torrado', 12),
(3, 'Zweden', 13),
(3, 'Bursa', 14),
(3, 'Rood', 15),
(3, '1968', 16),
(3, '6800', 17),
(3, 'Marseille', 18),
(3, '32', 19),
(3, 'geen', 20),
(3, '1708', 21),
(3, '2012', 22),
(3, '10', 23),
(3, 'Mars', 24),
(3, 'Engeland', 25),
(3, '100.000', 26),
(3, '14 april', 27),
(3, '1 januari', 28),
(3, 'Graphic Unit Injection', 29),
(3, 'Brian', 30),
(3, '80 minuten', 31),
(3, 'Mons Huygens', 32),
(3, '203', 33),
(3, 'Albert Hobby', 34),
(3, 'ongeveer 1.000.000.000.000 km²', 35),
(3, '+- 28.862 meter', 36),
(3, '140.000.000 km²', 37),
(3, '211', 38),
(3, 'Geen bestek', 39),
(3, 'Dubai', 40),
(3, '16', 41),
(3, 'Drenthe', 42),
(3, 'Overovergrootmoeder', 43),
(3, 'Oranje', 44),
(3, 'MII', 45);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `question`
--
ALTER TABLE `question`
  ADD PRIMARY KEY (`QuestionId`),
  ADD UNIQUE KEY `Question` (`Question`);

--
-- Indexes for table `rightanswer`
--
ALTER TABLE `rightanswer`
  ADD PRIMARY KEY (`RightAnswerId`,`QuestionId`),
  ADD KEY `question_rightanswer_fk_idx` (`QuestionId`);

--
-- Indexes for table `wronganswer`
--
ALTER TABLE `wronganswer`
  ADD PRIMARY KEY (`WrongAnswerId`,`QuestionId`),
  ADD KEY `question_wronganswer_fk_idx` (`QuestionId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `question`
--
ALTER TABLE `question`
  MODIFY `QuestionId` int(25) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=46;
--
-- AUTO_INCREMENT for table `rightanswer`
--
ALTER TABLE `rightanswer`
  MODIFY `QuestionId` int(25) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=46;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `rightanswer`
--
ALTER TABLE `rightanswer`
  ADD CONSTRAINT `rightanswer_constraint_1` FOREIGN KEY (`QuestionId`) REFERENCES `question` (`QuestionId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `wronganswer`
--
ALTER TABLE `wronganswer`
  ADD CONSTRAINT `wronganswer_constraint_1` FOREIGN KEY (`QuestionId`) REFERENCES `question` (`QuestionId`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
