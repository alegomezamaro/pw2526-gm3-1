DROP TABLE IF EXISTS `Student`;
CREATE TABLE IF NOT EXISTS `Student` (
  `id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `surname` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `birthDate` DATE DEFAULT NULL,
  `type` ENUM('FULL_TIME', 'PARTIAL_TIME', 'ERASMUS'),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO `Student` VALUES (1, 'Andrea', 'Aguirre', '2000-01-10', 'FULL_TIME');
INSERT INTO `Student` VALUES (2, 'Beatriz', 'Benitez', '2000-02-12', 'FULL_TIME');
INSERT INTO `Student` VALUES (3, 'Carlos', 'Castro', '2000-03-03', 'PARTIAL_TIME');
INSERT INTO `Student` VALUES (4, 'David', 'Dominguez', '2001-04-14', 'FULL_TIME');
INSERT INTO `Student` VALUES (5, 'Eric', 'Elliott', '2002-05-25', 'ERASMUS');
INSERT INTO `Student` VALUES (6, 'Francesca', 'Ferguson', '2002-06-26', 'ERASMUS');

-- Create Socio table (current phase only Socio exists)
DROP TABLE IF EXISTS `Socio`;
CREATE TABLE IF NOT EXISTS `Socio` (
  `dni` INT NOT NULL,
  `name` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
  `surname` VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
  `address` VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `birthDate` DATE DEFAULT NULL,
  `inscriptionDate` DATE DEFAULT NULL,
  `patronEmbarcacion` TINYINT(1) NOT NULL DEFAULT 0,
  `inscriptionId` INT DEFAULT NULL,
  PRIMARY KEY (`dni`),
  KEY `idx_inscriptionId` (`inscriptionId`),
  CHECK (`dni` BETWEEN 10000000 AND 99999999)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Seed data: realistic Spanish DNIs (8 dígitos + letra) y direcciones
INSERT INTO `Socio` (`dni`, `name`, `surname`, `address`, `birthDate`, `inscriptionDate`, `patronEmbarcacion`, `inscriptionId`) VALUES
(12345678, 'Ana', 'López García', 'Av. del Puerto 12, Cádiz', '1999-03-21', '2023-06-15', 1, NULL),
(87654321, 'Javier', 'Martín Ruiz', 'C/ Cruz Conde 7, Córdoba', '2001-11-02', '2024-01-10', 0, NULL),
(70890123, 'Lucía', 'Serrano Torres', 'Gran Vía 101, Madrid', '2000-08-14', '2022-09-01', 1, NULL),
(22446688, 'Carlos', 'Castillo Romero', 'Rambla Catalunya 45, Barcelona', '1998-05-05', '2021-04-20', 0, NULL),
(33445566, 'Beatriz', 'Navarro Fernández', 'C/ Larios 3, Málaga', '2002-12-30', '2024-07-01', 0, NULL),
(44556677, 'Sergio', 'Pérez Domínguez', 'Av. de la Constitución 8, Sevilla', '1997-01-17', '2020-03-12', 1, NULL);
