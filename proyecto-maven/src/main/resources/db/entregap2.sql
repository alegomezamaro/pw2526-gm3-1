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
-- Tiempo de generación: 14-12-2025 a las 22:58:42
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
  `dniTitular` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `fechaInicio` date NOT NULL,
  `fechaFin` date NOT NULL,
  `plazasSolicitadas` int(11) NOT NULL,
  `precioTotal` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_alquiler_embarcacion` (`matriculaEmbarcacion`),
  KEY `fk_alquiler_titular` (`dniTitular`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=9 ;

-- 
-- Volcar la base de datos para la tabla `Alquiler`
-- 

INSERT INTO `Alquiler` VALUES (1, '7a-AB-1-2-20', NULL, '2025-05-10', '2025-05-17', 4, 640);
INSERT INTO `Alquiler` VALUES (2, '7a-XY-1-3-21', '87654321X', '2025-10-01', '2025-10-03', 3, 180);
INSERT INTO `Alquiler` VALUES (3, '7a-AB-1-2-20', '22222222B', '2025-11-14', '2025-11-21', 10, 400);
INSERT INTO `Alquiler` VALUES (6, '8a-BD-1-1-23', NULL, '2025-11-23', '2025-12-02', 3, 299.99);
INSERT INTO `Alquiler` VALUES (8, '6a-AB-1-1-15', NULL, '2025-12-09', '2025-12-11', 4, 200);

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

INSERT INTO `Embarcacion` VALUES ('6a-AB-1-1-15', 'Patch Boat', 'YATE', 5, '300x20m', NULL);
INSERT INTO `Embarcacion` VALUES ('6a-BC-1-3-12', 'Api Boat', 'YATE', 15, '100x20m', NULL);
INSERT INTO `Embarcacion` VALUES ('7a-AB-1-2-20', 'Tramontana', 'CATAMARAN', 12, '30x5m', NULL);
INSERT INTO `Embarcacion` VALUES ('7a-XY-1-3-21', 'Levante', 'LANCHA', 6, '72x8m', NULL);
INSERT INTO `Embarcacion` VALUES ('8a-BD-1-1-23', 'Big Amaro', 'CATAMARAN', 10, '300x25m', NULL);

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `Familia`
-- 

CREATE TABLE `Familia` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dniTitular` varchar(20) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `numAdultos` int(11) NOT NULL,
  `numNiños` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_familia_titular` (`dniTitular`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=19 ;

-- 
-- Volcar la base de datos para la tabla `Familia`
-- 

INSERT INTO `Familia` VALUES (1, '11111111A', 2, 2);
INSERT INTO `Familia` VALUES (2, '22222222B', 2, 0);
INSERT INTO `Familia` VALUES (3, '87654321X', 3, 1);
INSERT INTO `Familia` VALUES (8, '87658371X', 3, 0);

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
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=29 ;

-- 
-- Volcar la base de datos para la tabla `Inscripcion`
-- 

INSERT INTO `Inscripcion` VALUES (1, 'FAMILIAR', 300, '2023-06-15', '11111111A', 3);
INSERT INTO `Inscripcion` VALUES (2, 'FAMILIAR', 650, '2024-01-10', '22222222B', 2);
INSERT INTO `Inscripcion` VALUES (5, 'FAMILIAR', 300, '2025-11-27', '12345678R', 8);
INSERT INTO `Inscripcion` VALUES (14, 'FAMILIAR', 300, '2025-12-13', '13579246D', 3);

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=5 ;

-- 
-- Volcar la base de datos para la tabla `Patron`
-- 

INSERT INTO `Patron` VALUES (1, '33333333C', 'Lucía', 'Serrano Torres', '1999-01-01', '2015-06-01');
INSERT INTO `Patron` VALUES (4, '12345678R', 'Mohssin', 'Bassat', '2004-12-14', '2025-12-14');

-- --------------------------------------------------------

-- 
-- Estructura de tabla para la tabla `Reserva`
-- 

CREATE TABLE `Reserva` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `matriculaEmbarcacion` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `plazasReserva` int(11) NOT NULL,
  `fechaReserva` date NOT NULL,
  `precioReserva` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_reserva_embarcacion` (`matriculaEmbarcacion`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=30 ;

-- 
-- Volcar la base de datos para la tabla `Reserva`
-- 

INSERT INTO `Reserva` VALUES (1, '7a-AB-1-2-20', 8, '2025-04-12', 400);
INSERT INTO `Reserva` VALUES (2, '7a-XY-1-3-21', 4, '2025-07-05', 160);
INSERT INTO `Reserva` VALUES (7, '8a-BD-1-1-23', 6, '2025-03-01', 240);
INSERT INTO `Reserva` VALUES (16, '6a-AB-1-1-15', 4, '2034-01-01', 160);

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

INSERT INTO `Socio` VALUES ('10293847D', 'Alejandro', 'Antúnez', 'Nueva Direccion 7', '2005-12-11', 0, '2025-12-09');
INSERT INTO `Socio` VALUES ('11111111A', 'Ana', 'López García', 'Av. del Puerto 12, Cádiz', '1999-03-21', 1, '2023-11-12');
INSERT INTO `Socio` VALUES ('12345678K', 'prueba', 'inscripcion', 'calle colon', '2004-06-16', 0, '2025-11-27');
INSERT INTO `Socio` VALUES ('12345678R', 'Pedro', 'Perez', 'Calle Magallanes', '2004-11-10', 0, '2025-11-27');
INSERT INTO `Socio` VALUES ('12345678S', 'Álvaro', 'Garcia', 'Calle Lorca', '2003-02-15', 0, '2025-11-27');
INSERT INTO `Socio` VALUES ('12345678Z', 'Cliente', 'Test', NULL, '1999-01-01', 0, '2025-12-04');
INSERT INTO `Socio` VALUES ('13579246D', 'Julian', 'López Mon', 'Calle Trabajo n41', '1994-10-04', 0, '2025-12-13');
INSERT INTO `Socio` VALUES ('19283746E', 'Pedro', 'Amaro', 'Calle Luciano, 3, Lucena', '2004-03-12', 0, '2023-11-12');
INSERT INTO `Socio` VALUES ('22222222B', 'Javier', 'Martín Ruiz', 'C/ Cruz Conde 7, Córdoba', '2001-11-02', 0, '2023-11-12');
INSERT INTO `Socio` VALUES ('32009284H', 'José', 'Sánchez', 'Calle Córdoba, 14, Sevilla', '2004-10-14', 0, '2023-11-12');
INSERT INTO `Socio` VALUES ('32567893C', 'Kilian', 'Ramirez', 'Calle Luis Montoto n25', '1998-06-10', 0, '2025-12-13');
INSERT INTO `Socio` VALUES ('34569876B', 'Mohssin', 'Bassat', 'Calle Antonio Maura 4', '1999-01-01', 0, '2025-11-18');
INSERT INTO `Socio` VALUES ('42356432J', 'Alberto', 'Ruiz López', 'Calle Republica Argentina n12', '1999-05-05', 0, '2025-12-13');
INSERT INTO `Socio` VALUES ('45678934X', 'Prueba', 'PATCH', 'Calle PW', '2006-12-25', 1, '2025-11-26');
INSERT INTO `Socio` VALUES ('54290546C', 'Alejandro', 'Gómez', 'Calle del Oro, 10, Málaga', '2004-10-14', 1, '2023-11-12');
INSERT INTO `Socio` VALUES ('66667777A', 'prueba', '2', NULL, '2000-12-24', 0, '2025-12-04');
INSERT INTO `Socio` VALUES ('87654321B', 'prueba', 'prueba1', 'calle malan', '2025-11-05', 0, '2025-11-27');
INSERT INTO `Socio` VALUES ('87654321X', 'Mohssin', 'Bassat Sidki', 'Calle Manuel Altolaguirre, Málaga', '2004-05-18', 0, '2025-11-12');
INSERT INTO `Socio` VALUES ('87658371X', 'Maria', 'Lopez', 'Calle Africa 1', '2004-07-10', 0, '2025-11-18');
INSERT INTO `Socio` VALUES ('98765432F', 'Carlos', 'Conchillo Martinez', 'Calle Trabajo n41', '2006-12-05', 0, '2025-12-13');

-- 
-- Filtros para las tablas descargadas (dump)
-- 

-- 
-- Filtros para la tabla `Alquiler`
-- 
ALTER TABLE `Alquiler`
  ADD CONSTRAINT `fk_alquiler_embarcacion` FOREIGN KEY (`matriculaEmbarcacion`) REFERENCES `Embarcacion` (`matricula`) ON DELETE CASCADE ON UPDATE CASCADE,
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
  ADD CONSTRAINT `fk_inscripcion_familia` FOREIGN KEY (`familiaId`) REFERENCES `Familia` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_inscripcion_titular` FOREIGN KEY (`dniTitular`) REFERENCES `Socio` (`dni`) ON UPDATE CASCADE;

-- 
-- Filtros para la tabla `Reserva`
-- 
ALTER TABLE `Reserva`
  ADD CONSTRAINT `fk_reserva_embarcacion` FOREIGN KEY (`matriculaEmbarcacion`) REFERENCES `Embarcacion` (`matricula`) ON DELETE CASCADE ON UPDATE CASCADE;
