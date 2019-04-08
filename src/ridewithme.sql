-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.6.19 - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL Version:             8.3.0.4694
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Dumping database structure for ride_with_me
CREATE DATABASE IF NOT EXISTS `ride_with_me` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `ride_with_me`;


-- Dumping structure for table ride_with_me.admin
CREATE TABLE IF NOT EXISTS `admin` (
  `adminid` int(11) NOT NULL AUTO_INCREMENT,
  `password` varchar(50) NOT NULL DEFAULT '0',
  `email` varchar(50) NOT NULL DEFAULT '0',
  `name` varchar(50) NOT NULL DEFAULT '0',
  PRIMARY KEY (`adminid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table ride_with_me.admin: ~0 rows (approximately)
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;


-- Dumping structure for table ride_with_me.availablepassenger
CREATE TABLE IF NOT EXISTS `availablepassenger` (
  `passengerid` int(11) NOT NULL,
  `originlat` double DEFAULT NULL,
  `originlng` double DEFAULT NULL,
  `destinationlat` double DEFAULT NULL,
  `destinationlng` double DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `rating` double DEFAULT NULL,
  PRIMARY KEY (`passengerid`),
  KEY `name` (`name`),
  KEY `email` (`email`),
  CONSTRAINT `email` FOREIGN KEY (`email`) REFERENCES `passenger` (`email`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `name` FOREIGN KEY (`name`) REFERENCES `passenger` (`name`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `pid` FOREIGN KEY (`passengerid`) REFERENCES `passenger` (`passengerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table ride_with_me.availablepassenger: ~4 rows (approximately)
/*!40000 ALTER TABLE `availablepassenger` DISABLE KEYS */;
INSERT INTO `availablepassenger` (`passengerid`, `originlat`, `originlng`, `destinationlat`, `destinationlng`, `name`, `email`, `rating`) VALUES
	(17, 39.95064039628184, 116.3385582715273, 39.968227176862825, 116.35188985615967, 'Kaylo', 'lombe@gmail.com ', 3.5),
	(19, 39.950499805114895, 116.3384261727333, 39.97690171400526, 116.35204408317804, 'Li', 'li@gmail.com', 3.5),
	(23, 39.94772416274722, 116.3372506946, 39.958865119109, 116.33461877703667, 'Sebastian', 'sebastian@gmail.com', 3.5),
	(25, 39.95094034027361, 116.33796148002146, 39.976649671808175, 116.34644865989685, 'Sebastian', 'sebastian@gmail.com', 3.5);
/*!40000 ALTER TABLE `availablepassenger` ENABLE KEYS */;


-- Dumping structure for table ride_with_me.driver
CREATE TABLE IF NOT EXISTS `driver` (
  `driverid` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(70) NOT NULL DEFAULT '0',
  `picture` varchar(70) NOT NULL DEFAULT '0',
  `creditcardnumber` varchar(70) NOT NULL DEFAULT '0',
  `carnumber` varchar(70) NOT NULL DEFAULT '0',
  `carpicture` varchar(70) NOT NULL DEFAULT '0',
  `carmodel` varchar(70) NOT NULL DEFAULT '0',
  `driverlicense` varchar(70) NOT NULL DEFAULT '0',
  `score` double NOT NULL DEFAULT '0',
  `name` varchar(50) DEFAULT '0',
  `email` varchar(50) DEFAULT '0',
  `password` varchar(50) DEFAULT '111111',
  `phonenumber` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`driverid`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- Dumping data for table ride_with_me.driver: ~3 rows (approximately)
/*!40000 ALTER TABLE `driver` DISABLE KEYS */;
INSERT INTO `driver` (`driverid`, `address`, `picture`, `creditcardnumber`, `carnumber`, `carpicture`, `carmodel`, `driverlicense`, `score`, `name`, `email`, `password`, `phonenumber`) VALUES
	(1, 'random place', 'we', '23222', 'abc1111', '0', 'Toyota', '2232', 0, 'Randomguy', 'random@gmail.com', '111111', NULL),
	(2, '2, Beijing Ring Road', 'http://random.com', '1234 5678 9101 1121', 'abc 9283', '0', 'Nissan', 'BJ3234', 2.5, 'Another Random Guy', 'another@gmail.com', '111111', '188109234'),
	(3, '2,bjtu', 'http://imgur.com', '1111 3333 4445', 'Bjtu 2323', '0', 'BMW M5', 'abscs', 0, 'Felix', 'felix@gmail.com', '123456', '18810627721');
/*!40000 ALTER TABLE `driver` ENABLE KEYS */;


-- Dumping structure for table ride_with_me.driverbids
CREATE TABLE IF NOT EXISTS `driverbids` (
  `driverid` int(11) NOT NULL,
  `passengerid` int(11) NOT NULL,
  `price` int(11) NOT NULL,
  `pickuptime` int(11) NOT NULL,
  `bidid` int(11) NOT NULL AUTO_INCREMENT,
  `driverlocationlat` double DEFAULT NULL,
  `driverlocationlng` double DEFAULT NULL,
  `driverEmail` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`bidid`),
  UNIQUE KEY `driverid` (`driverid`),
  KEY `did` (`passengerid`),
  KEY `driverfk` (`driverid`),
  KEY `driverEmail` (`driverEmail`),
  CONSTRAINT `driverEmail` FOREIGN KEY (`driverEmail`) REFERENCES `driver` (`email`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `driverfk` FOREIGN KEY (`driverid`) REFERENCES `driver` (`driverid`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;

-- Dumping data for table ride_with_me.driverbids: ~3 rows (approximately)
/*!40000 ALTER TABLE `driverbids` DISABLE KEYS */;
INSERT INTO `driverbids` (`driverid`, `passengerid`, `price`, `pickuptime`, `bidid`, `driverlocationlat`, `driverlocationlng`, `driverEmail`) VALUES
	(3, 25, 40, 15, 33, 39.958865119112, 116.33461877703662, 'felix@gmail.com'),
	(1, 25, 30, 56, 48, 39.9506378, 116.3386407, 'random@gmail.com'),
	(2, 19, 40, 15, 51, 39.958865119508, 116.33461877703687, 'another@gmail.com');
/*!40000 ALTER TABLE `driverbids` ENABLE KEYS */;


-- Dumping structure for table ride_with_me.passenger
CREATE TABLE IF NOT EXISTS `passenger` (
  `passengerId` int(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(50) DEFAULT NULL,
  `address` varchar(50) DEFAULT NULL,
  `creditcardnumber` varchar(50) DEFAULT NULL,
  `picture` varchar(70) DEFAULT NULL,
  `score` double DEFAULT '3.5',
  `name` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT '111111',
  `email` varchar(50) NOT NULL,
  PRIMARY KEY (`passengerId`),
  UNIQUE KEY `email` (`email`),
  KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COMMENT='For passenger info ';

-- Dumping data for table ride_with_me.passenger: ~10 rows (approximately)
/*!40000 ALTER TABLE `passenger` DISABLE KEYS */;
INSERT INTO `passenger` (`passengerId`, `phone`, `address`, `creditcardnumber`, `picture`, `score`, `name`, `password`, `email`) VALUES
	(16, '12222', 'randomplace', '2321211', 'somelocation', 3.5, 'Lombe', '123456', 'nickhljk'),
	(17, '12222', 'randomplace', '2321211', 'somelocation', 3.5, 'Kaylo', '123456', 'lombe@gmail.com'),
	(18, '12222', 'randomplace', '2321211', 'null', 3.5, 'Kaylo', '123456', 'lombe.chileshe@gmail.com'),
	(19, '12222', 'randomplace', '2321211', 'null', 3.5, 'Li', '123456', 'li@gmail.com'),
	(20, '12222', 'randomplace', '2321211', 'null', 3.5, 'Li', '123456', 'lie@gmail.com'),
	(21, '12222', 'randomplace', '2321211', 'somelocation', 3.5, 'Lombe', '123456', 'lombechileshe@wgmail.com'),
	(22, '18810627807', 'Shanghai', '123456789', 'null', 3.5, 'Nick', '123456', 'nick@gmail.com'),
	(23, '', '', '', '', 3.5, '', 't', ''),
	(24, '5285', '', '3456', '', 3.5, 'Bill Joy\n', 'jdbc', 'jdK@java.com'),
	(25, '188106277754', 'C1205 bjtu', '12344', '', 3.5, 'Sebastian', '123456', 'sebastian@gmail.com');
/*!40000 ALTER TABLE `passenger` ENABLE KEYS */;


-- Dumping structure for table ride_with_me.trips
CREATE TABLE IF NOT EXISTS `trips` (
  `tripsid` int(11) NOT NULL AUTO_INCREMENT,
  `Started` int(11) NOT NULL DEFAULT '0',
  `driverid` int(11) NOT NULL,
  `passengerid` int(11) NOT NULL,
  `driverreview` double DEFAULT '-1',
  `passengerreview` double DEFAULT '-1',
  `cancelled` int(11) DEFAULT '0',
  `passengeremail` varchar(50) DEFAULT '0',
  `driveremail` varchar(50) DEFAULT '0',
  `completed` int(11) DEFAULT '0',
  `price` int(11) NOT NULL,
  `originlat` double NOT NULL,
  `originlng` double NOT NULL,
  `destinationlat` double NOT NULL,
  `destinationlng` double NOT NULL,
  `bidid` int(11) DEFAULT NULL,
  `Date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`tripsid`),
  UNIQUE KEY `passengerid` (`passengerid`),
  UNIQUE KEY `driverid` (`driverid`),
  KEY `tripfk` (`driverid`),
  KEY `passfk` (`passengerid`),
  KEY `passengeremail` (`passengeremail`),
  CONSTRAINT `passengeremail` FOREIGN KEY (`passengeremail`) REFERENCES `passenger` (`email`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `passfk` FOREIGN KEY (`passengerid`) REFERENCES `passenger` (`passengerId`),
  CONSTRAINT `tripfk` FOREIGN KEY (`driverid`) REFERENCES `driver` (`driverid`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8;

-- Dumping data for table ride_with_me.trips: ~1 rows (approximately)
/*!40000 ALTER TABLE `trips` DISABLE KEYS */;
INSERT INTO `trips` (`tripsid`, `Started`, `driverid`, `passengerid`, `driverreview`, `passengerreview`, `cancelled`, `passengeremail`, `driveremail`, `completed`, `price`, `originlat`, `originlng`, `destinationlat`, `destinationlng`, `bidid`, `Date`) VALUES
	(43, 1, 2, 19, 4, 2.5, 0, 'li@gmail.com', 'another@gmail.com', 1, 40, 39.950499805114895, 116.3384261727333, 39.97690171400526, 116.35204408317804, 51, '2014-07-26 18:29:06');
/*!40000 ALTER TABLE `trips` ENABLE KEYS */;


-- Dumping structure for table ride_with_me.tripshistory
CREATE TABLE IF NOT EXISTS `tripshistory` (
  `tripsid` int(11) NOT NULL AUTO_INCREMENT,
  `passengerid` int(11) NOT NULL,
  `driverreview` double DEFAULT '-1',
  `passengerreview` double DEFAULT '-1',
  `cancelled` int(11) DEFAULT NULL,
  `completed` int(11) DEFAULT NULL,
  `price` int(11) NOT NULL,
  `originlat` double NOT NULL,
  `originlng` double NOT NULL,
  `destinationlat` double NOT NULL,
  `destinationlng` double NOT NULL,
  `driverid` int(11) NOT NULL,
  `passengeremail` varchar(50) DEFAULT '0',
  `driveremail` varchar(50) DEFAULT '0',
  `bidid` int(11) NOT NULL,
  `date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`tripsid`),
  KEY `tripfk` (`driverid`),
  KEY `passfk` (`passengerid`),
  KEY `bidfk` (`bidid`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- Dumping data for table ride_with_me.tripshistory: ~4 rows (approximately)
/*!40000 ALTER TABLE `tripshistory` DISABLE KEYS */;
INSERT INTO `tripshistory` (`tripsid`, `passengerid`, `driverreview`, `passengerreview`, `cancelled`, `completed`, `price`, `originlat`, `originlng`, `destinationlat`, `destinationlng`, `driverid`, `passengeremail`, `driveremail`, `bidid`, `date`) VALUES
	(11, 1, 17, 3, 2, 0, 1, 12, 231, 23, 2422, 23, '0', '0', 2, '2014-07-24 10:34:03'),
	(16, 2, 17, 2, 3, 0, 1, 32, 231, 23, 2422, 23, '0', '0', 6, '2014-07-24 10:35:41'),
	(17, 17, 3, 2, 0, 1, 32, 231, 23, 2422, 23, 2, '0', '0', 6, '2014-07-24 14:42:29'),
	(21, 22, 3, 5, 0, 1, 50, 67.34, 234.21, 123.43, 1244.21, 3, '0', '0', 13, '2014-07-24 15:39:08');
/*!40000 ALTER TABLE `tripshistory` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
