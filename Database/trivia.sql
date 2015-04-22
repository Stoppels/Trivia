-- phpMyAdmin SQL Dump
-- version 4.4.2
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Gegenereerd op: 22 apr 2015 om 14:21
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

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `antwoordfout`
--

CREATE TABLE IF NOT EXISTS `antwoordfout` (
  `AntwoordFoutID` int(5) NOT NULL,
  `AntwoordFout` varchar(255) DEFAULT NULL,
  `VraagID` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Gegevens worden geëxporteerd voor tabel `antwoordfout`
--

INSERT INTO `antwoordfout` (`AntwoordFoutID`, `AntwoordFout`, `VraagID`) VALUES
(0, 'Nederland', 1),
(1, 'Spanje', 1),
(2, 'Brazilië', 1);

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `antwoordgoed`
--

CREATE TABLE IF NOT EXISTS `antwoordgoed` (
  `AntwoordGoedID` int(5) NOT NULL,
  `AntwoordGoed` varchar(255) DEFAULT NULL,
  `VraagID` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Gegevens worden geëxporteerd voor tabel `antwoordgoed`
--

INSERT INTO `antwoordgoed` (`AntwoordGoedID`, `AntwoordGoed`, `VraagID`) VALUES
(1, 'Duitsland', 1);

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `vraag`
--

CREATE TABLE IF NOT EXISTS `vraag` (
  `VraagID` int(5) NOT NULL,
  `Vraag` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Gegevens worden geëxporteerd voor tabel `vraag`
--

INSERT INTO `vraag` (`VraagID`, `Vraag`) VALUES
(0, 'Wanneer begon de Eerste Wereldoorlog?'),
(1, 'Welk land won het WK voetbal 2014?'),
(2, 'Hoe heette het vorige project?'),
(3, 'Hoe heet dit project?');

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
-- Beperkingen voor geëxporteerde tabellen
--

--
-- Beperkingen voor tabel `antwoordfout`
--
ALTER TABLE `antwoordfout`
  ADD CONSTRAINT `antwoordfout_constraint_1` FOREIGN KEY (`VraagID`) REFERENCES `vraag` (`VraagID`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Beperkingen voor tabel `antwoordgoed`
--
ALTER TABLE `antwoordgoed`
  ADD CONSTRAINT `antwoordgoed_constraint_1` FOREIGN KEY (`VraagID`) REFERENCES `vraag` (`VraagID`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
