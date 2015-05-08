-- phpMyAdmin SQL Dump
-- version 4.4.2
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Gegenereerd op: 08 mei 2015 om 14:48
-- Serverversie: 5.6.20
-- PHP-versie: 5.5.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `trivia`
--
-- DROP SCHEMA `trivia`;

CREATE SCHEMA IF NOT EXISTS `trivia`;
USE `trivia`;


-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `antwoordfout`
--

CREATE TABLE IF NOT EXISTS `antwoordfout` (
  `AntwoordFoutID` int(9) NOT NULL,
  `AntwoordFout` varchar(255) DEFAULT NULL,
  `VraagID` int(9) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Gegevens worden geëxporteerd voor tabel `antwoordfout`
--

INSERT INTO `antwoordfout` (`AntwoordFoutID`, `AntwoordFout`, `VraagID`) VALUES
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
(2, '1940', 1),
(2, 'Spanje', 2),
(2, 'Calcium', 3),
(2, 'Ijzer', 4),
(2, 'Willem van Oranje', 5),
(2, '1868', 6),
(2, 'Willem ALexander', 7),
(2, 'Centaur', 8),
(2, '1967', 9),
(2, 'Oude mensen', 10),
(2, 'Barack Obama', 11),
(2, 'Fulgencio Batista', 12),
(2, 'Noorwegen', 13),
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
(3, 'Zweden', 13);

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `antwoordgoed`
--

CREATE TABLE IF NOT EXISTS `antwoordgoed` (
  `AntwoordGoedID` int(9) NOT NULL,
  `AntwoordGoed` varchar(255) DEFAULT NULL,
  `VraagID` int(25) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

--
-- Gegevens worden geëxporteerd voor tabel `antwoordgoed`
--

INSERT INTO `antwoordgoed` (`AntwoordGoedID`, `AntwoordGoed`, `VraagID`) VALUES
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
(13, 'Polen', 13);

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `vraag`
--

CREATE TABLE IF NOT EXISTS `vraag` (
  `VraagID` int(25) NOT NULL,
  `Vraag` varchar(255) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

--
-- Gegevens worden geëxporteerd voor tabel `vraag`
--

INSERT INTO `vraag` (`VraagID`, `Vraag`) VALUES
(1, 'Wanneer begon de Eerste Wereldoorlog?'),
(2, 'Welk land won het WK voetbal 2014?'),
(3, 'Wat is de moderne naam van het vroegere “Caledonia”?'),
(4, 'Voor welk scheikundig element staat het symbool Cu?'),
(5, 'Wie staat er met zijn makkers in brons vereeuwigd buiten het kasteel van Nottingham in Engeland?'),
(6, 'In welk jaar kreeg Moeder Theresa de Nobelprijs voor de Vrede?'),
(7, 'Wie was de langst zittende premier van Nederland?'),
(8, 'Wat is de naam van het legendarische monster dat half mens, half stier was en door Theseus werd gedood?'),
(9, 'In welk jaar was de eerste Elfstedentocht?'),
(10, 'Waar is iemand die lijdt aan xenofobie bang voor?'),
(11, 'Welke Amerikaanse president besloot tot het inzetten van de atoombom tegen Japan?'),
(12, 'Wie bevrijdde Cuba in 1959 van de corrupte dictator Batista?'),
(13, 'Welk land is geen onderdeel van Scandinavië?');

--
-- Indexen voor geëxporteerde tabellen
--

--
-- Indexen voor tabel `antwoordfout`
--
ALTER TABLE `antwoordfout`
  ADD PRIMARY KEY (`AntwoordFoutID`,`VraagID`),
  ADD KEY `vraag_antwoordfout_fk_idx` (`VraagID`);

--
-- Indexen voor tabel `antwoordgoed`
--
ALTER TABLE `antwoordgoed`
  ADD PRIMARY KEY (`AntwoordGoedID`,`VraagID`),
  ADD KEY `vraag_antwoordgoed_fk_idx` (`VraagID`);

--
-- Indexen voor tabel `vraag`
--
ALTER TABLE `vraag`
  ADD PRIMARY KEY (`VraagID`);

--
-- AUTO_INCREMENT voor geëxporteerde tabellen
--

--
-- AUTO_INCREMENT voor een tabel `antwoordgoed`
--
ALTER TABLE `antwoordgoed`
  MODIFY `VraagID` int(25) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=14;
--
-- AUTO_INCREMENT voor een tabel `vraag`
--
ALTER TABLE `vraag`
  MODIFY `VraagID` int(25) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=14;
--
-- Beperkingen voor geëxporteerde tabellen
--

--
-- Beperkingen voor tabel `antwoordfout`
--
ALTER TABLE `antwoordfout`
  ADD CONSTRAINT `antwoordfout_constraint_1` FOREIGN KEY (`VraagID`) REFERENCES `vraag` (`VraagID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Beperkingen voor tabel `antwoordgoed`
--
 ALTER TABLE `antwoordgoed`
  ADD CONSTRAINT `antwoordgoed_constraint_1` FOREIGN KEY (`VraagID`) REFERENCES `vraag` (`VraagID`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
