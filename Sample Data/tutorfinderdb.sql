-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Erstellungszeit: 02. Dez 2015 um 13:52
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

REPLACE INTO `profile` (`id`, `biography`, `countedRatings`, `email`, `language`, `rating`, `region`, `university`, `wage`) VALUES
(1, 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.', NULL, 'ese@example.ch', 'English', NULL, 'Bern and agglomeration', 'Bern', '45.00'),
(2, 'This should be a biography', NULL, 'tutor@example.ch', 'German, English, French', NULL, 'Bern and agglomeration', 'Bern', '50.00'),
(3, NULL, 0, 'student@example.ch', NULL, NULL, NULL, NULL, '0.00'),
(4, 'This should be a biography', NULL, 'julia@example', 'German, English, Italian', NULL, 'Bern and agglomeration', 'Bern', '55.00'),
(5, 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.', NULL, 'sofia@example.ch', 'German, English, Rhaeto-Romanic', NULL, 'Bern and agglomeration', 'Bern', '60.00');

--
-- Daten für Tabelle `user`
--

REPLACE INTO `user` (`id`, `email`, `firstName`, `lastName`, `password`, `role`, `profile_id`) VALUES
(1, 'ese@example.ch', 'Max', 'ESE', '$2a$10$4hkyF6ycGtcS0i768hoOqukuYyFdHFaawus825HtYHeX85kDcmMx2', 'TUTOR', 1),
(2, 'tutor@example.ch', 'Moritz', 'Tutor', '$2a$10$EJWEcLI9JZHuFETJnsqliuRsmHjQ5MMV8YH3.duxOY8dEfdENoF2u', 'TUTOR', 2),
(3, 'student@example.ch', 'Fritz', 'Student', '$2a$10$HHH3/c3tcvMGOtFRJJ43keB.9S5Zkrs3zOzQf1SPerlvid6ILJDKy', 'STUDENT', 3),
(4, 'julia@example.ch', 'Julia', 'Müller', '$2a$10$EJWEcLI9JZHuFETJnsqliuRsmHjQ5MMV8YH3.duxOY8dEfdENoF2u', 'TUTOR', 4),
(5, 'sofia@example.ch', 'Sofia', 'Sossai', '$2a$10$EJWEcLI9JZHuFETJnsqliuRsmHjQ5MMV8YH3.duxOY8dEfdENoF2u', 'TUTOR', 5);

--
-- Daten für Tabelle `timetable`
--

REPLACE INTO `timetable` (`id`, `day`, `timeslot`, `user`) VALUES
(1, 0, 17, 2),
(2, 0, 18, 2),
(3, 1, 17, 2),
(4, 1, 18, 2),
(5, 2, 17, 2),
(6, 2, 18, 2),
(7, 3, 17, 2),
(8, 3, 18, 2),
(9, 4, 17, 2),
(10, 4, 18, 2),
(11, 5, 17, 2),
(12, 5, 18, 2),
(13, 6, 17, 2),
(14, 6, 18, 2),
(15, 0, 17, 1),
(16, 0, 18, 1),
(17, 1, 17, 1),
(18, 1, 18, 1),
(19, 2, 17, 1),
(20, 2, 18, 1),
(21, 3, 17, 1),
(22, 3, 18, 1),
(23, 0, 17, 4),
(24, 0, 18, 4),
(25, 0, 19, 4),
(26, 2, 17, 4),
(27, 2, 18, 4),
(28, 2, 19, 4),
(29, 1, 18, 5),
(30, 1, 19, 5),
(31, 3, 13, 5),
(31, 3, 14, 5),
(32, 3, 15, 5),
(33, 3, 17, 5);


--
-- Daten für Tabelle `appointment`
--

REPLACE INTO `appointment` (`id`, `availability`, `day`, `rating`, `timestamp`, `wage`, `student`, `tutor`) VALUES
(1, 3, 0, NULL, '2015-11-23 14:00:00', '60.00', 1, 5),
(2, 3, 6, NULL, '2015-12-20 17:00:00', '60.00', 2, 5),
(3, 2, 2, NULL, '2015-11-25 17:00:00', '45.00', 3, 1),
(4, 3, 3, NULL, '2015-11-26 13:00:00', '50.00', 4, 2),
(5, 3, 5, NULL, '2015-11-28 14:00:00', '50.00', 5, 2),
(6, 3, 4, NULL, '2016-01-29 16:00:00', '55.00', 1, 4),
(7, 2, 1, NULL, '2015-12-22 16:00:00', '55.00', 2, 4),
(8, 2, 1, NULL, '2015-12-15 16:00:00', '50.00', 3, 2),
(12, 3, 4, NULL, '2015-12-11 15:00:00', '45.00', 4, 1),
(13, 3, 5, NULL, '2015-12-12 16:00:00', '55.00', 5, 4),
(14, 3, 6, NULL, '2015-12-13 17:00:00', '50.00', 1, 2),
(15, 3, 0, NULL, '2015-12-14 18:00:00', '45.00', 2, 4),
(16, 3, 1, NULL, '2015-12-15 19:00:00', '45.00', 3, 1),
(17, 3, 2, NULL, '2015-12-16 20:00:00', '50.00', 4, 2),
(18, 3, 1, NULL, '2015-11-17 21:00:00', '45.00', 5, 1),
(19, 3, 2, NULL, '2015-11-18 22:00:00', '60.00', 1, 5),
(20, 3, 3, NULL, '2015-11-19 23:00:00', '55.00', 2, 4),
(21, 3, 4, NULL, '2015-11-20 00:00:00', '45.00', 3, 1),
(22, 3, 5, NULL, '2015-11-21 01:00:00', '50.00', 4, 2),
(23, 3, 6, NULL, '2015-11-22 02:00:00', '55.00', 5, 4),
(24, 2, 1, NULL, '2015-01-10 03:00:00', '50.00', 1, 2),
(25, 2, 0, NULL, '2015-01-11 04:00:00', '45.00', 2, 1),
(26, 2, 1, NULL, '2016-01-12 17:00:00', '55.00', 3, 4),
(27, 2, 2, NULL, '2016-01-13 18:00:00', '50.00', 4, 2),
(28, 2, 3, NULL, '2016-01-14 19:00:00', '45.00', 5, 1),
(29, 2, 4, NULL, '2016-01-15 20:00:00', '60.00', 1, 5),
(30, 2, 5, NULL, '2016-01-16 21:00:00', '45.00', 2, 1),
(31, 2, 6, NULL, '2016-01-17 22:00:00', '50.00', 3, 2),
(32, 2, 0, NULL, '2016-01-18 23:00:00', '60.00', 4, 5),
(33, 2, 1, NULL, '2016-01-19 17:00:00', '50.00', 5, 2),
(34, 2, 2, NULL, '2016-01-20 18:00:00', '55.00', 1, 4),
(35, 2, 3, NULL, '2016-01-21 19:00:00', '60.00', 2, 5),
(36, 2, 4, NULL, '2016-01-22 20:00:00', '45.00', 3, 1),
(37, 2, 5, NULL, '2016-01-23 21:00:00', '50.00', 4, 2),
(38, 2, 6, NULL, '2016-01-24 17:00:00', '55.00', 5, 4);

--
-- Daten für Tabelle `bill`
--

REPLACE INTO `bill` (`id`, `amount`, `month`, `monthValue`, `paymentStatus`, `year`, `tutor_id`, `percentage`, `total`) VALUES
(1, '19.00', 'November', 11, 0, 2015, 1, '0.10', '190.00'),
(2, '15.00', 'November', 11, 0, 2015, 2, '0.10', '150.00'),
(4, '10.00', 'November', 11, 0, 2015, 4, '0.10', '100.00'),
(5, '5.00', 'November', 11, 0, 2015, 5, '0.10', '50.00');

--
-- Daten für Tabelle `subject`
--

REPLACE INTO `subject` (`id`, `grade`, `name`, `user`) VALUES
(1, 4.5, 'Analysis', 4),
(2, 5.5, 'ESE', 2),
(3, 6, 'Programming 1', 1),
(4, 5.5, 'Programming 2', 1),
(5, 6, 'ESE', 1),
(6, 5.5, 'Lineare Algebra', 4),
(7, 4.5, 'Programming 1', 2),
(8, 5, 'Programming 2', 2),
(9, 4, 'Management', 2),
(10, 4, 'Human-Computer-Interface', 5),
(11, 4.5, 'Computernetwork', 5);

--
-- Daten für Tabelle `message`
--

REPLACE INTO `message` (`id`, `isRead`, `message`, `subject`, `timestamp`, `receiver`, `sender`) VALUES
(1, 0, 'This should be a message:\r\nLorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.\r\nAt vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.', 'Here is the subject', '2015-11-26 11:21:00', 1, 2),
(2, 0, 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.\r\nAt vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.', 'Hallo', '2015-11-26 11:24:00', 2, 3),
(4, 0, 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.\r\nAt vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.', 'Here is another subject', '2015-11-28 11:14:24', 3, 4),
(2, 0, 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.\r\nAt vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.', 'Test subject', '2015-11-26 11:24:00', 4, 5),
(2, 0, 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.\r\nAt vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.', 'Hallo', '2015-11-26 11:24:00', 5, 1),
(2, 0, 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.\r\nAt vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.', 'Here should a subject be', '2015-11-26 11:24:00', 1, 5),
(2, 0, 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.\r\nAt vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.', 'Exampe subject', '2015-11-26 11:24:00', 2, 4);


/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
