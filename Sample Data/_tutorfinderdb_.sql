-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Erstellungszeit: 11. Nov 2015 um 10:16
-- Server Version: 5.6.17
-- PHP-Version: 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `tutorfinderdb`
--

--
-- Daten für Tabelle `profile`
--

REPLACE INTO `profile` (`id`, `biography`, `email`, `region`, `wage`) VALUES
(1, 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren.', 'ese@example.ch', 'Bern', '50.00'),
(2, NULL, 'student@example.ch', NULL, '0.00'),
(3, 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren.', 'tutor@example.ch', 'Bern', '45.00');

--
-- Daten für Tabelle `user`
--

REPLACE INTO `user` (`id`, `email`, `firstName`, `lastName`, `password`, `role`, `profile_id`) VALUES
(1, 'ese@example.ch', 'Max', 'ESE', '$2a$10$N5YaUKB6O0VAYBzSXzcblOy4S41RF.imdxc8up3KjXyJzVrU9gJ2K', 'TUTOR', 1),
(2, 'student@example.ch', 'Moritz', 'Student', '$2a$10$2L7BW63pERMbcV82MkUzB./syBUyDc6EGmSrmyGkcMZxXNg/OrtCC', 'STUDENT', 2),
(3, 'tutor@example.ch', 'Fritz', 'Tutor', '$2a$10$Ez4ZsuUAnKFWcKlS/0AAC.W9F2VHdMxMRIFnmk6gkiqXGWGRNOvpq', 'TUTOR', 3);

--
-- Daten für Tabelle `subject`
--

REPLACE INTO `subject` (`id`, `grade`, `name`, `user`) VALUES
(1, 5.5, 'ESE', 1),
(2, 5.5, 'Programming 1', 1),
(3, 5.5, 'Programming 2', 1),
(4, 5, 'Management', 3),
(5, 5, 'Lineare Algebra', 3);

--
-- Daten für Tabelle `timetable`
--

REPLACE INTO `timetable` (`id`, `day`, `timeslot`, `user`) VALUES
(1, 0, 17, 1),
(15, 0, 17, 3),
(2, 0, 18, 1),
(16, 0, 18, 3),
(3, 1, 17, 1),
(17, 1, 17, 3),
(4, 1, 18, 1),
(18, 1, 18, 3),
(5, 2, 17, 1),
(19, 2, 17, 3),
(6, 2, 18, 1),
(20, 2, 18, 3),
(7, 3, 17, 1),
(21, 3, 17, 3),
(8, 3, 18, 1),
(22, 3, 18, 3),
(9, 4, 17, 1),
(23, 4, 17, 3),
(10, 4, 18, 1),
(24, 4, 18, 3),
(11, 5, 17, 1),
(25, 5, 17, 3),
(12, 5, 18, 1),
(26, 5, 18, 3),
(13, 6, 17, 1),
(27, 6, 17, 3),
(14, 6, 18, 1),
(28, 6, 18, 3);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
