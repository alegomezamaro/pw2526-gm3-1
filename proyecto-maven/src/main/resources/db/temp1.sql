/*
 * SCRIPT INSERTAR VALORES FICTIOS
 */

-- BORRADO DE TABLAS INICIAL
TRUNCATE MATERIAL;
TRUNCATE PISTA;
TRUNCATE BONO;
TRUNCATE RESERVA;
TRUNCATE USUARIO;

-- TABLA PISTAS
INSERT INTO PISTA (nombre, disponible, isExterior, TamanoPista, maxJugadores, pelotas, conos, canastas)
VALUES
    ('Pista1', true, false, 'ADULTOS', 10, 1, 0, 0),
    ('Pista2', true, true, 'ADULTOS', 10, 0, 1, 0),
    ('Pista3', false, false, 'ADULTOS', 10, 0, 0, 0),
    ('Pista4', false, true, 'ADULTOS', 10, 0, 0, 0),
    ('Pista5', true, false, 'MINIBASKET', 10, 0, 1, 0),
    ('Pista6', true, true, 'MINIBASKET', 10, 0, 0, 1),
    ('Pista7', false, false, 'MINIBASKET', 10, 1, 0, 0),
    ('Pista8', false, true, 'MINIBASKET', 10, 0, 1, 0),
    ('Pista9', true, false, 'TRES_VS_TRES', 6, 0, 0, 0),
    ('Pista10', true, true, 'TRES_VS_TRES', 6, 0, 0, 1),
    ('Pista11', true, false, 'TRES_VS_TRES', 6, 0, 1, 0),
    ('Pista12', false, true, 'TRES_VS_TRES', 6, 0, 0, 0);

-- TABLA MATERIAL
INSERT INTO MATERIAL (estadoMaterial, usoMaterial, tipoMaterial, pista_asociada)
VALUES
    ('DISPONIBLE', true, 'PELOTAS', 'Pista1'),
    ('DISPONIBLE', true, 'CONOS', 'Pista2'),
    ('DISPONIBLE', true, 'CONOS', 'Pista5'),
    ('DISPONIBLE', true, 'CANASTAS', 'Pista6'),
    ('DISPONIBLE', true, 'PELOTAS', 'Pista7'),
    ('DISPONIBLE', true, 'CONOS', 'Pista8'),
    ('DISPONIBLE', true, 'CANASTAS', 'Pista10'),
    ('DISPONIBLE', true, 'CONOS', 'Pista11');

-- TABLA USUARIO
INSERT INTO USUARIO (correo, password, administrador, nombre, apellidos, fecha_Nacimiento, fecha_Inscripcion)
VALUES
    ('admin@admin.es', 'admin', 1, 'admin', 'admin', '01/03/2004', '1/1/1900'),
    ('pablo@uco.es', 'pablo', 0, 'Pablo', 'Estepa Alcaide', '03/03/2004', '01/01/1900'),
    ('asun@uco.es', 'asun', 0, 'Asun',  'Capilla Muñoz', '03/04/2004', '01/01/2020'),
    ('lolalolailo@gmail.com', 'lola', 0, 'Lola', 'Ortiz Solis', '11/06/2004', '22/12/2024');

-- TABLA BONO
INSERT INTO BONO (numero_sesiones, usuario, tipo_Bono, fecha_caducidad)
VALUES
    ('5', 'asun@uco.es', 'ADULTOS', '2026-06-11'),
    ('5', 'lolalolailo@gmail.com', 'FAMILIAR', '2026-12-04');

-- TABLA RESERVA
INSERT INTO RESERVA (usuario, id_Bono, numero_sesion, modalidad_reserva, tipo_Reserva, participantes_infantiles, participantes_adultos, fecha, duracion, descuento, precio, pista)
VALUES
    ('asun@uco.es', 1, 1, 'BONO', 'ADULTOS', 0, 2, '2025-11-16 10:00:00', 60, 5, 19, 'Pista1'),
    ('asun@uco.es', 1, 2, 'BONO', 'ADULTOS', 0, 1, '2025-11-17 10:00:00', 120, 5, 38, 'Pista1'),
    ('asun@uco.es', 1, 3, 'BONO', 'ADULTOS', 0, 3, '2025-11-18 10:00:00', 60, 5, 19, 'Pista1'),
    ('asun@uco.es', 1, 4, 'BONO', 'ADULTOS', 0, 4, '2025-11-19 10:00:00', 120, 5, 38, 'Pista1'),
    ('asun@uco.es', 1, 5, 'BONO', 'ADULTOS', 0, 5, '2025-11-20 10:00:00', 60, 5, 19, 'Pista1'),
    ('asun@uco.es', NULL, 0, 'INDIVIDUAL', 'INFANTIL', 2, 0, '2020-09-17 12:00:00', 120, 0, 40, 'Pista5'),
    ('asun@uco.es', NULL, 0, 'INDIVIDUAL', 'INFANTIL', 2, 0, '2021-03-20 12:00:00', 60, 0, 20, 'Pista5'),
    ('asun@uco.es', NULL, 0, 'INDIVIDUAL', 'INFANTIL', 2, 0, '2021-04-01 12:00:00', 90, 0, 30, 'Pista5'),
    ('asun@uco.es', NULL, 0, 'INDIVIDUAL', 'INFANTIL', 2, 0, '2022-05-06 12:00:00', 120, 10, 36, 'Pista5'),
    ('asun@uco.es', NULL, 0, 'INDIVIDUAL', 'INFANTIL', 2, 0, '2023-09-19 12:00:00', 60, 10, 18, 'Pista5'),
    ('lolalolailo@gmail.com', 2, 1, 'BONO', 'FAMILIAR', 1, 2, '2025-10-18 14:00:00', 120, 5, 38, 'Pista11'),
    ('lolalolailo@gmail.com', 2, 2, 'BONO', 'FAMILIAR', 2, 2, '2025-10-19 14:00:00', 60, 5, 19, 'Pista11'),
    ('lolalolailo@gmail.com', 2, 3, 'BONO', 'FAMILIAR', 3, 2, '2025-10-20 14:00:00', 120, 5, 38, 'Pista11'),
    ('lolalolailo@gmail.com', 2, 4, 'BONO', 'FAMILIAR', 4, 2, '2025-10-21 14:00:00', 60, 5, 19, 'Pista11'),
    ('lolalolailo@gmail.com', 2, 5, 'BONO', 'FAMILIAR', 1, 3, '2025-10-22 14:00:00', 60, 5, 19, 'Pista11');