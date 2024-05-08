-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: May 08, 2024 at 07:48 PM
-- Server version: 8.0.31
-- PHP Version: 8.1.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `greenta`
--

-- --------------------------------------------------------

--
-- Table structure for table `application`
--

DROP TABLE IF EXISTS `application`;
CREATE TABLE IF NOT EXISTS `application` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `firstname` varchar(255) NOT NULL,
  `lastname` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `jobtitle_id` int DEFAULT NULL,
  `pdf` varchar(255) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `IDX_A45BDDC1E438D15B` (`jobtitle_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `application`
--

INSERT INTO `application` (`Id`, `firstname`, `lastname`, `email`, `jobtitle_id`, `pdf`) VALUES
(1, 'nada', 'nada', 'nada@esprit.tn', NULL, ''),
(2, 'Test', 'Integ', 'test@integ.com', NULL, 'C:\\Users\\nadab\\Downloads\\Nada BENKHLIFA - SERIE 1.pdf'),
(3, 'Test', 'Integ', 'test@integ.com', NULL, 'C:\\Users\\nadab\\Downloads\\Nada BENKHLIFA - SERIE 1.pdf');

-- --------------------------------------------------------

--
-- Table structure for table `charity`
--

DROP TABLE IF EXISTS `charity`;
CREATE TABLE IF NOT EXISTS `charity` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name_of_charity` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `amount_donated` double NOT NULL,
  `location` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_date` datetime NOT NULL,
  `picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `charity`
--

INSERT INTO `charity` (`id`, `name_of_charity`, `amount_donated`, `location`, `last_date`, `picture`) VALUES
(1, 'nada', 500, '', '2024-03-06 13:21:07', ''),
(2, 'test', 500, 'integ', '2024-05-09 00:00:00', 'C:Users\nadabCodenameOne Screenshot 2.png'),
(3, 'try', 1000, 'again', '2024-05-07 00:00:00', 'C:Users\nadabCodenameOne Screenshot 1.png');

-- --------------------------------------------------------

--
-- Table structure for table `cour`
--

DROP TABLE IF EXISTS `cour`;
CREATE TABLE IF NOT EXISTS `cour` (
  `id` int NOT NULL AUTO_INCREMENT,
  `titre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `niveau` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime NOT NULL COMMENT '(DC2Type:datetime_immutable)',
  `categorie` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `pdfpath` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `cour`
--

INSERT INTO `cour` (`id`, `titre`, `description`, `niveau`, `created_at`, `categorie`, `pdfpath`) VALUES
(2, 'test', 'add', 'Intermediate', '2024-05-08 00:51:05', 'Sensitization', 'DS 3B 21-22  ----.pdf');

-- --------------------------------------------------------

--
-- Table structure for table `doctrine_migration_versions`
--

DROP TABLE IF EXISTS `doctrine_migration_versions`;
CREATE TABLE IF NOT EXISTS `doctrine_migration_versions` (
  `version` varchar(191) COLLATE utf8mb3_unicode_ci NOT NULL,
  `executed_at` datetime DEFAULT NULL,
  `execution_time` int DEFAULT NULL,
  PRIMARY KEY (`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

--
-- Dumping data for table `doctrine_migration_versions`
--

INSERT INTO `doctrine_migration_versions` (`version`, `executed_at`, `execution_time`) VALUES
('DoctrineMigrations\\Version20240409232212', '2024-04-09 23:23:01', 290);

-- --------------------------------------------------------

--
-- Table structure for table `donation`
--

DROP TABLE IF EXISTS `donation`;
CREATE TABLE IF NOT EXISTS `donation` (
  `id` int NOT NULL AUTO_INCREMENT,
  `charity_id` int NOT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `date` datetime NOT NULL,
  `first_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone_number` int NOT NULL,
  `amount` double NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQ_31E581A0F5C97E37` (`charity_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `donation`
--

INSERT INTO `donation` (`id`, `charity_id`, `address`, `date`, `first_name`, `last_name`, `phone_number`, `amount`) VALUES
(1, 1, 'nada', '2024-03-06 14:37:23', 'nada', 'bkh', 123, 1500);

-- --------------------------------------------------------

--
-- Table structure for table `epreuve`
--

DROP TABLE IF EXISTS `epreuve`;
CREATE TABLE IF NOT EXISTS `epreuve` (
  `id` int NOT NULL AUTO_INCREMENT,
  `quizid_id` int NOT NULL,
  `date_p` datetime NOT NULL,
  `note` int NOT NULL,
  `etat` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_D6ADE47FE8901217` (`quizid_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `epreuve`
--

INSERT INTO `epreuve` (`id`, `quizid_id`, `date_p`, `note`, `etat`) VALUES
(7, 4, '2024-05-08 18:07:44', 0, 1),
(8, 4, '2024-05-08 18:24:05', 0, 1),
(9, 4, '2024-05-08 18:32:05', 0, 1),
(10, 4, '2024-05-08 18:33:07', 0, 1);

-- --------------------------------------------------------

--
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
CREATE TABLE IF NOT EXISTS `event` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `location` varchar(255) NOT NULL,
  `organizer` varchar(255) NOT NULL,
  `capacity` int NOT NULL,
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `event`
--

INSERT INTO `event` (`Id`, `title`, `start_date`, `end_date`, `location`, `organizer`, `capacity`, `image`) VALUES
(2, 'Testing', '2024-05-13 23:00:00', '2024-05-14 23:00:00', 'Pidev', 'Image', 20, 'null');

-- --------------------------------------------------------

--
-- Table structure for table `job`
--

DROP TABLE IF EXISTS `job`;
CREATE TABLE IF NOT EXISTS `job` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `organisation` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `startdate` date NOT NULL,
  `picture` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `job`
--

INSERT INTO `job` (`Id`, `organisation`, `title`, `description`, `startdate`, `picture`) VALUES
(1, 'esprit', 'enseignant', 'enseignant java', '2025-03-06', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `messenger_messages`
--

DROP TABLE IF EXISTS `messenger_messages`;
CREATE TABLE IF NOT EXISTS `messenger_messages` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `body` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `headers` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `queue_name` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime NOT NULL,
  `available_at` datetime NOT NULL,
  `delivered_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_75EA56E0FB7336F0` (`queue_name`),
  KEY `IDX_75EA56E0E3BD61CE` (`available_at`),
  KEY `IDX_75EA56E016BA31DB` (`delivered_at`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
CREATE TABLE IF NOT EXISTS `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `productname` varchar(255) NOT NULL,
  `productquantity` int NOT NULL,
  `productsize` varchar(255) NOT NULL,
  `productprice` double NOT NULL,
  `productdescription` varchar(255) NOT NULL,
  `productdisponibility` varchar(255) NOT NULL,
  `productimg` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`id`, `productname`, `productquantity`, `productsize`, `productprice`, `productdescription`, `productdisponibility`, `productimg`) VALUES
(1, 'tshirt', 4, 'M', 69.69, 'selemselemselem', 'Unavailable', 'ieosjkldeoiqjslkfqeflk'),
(2, 'test', 9, 'M', 10, 'integ', 'Available', 'D:\\home.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `product_category`
--

DROP TABLE IF EXISTS `product_category`;
CREATE TABLE IF NOT EXISTS `product_category` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `categoryname` varchar(255) NOT NULL,
  `categoryimage` varchar(255) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `product_category`
--

INSERT INTO `product_category` (`Id`, `categoryname`, `categoryimage`) VALUES
(2, 'kaabi', 'kzjend'),
(3, 'TextField[id=productcategoryname, styleClass=text-input text-field]', 'null');

-- --------------------------------------------------------

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
CREATE TABLE IF NOT EXISTS `question` (
  `id` int NOT NULL AUTO_INCREMENT,
  `quizid_id` int DEFAULT NULL,
  `question` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `reponse` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `note` int NOT NULL,
  `reponseCorrecte` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_B6F7494EE8901217` (`quizid_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `question`
--

INSERT INTO `question` (`id`, `quizid_id`, `question`, `reponse`, `note`, `reponseCorrecte`) VALUES
(3, 4, 'test', '1;2;3;4', 4, 4);

-- --------------------------------------------------------

--
-- Table structure for table `quiz`
--

DROP TABLE IF EXISTS `quiz`;
CREATE TABLE IF NOT EXISTS `quiz` (
  `id` int NOT NULL AUTO_INCREMENT,
  `courid_id` int DEFAULT NULL,
  `titre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime NOT NULL COMMENT '(DC2Type:datetime_immutable)',
  `duree` int NOT NULL,
  `note` int NOT NULL,
  `nbrq` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_A412FA92CBF021D4` (`courid_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `quiz`
--

INSERT INTO `quiz` (`id`, `courid_id`, `titre`, `created_at`, `duree`, `note`, `nbrq`) VALUES
(4, 2, 'test', '2024-05-08 01:34:18', 30, 1, 1),
(5, 2, 'Test', '2024-05-08 01:35:45', 5, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `reset_password_request`
--

DROP TABLE IF EXISTS `reset_password_request`;
CREATE TABLE IF NOT EXISTS `reset_password_request` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `selector` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `hashed_token` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `requested_at` datetime NOT NULL COMMENT '(DC2Type:datetime_immutable)',
  `expires_at` datetime NOT NULL COMMENT '(DC2Type:datetime_immutable)',
  PRIMARY KEY (`id`),
  KEY `IDX_7CE748AA76ED395` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sale`
--

DROP TABLE IF EXISTS `sale`;
CREATE TABLE IF NOT EXISTS `sale` (
  `id_sale` int NOT NULL AUTO_INCREMENT,
  `nbr_vente` int NOT NULL,
  `product_id` int NOT NULL,
  PRIMARY KEY (`id_sale`),
  KEY `product` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `sale`
--

INSERT INTO `sale` (`id_sale`, `nbr_vente`, `product_id`) VALUES
(1, 1, 1),
(2, 1, 2);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `firstname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `lastname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_active` tinyint(1) DEFAULT NULL,
  `is_banned` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQ_8D93D649E7927C74` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `firstname`, `lastname`, `email`, `phone`, `password`, `roles`, `is_active`, `is_banned`) VALUES
(17, 'greenta', 'society', 'greenta@gmail.com', '123123', '$2y$13$EM4I/jIj3eF5K2jEfdC.GO7j0ITIvHxd3x.YrJgTF9lGm8roOcgXm', 'ROLE_ADMIN', 0, 0),
(30, 'Nadaa', 'Benkhlifa', 'nadabenkhlifa@esprit.tn', '93144651', '$2y$13$Mnzw26gVIOTf.SfO6RQOru01795muIQkAtAwHJPTo1ftAkwFt2zoy', 'ROLE_ADMIN', 1, 0),
(38, 'test', 'status', 'test@statius.om', '123', '$2y$13$a/k0aTs1C.J3n20KFTKyZeYFoAHJteMy7h3dgljbsbKgFUA87ki5.', 'ROLE_USER', 0, 0),
(39, 'Antika', 'Application', 'antika.application@gmail.com', '23456789', '$2y$13$y4PCSmcJceHXFPMRk328yeXSnzESX/WYJci2tatDtY1AMqSMWDYYu', 'ROLE_USER', 1, 0),
(64, 'wassim', 'frigui', 'wess@gmail.com', '12312312', '$2y$13$Qf2jMgam.GsGecQtkZj5wO13r2.fm3SrF7aX/3oI1PozNuI73c2/y', 'ROLE_CLIENT', 1, 1),
(65, 'b', 'bb', 'b@gmail.com', '12312312', '$2y$13$ET3bIHRCiZldn.B8y48LQ.9SUGwZpc.GJuUk/rm5DJxXQhjB20A5q', 'ROLE_CLIENT', 0, 1),
(68, '3a61', 'pidev', 'pi@gmail.com', '12312312', '$2y$13$gNTe1OPEhDszT9ZvzIvgQeb9jW67mXb0Y8DmISg7cnbU18lCCBZsu', 'ROLE_CLIENT', 1, 1),
(70, 'Remember', 'mee', 'me@gmail.com', '11111111', '$2y$13$LCDyP3pTf0jZd4EvSPnjveYyX1BzKoaAX0i7fnmcFKZNggfSaUvq2', 'ROLE_CLIENT', 1, 0);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `application`
--
ALTER TABLE `application`
  ADD CONSTRAINT `FK_A45BDDC1E438D15B` FOREIGN KEY (`jobtitle_id`) REFERENCES `job` (`Id`);

--
-- Constraints for table `donation`
--
ALTER TABLE `donation`
  ADD CONSTRAINT `FK_31E581A0F5C97E37` FOREIGN KEY (`charity_id`) REFERENCES `charity` (`id`);

--
-- Constraints for table `epreuve`
--
ALTER TABLE `epreuve`
  ADD CONSTRAINT `FK_D6ADE47FE8901217` FOREIGN KEY (`quizid_id`) REFERENCES `quiz` (`id`);

--
-- Constraints for table `question`
--
ALTER TABLE `question`
  ADD CONSTRAINT `FK_B6F7494EE8901217` FOREIGN KEY (`quizid_id`) REFERENCES `quiz` (`id`);

--
-- Constraints for table `quiz`
--
ALTER TABLE `quiz`
  ADD CONSTRAINT `FK_A412FA92CBF021D4` FOREIGN KEY (`courid_id`) REFERENCES `cour` (`id`);

--
-- Constraints for table `reset_password_request`
--
ALTER TABLE `reset_password_request`
  ADD CONSTRAINT `FK_7CE748AA76ED395` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `sale`
--
ALTER TABLE `sale`
  ADD CONSTRAINT `sale_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
