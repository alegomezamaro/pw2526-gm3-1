-- =========================================================
-- Club Náutico - Script de creación de base de datos
-- Solo creación de tablas e inserción de datos
-- =========================================================

-- Crear base de datos
-- DROP DATABASE IF EXISTS club_nautico;
-- CREATE DATABASE club_nautico
--   DEFAULT CHARACTER SET utf8
--   DEFAULT COLLATE utf8_unicode_ci;
-- USE club_nautico;

-- =========================================================
-- Tabla: Socio
-- =========================================================
DROP TABLE IF EXISTS Socio;
CREATE TABLE Socio (
  dni INT NOT NULL,
  name VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
  surname VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
  address VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  birthDate DATE DEFAULT NULL,
  inscriptionDate DATE DEFAULT NULL,
  patronEmbarcacion TINYINT(1) NOT NULL DEFAULT 0,
  inscriptionId INT DEFAULT NULL,
  PRIMARY KEY (dni)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO Socio VALUES
(12345678, 'Ana', 'López García', 'Av. del Puerto 12, Cádiz', '1999-03-21', '2023-06-15', 1, NULL),
(87654321, 'Javier', 'Martín Ruiz', 'C/ Cruz Conde 7, Córdoba', '2001-11-02', '2024-01-10', 0, NULL),
(70890123, 'Lucía', 'Serrano Torres', 'Gran Vía 101, Madrid', '2000-08-14', '2022-09-01', 1, NULL),
(22446688, 'Carlos', 'Castillo Romero', 'Rambla Catalunya 45, Barcelona', '1998-05-05', '2021-04-20', 0, NULL),
(33445566, 'Beatriz', 'Navarro Fernández', 'C/ Larios 3, Málaga', '2002-12-30', '2024-07-01', 0, NULL),
(44556677, 'Sergio', 'Pérez Domínguez', 'Av. de la Constitución 8, Sevilla', '1997-01-17', '2020-03-12', 1, NULL);

-- =========================================================
-- Tabla: Inscripcion
-- =========================================================
DROP TABLE IF EXISTS Inscripcion;
CREATE TABLE Inscripcion (
  id INT NOT NULL AUTO_INCREMENT,
  tipo ENUM('INDIVIDUAL','FAMILIAR') NOT NULL,
  cuota DECIMAL(10,2) NOT NULL,
  fecha_creacion DATE NOT NULL,
  titular_dni INT NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO Inscripcion (tipo, cuota, fecha_creacion, titular_dni) VALUES
('INDIVIDUAL', 300.00, '2023-06-15', 12345678),
('FAMILIAR',   650.00, '2024-01-10', 87654321);

-- =========================================================
-- Tabla: Familia
-- =========================================================
DROP TABLE IF EXISTS Familia;
CREATE TABLE Familia (
  inscripcion_id INT NOT NULL,
  socio_dni INT NOT NULL,
  rol ENUM('TITULAR','ADULTO','HIJO') NOT NULL,
  PRIMARY KEY (inscripcion_id, socio_dni)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO Familia VALUES
(1, 12345678, 'TITULAR'),
(2, 87654321, 'TITULAR'),
(2, 33445566, 'ADULTO'),
(2, 70890123, 'HIJO');

-- =========================================================
-- Tabla: Patron
-- =========================================================
DROP TABLE IF EXISTS Patron;
CREATE TABLE Patron (
  dni INT NOT NULL,
  name VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
  surname VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
  birthDate DATE DEFAULT NULL,
  fechaExpedicion DATE DEFAULT NULL,
  PRIMARY KEY (dni)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO Patron VALUES
(11112222, 'María', 'Gómez', '1985-04-11', '2010-06-01'),
(22223333, 'Pedro', 'Santos', '1979-12-09', '2005-09-15');

-- =========================================================
-- Tabla: Embarcacion
-- =========================================================
DROP TABLE IF EXISTS Embarcacion;
CREATE TABLE Embarcacion (
  matricula VARCHAR(20) COLLATE utf8_unicode_ci NOT NULL,
  nombre VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL,
  tipo ENUM('VELERO','YATE','LANCHA','CATAMARAN','MOTO_ACUATICA') NOT NULL,
  plazas INT NOT NULL,
  eslora_m DECIMAL(6,2) DEFAULT NULL,
  manga_m DECIMAL(6,2) DEFAULT NULL,
  calado_m DECIMAL(6,2) DEFAULT NULL,
  patron_dni INT DEFAULT NULL,
  PRIMARY KEY (matricula)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO Embarcacion VALUES
('CAD-0001', 'Luna de Mar', 'VELERO', 8, 10.50, 3.50, 1.80, 11112222),
('CAD-0002', 'Brisa Azul', 'LANCHA', 6, 7.20, 2.50, 0.90, 22223333),
('CAD-0003', 'Costa Dorada', 'YATE', 12, 14.00, 4.20, 2.10, NULL);

-- =========================================================
-- Tabla: Alquiler
-- =========================================================
DROP TABLE IF EXISTS Alquiler;
CREATE TABLE Alquiler (
  id BIGINT NOT NULL AUTO_INCREMENT,
  socio_titular_dni INT NOT NULL,
  matricula VARCHAR(20) COLLATE utf8_unicode_ci NOT NULL,
  fecha_inicio DATE NOT NULL,
  fecha_fin DATE NOT NULL,
  plazas_solicitadas INT NOT NULL,
  num_personas INT NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO Alquiler (socio_titular_dni, matricula, fecha_inicio, fecha_fin, plazas_solicitadas, num_personas) VALUES
(12345678, 'CAD-0001', '2025-05-05', '2025-05-11', 6, 4);

-- =========================================================
-- Tabla: Reserva
-- =========================================================
DROP TABLE IF EXISTS Reserva;
CREATE TABLE Reserva (
  id BIGINT NOT NULL AUTO_INCREMENT,
  socio_dni INT NOT NULL,
  matricula VARCHAR(20) COLLATE utf8_unicode_ci NOT NULL,
  fecha_evento DATE NOT NULL,
  plazas_solicitadas INT NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO Reserva (socio_dni, matricula, fecha_evento, plazas_solicitadas) VALUES
(87654321, 'CAD-0002', '2025-06-20', 5);
