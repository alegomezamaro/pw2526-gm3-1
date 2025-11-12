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
-- =========================================================
-- =========================================================
-- Tabla: Socio
-- Campos: dni (PK), nombre, apellidos, direccion, fechaNacimiento, patronEmbarcacion
-- =========================================================
CREATE TABLE Socio (
  dni                 VARCHAR(20)   NOT NULL,
  nombre              VARCHAR(120)  NOT NULL,
  apellidos           VARCHAR(160)  NOT NULL,
  direccion           VARCHAR(255),
  fechaNacimiento     DATE          NOT NULL,
  patronEmbarcacion   TINYINT(1)    NOT NULL DEFAULT 0,
  fechaAlta           DATE          NOT NULL,
  PRIMARY KEY (dni)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- =========================================================
-- Tabla: Familia (ACTUALIZADA)
-- Campos: id (PK), dniTitular (FK a Socio.dni), numAdultos, numNiños
-- ¡OJO! Se respeta la tilde en el nombre de columna `numNiños`.
-- =========================================================
CREATE TABLE Familia (
  id          INT          NOT NULL AUTO_INCREMENT,
  dniTitular  VARCHAR(20)  NOT NULL,
  numAdultos  INT          NOT NULL,
  `numNiños`  INT          NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_familia_titular FOREIGN KEY (dniTitular) REFERENCES Socio(dni)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- =========================================================
-- Tabla: Inscripcion
-- Campos: id (PK), tipoCuota (ENUM), cuotaAnual, fechaInscripcion, dniTitular (FK Socio), familiaId (FK Familia)
-- =========================================================
CREATE TABLE Inscripcion (
  id                 INT          NOT NULL AUTO_INCREMENT,
  tipoCuota          ENUM('INDIVIDUAL','FAMILIAR') NOT NULL,
  cuotaAnual         INT          NOT NULL,
  fechaInscripcion   DATE         NOT NULL,
  dniTitular         VARCHAR(20)  NOT NULL,
  familiaId          INT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_inscripcion_titular FOREIGN KEY (dniTitular) REFERENCES Socio(dni)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_inscripcion_familia FOREIGN KEY (familiaId) REFERENCES Familia(id)
    ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- =========================================================
-- Tabla: Patron
-- Campos: id (PK), dni (UNIQUE), nombre, apellidos, fechaNacimiento, fechaTituloPatron
-- =========================================================
CREATE TABLE Patron (
  id                 INT          NOT NULL,
  dni                VARCHAR(20)  NOT NULL,
  nombre             VARCHAR(120) NOT NULL,
  apellidos          VARCHAR(160) NOT NULL,
  fechaNacimiento    DATE         NOT NULL,
  fechaTituloPatron  DATE         NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_patron_dni (dni)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- =========================================================
-- Tabla: Embarcacion
-- Campos: matricula (PK), nombre (UNIQUE), tipo (ENUM), plazas, dimensiones, patronAsignado (FK Patron.id)
-- =========================================================
CREATE TABLE Embarcacion (
  matricula        VARCHAR(32)   NOT NULL,
  nombre           VARCHAR(120)  NOT NULL,
  tipo             ENUM('VELERO','YATE','CATAMARAN','LANCHA') NOT NULL,
  plazas           INT           NOT NULL,
  dimensiones      VARCHAR(80)   NOT NULL,
  patronAsignado   INT NULL,
  PRIMARY KEY (matricula),
  UNIQUE KEY uk_embarcacion_nombre (nombre),
  CONSTRAINT fk_embarcacion_patron FOREIGN KEY (patronAsignado) REFERENCES Patron(id)
    ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- =========================================================
-- Tabla: Alquiler
-- Campos: id (PK), matriculaEmbarcacion (FK), dniTitular (FK), fechaInicio, fechaFin, plazasSolicitadas, precioTotal
-- =========================================================
CREATE TABLE Alquiler (
  id                     INT          NOT NULL AUTO_INCREMENT,
  matriculaEmbarcacion   VARCHAR(32)  NOT NULL,
  dniTitular             VARCHAR(20)  NOT NULL,
  fechaInicio            DATE         NOT NULL,
  fechaFin               DATE         NOT NULL,
  plazasSolicitadas      INT          NOT NULL,
  precioTotal            DOUBLE       NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_alquiler_embarcacion FOREIGN KEY (matriculaEmbarcacion) REFERENCES Embarcacion(matricula)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_alquiler_titular FOREIGN KEY (dniTitular) REFERENCES Socio(dni)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- =========================================================
-- Tabla: Reserva
-- Campos: id (PK), matriculaEmbarcacion (FK), plazasReserva, fechaReserva, precioReserva
-- =========================================================
CREATE TABLE Reserva (
  id                   INT          NOT NULL,
  matriculaEmbarcacion VARCHAR(32)  NOT NULL,
  plazasReserva        INT          NOT NULL,
  fechaReserva         DATE         NOT NULL,
  precioReserva        DOUBLE       NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_reserva_embarcacion FOREIGN KEY (matriculaEmbarcacion) REFERENCES Embarcacion(matricula)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- =========================================================
-- INSERTS: 2 instancias por tabla, respetando FKs
-- =========================================================

INSERT INTO Socio (dni, nombre, apellidos, direccion, fechaNacimiento, patronEmbarcacion) VALUES
('11111111A','Ana','López García','Av. del Puerto 12, Cádiz','1999-03-21',1),
('22222222B','Javier','Martín Ruiz','C/ Cruz Conde 7, Córdoba','2001-11-02',0);

-- Familias ejemplo (titulares son socios existentes)
INSERT INTO Familia (dniTitular, numAdultos, `numNiños`) VALUES
('11111111A', 2, 2),
('22222222B', 2, 0);

INSERT INTO Patron (id, dni, nombre, apellidos, fechaNacimiento, fechaTituloPatron) VALUES
(1,'33333333C','Lucía','Serrano Torres','1990-08-10','2015-06-01'),
(2,'44444444D','Carlos','Navarro Páez','1987-01-22','2012-04-15');

INSERT INTO Embarcacion (matricula, nombre, tipo, plazas, dimensiones, patronAsignado) VALUES
('ABC-123','Tramontana','VELERO',8,'10x3m',1),
('XYZ-789','Levante','LANCHA',6,'7x2m',NULL);

INSERT INTO Inscripcion (tipoCuota, cuotaAnual, fechaInscripcion, dniTitular, familiaId) VALUES
('INDIVIDUAL',300,'2023-06-15','11111111A',NULL),
('FAMILIAR',650,'2024-01-10','22222222B',2);

INSERT INTO Alquiler (matriculaEmbarcacion, dniTitular, fechaInicio, fechaFin, plazasSolicitadas, precioTotal) VALUES
('ABC-123','11111111A','2025-05-10','2025-05-17',4, 640.00),
('XYZ-789','11111111A','2025-10-01','2025-10-03',3, 180.00);

INSERT INTO Reserva (id, matriculaEmbarcacion, plazasReserva, fechaReserva, precioReserva) VALUES
(1,'ABC-123',5,'2025-06-20',200.00),
(2,'XYZ-789',4,'2025-07-05',160.00);

-- FIN
