-- =========================================================
-- Club Náutico - BD generada a partir de las clases (columnas = nombres de variables)
-- Motor: MySQL/MariaDB
-- Fecha: 2025-11-11

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS Alquiler;
DROP TABLE IF EXISTS Reserva;
DROP TABLE IF EXISTS Embarcacion;
DROP TABLE IF EXISTS Patron;
DROP TABLE IF EXISTS Inscripcion;
DROP TABLE IF EXISTS Familia;
DROP TABLE IF EXISTS Socio;

SET FOREIGN_KEY_CHECKS = 1;
-- phpMyAdmin SQL Dump
-- version 2.7.0-pl2
-- http://www.phpmyadmin.net
-- 
-- Servidor: oraclepr.uco.es
-- Tiempo de generación: 14-11-2025 a las 17:16:29
-- Versión del servidor: 5.1.73
-- Versión de PHP: 5.3.3
-- 
-- Base de datos: `i32casaj`
-- 

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `Alquiler`
-- 

CREATE TABLE `Alquiler` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `matriculaEmbarcacion` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `dniTitular` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `fechaInicio` date NOT NULL,
  `fechaFin` date NOT NULL,
  `plazasSolicitadas` int(11) NOT NULL,
  `precioTotal` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_alquiler_embarcacion` (`matriculaEmbarcacion`),
  KEY `fk_alquiler_titular` (`dniTitular`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=4 ;

-- 
-- Volcar la base de datos para la tabla `Alquiler`
-- 

INSERT INTO `Alquiler` VALUES (1, 'ABC-123', '11111111A', '2025-05-10', '2025-05-17', 4, 640);
INSERT INTO `Alquiler` VALUES (2, 'XYZ-789', '11111111A', '2025-10-01', '2025-10-03', 3, 180);
INSERT INTO `Alquiler` VALUES (3, 'ABC-123', '11111111A', '2025-11-14', '2025-11-21', 10, 400);

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `Embarcacion`
-- 

CREATE TABLE `Embarcacion` (
  `matricula` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `nombre` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  `tipo` enum('VELERO','YATE','CATAMARAN','LANCHA') COLLATE utf8_unicode_ci NOT NULL,
  `plazas` int(11) NOT NULL,
  `dimensiones` varchar(80) COLLATE utf8_unicode_ci NOT NULL,
  `patronAsignado` int(11) DEFAULT NULL,
  PRIMARY KEY (`matricula`),
  UNIQUE KEY `uk_embarcacion_nombre` (`nombre`),
  KEY `fk_embarcacion_patron` (`patronAsignado`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- 
-- Volcar la base de datos para la tabla `Embarcacion`
-- 

INSERT INTO `Embarcacion` VALUES ('6a-AB-1-1-15', 'prueba', 'YATE', 5, '300', NULL);
INSERT INTO `Embarcacion` VALUES ('6a-BC-1-3-12', 'Castilla Maricon', 'VELERO', 3, '35', NULL);
INSERT INTO `Embarcacion` VALUES ('8a-BD-1-1-23', 'Big Amaro', 'CATAMARAN', 10, '300', NULL);
INSERT INTO `Embarcacion` VALUES ('ABC-123', 'Tramontana', 'VELERO', 8, '10x3m', 1);
INSERT INTO `Embarcacion` VALUES ('XYZ-789', 'Levante', 'LANCHA', 6, '7x2m', NULL);

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `Familia`
-- 

CREATE TABLE `Familia` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dniTitular` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `numAdultos` int(11) NOT NULL,
  `numNiños` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_familia_titular` (`dniTitular`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=8 ;

-- 
-- Volcar la base de datos para la tabla `Familia`
-- 

INSERT INTO `Familia` VALUES (1, '11111111A', 2, 2);
INSERT INTO `Familia` VALUES (2, '22222222B', 2, 0);
INSERT INTO `Familia` VALUES (3, '87654321X', 2, 1);

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `Inscripcion`
-- 

CREATE TABLE `Inscripcion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tipoCuota` enum('INDIVIDUAL','FAMILIAR') COLLATE utf8_unicode_ci NOT NULL,
  `cuotaAnual` int(11) NOT NULL,
  `fechaInscripcion` date NOT NULL,
  `dniTitular` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `familiaId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_inscripcion_titular` (`dniTitular`),
  KEY `fk_inscripcion_familia` (`familiaId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=3 ;

-- 
-- Volcar la base de datos para la tabla `Inscripcion`
-- 

INSERT INTO `Inscripcion` VALUES (1, 'FAMILIAR', 300, '2023-06-15', '11111111A', 3);
INSERT INTO `Inscripcion` VALUES (2, 'FAMILIAR', 650, '2024-01-10', '22222222B', 2);

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `Patron`
-- 

CREATE TABLE `Patron` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dni` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `nombre` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  `apellidos` varchar(160) COLLATE utf8_unicode_ci NOT NULL,
  `fechaNacimiento` date NOT NULL,
  `fechaTituloPatron` date NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_patron_dni` (`dni`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- 
-- Volcar la base de datos para la tabla `Patron`
-- 

INSERT INTO `Patron` VALUES (1, '33333333C', 'Lucía', 'Serrano Torres', '1990-08-10', '2015-06-01');
INSERT INTO `Patron` VALUES (2, '44444444D', 'Carlos', 'Navarro Páez', '1987-01-22', '2012-04-15');

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `Reserva`
-- 

CREATE TABLE `Reserva` (
  `id` int(11) NOT NULL,
  `matriculaEmbarcacion` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `plazasReserva` int(11) NOT NULL,
  `fechaReserva` date NOT NULL,
  `precioReserva` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_reserva_embarcacion` (`matriculaEmbarcacion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- 
-- Volcar la base de datos para la tabla `Reserva`
-- 

INSERT INTO `Reserva` VALUES (1, 'ABC-123', 5, '2025-06-20', 200);
INSERT INTO `Reserva` VALUES (2, 'XYZ-789', 4, '2025-07-05', 160);

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `Socio`
-- 

CREATE TABLE `Socio` (
  `dni` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `nombre` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  `apellidos` varchar(160) COLLATE utf8_unicode_ci NOT NULL,
  `direccion` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fechaNacimiento` date NOT NULL,
  `patronEmbarcacion` tinyint(1) NOT NULL DEFAULT '0',
  `fechaAlta` date NOT NULL DEFAULT '2025-01-01',
  PRIMARY KEY (`dni`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- 
-- Volcar la base de datos para la tabla `Socio`
-- 

INSERT INTO `Socio` VALUES ('11111111A', 'Ana', 'López García', 'Av. del Puerto 12, Cádiz', '1999-03-21', 1, '0000-00-00');
INSERT INTO `Socio` VALUES ('22222222B', 'Javier', 'Martín Ruiz', 'C/ Cruz Conde 7, Córdoba', '2001-11-02', 0, '0000-00-00');
INSERT INTO `Socio` VALUES ('54590546T', 'Alejandro', 'Gómez Amaro', 'Calle Luciano Pavarotti', '2004-10-14', 1, '0000-00-00');
INSERT INTO `Socio` VALUES ('54590546Z', 'Alejandro', 'Gómez Amaro', 'Calle Luciano Pavarotti', '2002-02-01', 1, '0000-00-00');
INSERT INTO `Socio` VALUES ('54590547A', 'Alejandro', 'Gómez Amaro', 'Calle Luciano Pavarotti', '2005-12-10', 1, '0000-00-00');
INSERT INTO `Socio` VALUES ('54590547E', 'Alejandro', 'Gómez Amaro', 'Calle Luciano Pavarotti', '2004-03-12', 0, '0000-00-00');
INSERT INTO `Socio` VALUES ('54590547R', 'Alejandro', 'Gómez Amaro', 'Calle Luciano Pavarotti', '2004-10-14', 0, '0000-00-00');
INSERT INTO `Socio` VALUES ('87654321X', 'Mohssin', 'Bassat Sidki', 'Calle Manuel Altolaguirre', '2004-05-18', 0, '2025-11-12');

-- 
-- Filtros para las tablas descargadas (dump)
-- 

-- 
-- Filtros para la tabla `Alquiler`
-- 
ALTER TABLE `Alquiler`
  ADD CONSTRAINT `fk_alquiler_embarcacion` FOREIGN KEY (`matriculaEmbarcacion`) REFERENCES `Embarcacion` (`matricula`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_alquiler_titular` FOREIGN KEY (`dniTitular`) REFERENCES `Socio` (`dni`) ON UPDATE CASCADE;

-- 
-- Filtros para la tabla `Embarcacion`
-- 
ALTER TABLE `Embarcacion`
  ADD CONSTRAINT `fk_embarcacion_patron` FOREIGN KEY (`patronAsignado`) REFERENCES `Patron` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

-- 
-- Filtros para la tabla `Familia`
-- 
ALTER TABLE `Familia`
  ADD CONSTRAINT `fk_familia_titular` FOREIGN KEY (`dniTitular`) REFERENCES `Socio` (`dni`) ON UPDATE CASCADE;

-- 
-- Filtros para la tabla `Inscripcion`
-- 
ALTER TABLE `Inscripcion`
  ADD CONSTRAINT `fk_inscripcion_titular` FOREIGN KEY (`dniTitular`) REFERENCES `Socio` (`dni`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_inscripcion_familia` FOREIGN KEY (`familiaId`) REFERENCES `Familia` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

-- 
-- Filtros para la tabla `Reserva`
-- 
ALTER TABLE `Reserva`
  ADD CONSTRAINT `fk_reserva_embarcacion` FOREIGN KEY (`matriculaEmbarcacion`) REFERENCES `Embarcacion` (`matricula`) ON UPDATE CASCADE;
