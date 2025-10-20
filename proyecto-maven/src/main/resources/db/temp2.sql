/*
 * SCRIPT CREADOR ESTRUCTURA BBDD
 *
 */

/*
 * BORRADO DE TABLAS
 */

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS MATERIAL, PISTA, USUARIO, RESERVA, BONO;
SET FOREIGN_KEY_CHECKS = 1;

/*
 * CREACION DE TABLAS
 */
/*
 * ATRIBUTOS
 * 	- nombre: PRIMARY KEY. Nombre que identifica a la pista.
 * 	- disponible: Boolean para saber si la pista se encuentra disponible (true) o no disponible (false). Atributo de tipo NOT NULL.
 * 	- isExterior: Boolean para saber si la pista es exterior (true) o interior (false). Atributo de tipo NOT NULL.
 * 	- TamanoPista: enum para conocer el tipo de pista (MINIBASKET, ADULTOS, TRES_VS_TRES).
 *  - maxJugadores: Entero que determina el máximo de jugadores de una pista. Atributo de tipo NOT NULL.
 *  - pelotas: Entero que determina el número de pelotas de una pista. Atributo de tipo NOT NULL y DEFAULT 0
 *  - conos: Entero que determina el número de conos de una pista. Atributo de tipo NOT NULL y DEFAULT 0
 *  - canastas: Entero que determina el número de canastas de una pista. Atributo de tipo NOT NULL y DEFAULT 0
 *
 */
-- TABLA PISTAS

CREATE TABLE PISTA(
					  id int(3) PRIMARY KEY AUTO_INCREMENT,
                      nombre varchar(64) CHARACTER SET utf8 NOT NULL UNIQUE ,
                      disponible boolean NOT NULL,
                      isExterior boolean NOT NULL,
                      TamanoPista enum('MINIBASKET','ADULTOS','TRES_VS_TRES') NOT NULL,
                      maxJugadores int(3) NOT NULL,
                      pelotas int(2) UNSIGNED NOT NULL DEFAULT 0,
                      conos int(2) UNSIGNED NOT NULL DEFAULT 0,
                      canastas int(1) UNSIGNED NOT NULL DEFAULT 0,
                      
                      CONSTRAINT ck_pelotas CHECK pelotas <= 12,
                      CONSTRAINT ck_conos CHECK conos <= 20,
                      CONSTRAINT ck_canastas CHECK canastas <= 2

)ENGINE=InnoDB;

-- TABLA MATERIAL
/*
 * ATRIBUTOS
 * 	- id: PRIMARY KEY. Numero que identifica un material. Variable de tipo autincrementable.
 *  - estadoMaterial: Enum que indica el estado del material. Atributo de tipo NOT NULL
 * 	- tipoMaterial: Boolean para saber si el material es de exterior (true) o interior (false). Atributo de tipo NOT NULL
 * 	- pista_asociada: String que contiene el nombre de la pista a la que esta asociado al material.
 *
 * CONSTRAINTS
 * - fk_material_pistas: Foreign key que relaciona el atributo pista_asociada con la primary key nombre de la tabla PISTA. Solo se puede asociar un material a una pista existente
 */
CREATE TABLE MATERIAL(
                         id int(3) PRIMARY KEY AUTO_INCREMENT,
                         estadoMaterial enum('DISPONIBLE','RESERVADO','MAL_ESTADO') NOT NULL,
                         usoMaterial boolean NOT NULL,
                         tipoMaterial enum('PELOTAS', 'CANASTAS', 'CONOS') NOT NULL,
                         pista_asociada varchar(16) CHARACTER SET utf8,

                         CONSTRAINT fk_mats_pistas FOREIGN KEY (pista_asociada) references PISTA(nombre)
)ENGINE=InnoDB;


-- TABLA USUARIOS
/*
 * ATRIBUTOS
 * - correo: PRIMARY KEY. String que indica el correo de una persona
 * - nombre: String que contiene el nombre de una persona. Atributo de tipo NOT NULL.
 * - apellidos: String que contiene los apellidos de una persona. Artributo de tipo NOT NULL.
 * - fecha_Nacimiento: String (formato dd/mm/yyyy) que indica la fecha de nacimiento de una persona. Atributo tipo NOT NULL.
 * - fecha_Inscropcion: String (formato dd/mm/yyyy) que indica la fecha de cuando se hizo la primera reserva. Atributo con valor DEFAULT '1/1/1900'
 */
CREATE TABLE USUARIO(
						id int(3) PRIMARY KEY AUTO_INCREMENT,
                        correo varchar(64) CHARACTER SET utf8 NOT NULL UNIQUE,
                        password varchar(16) CHARACTER SET utf8 NOT NULL,
    					administrador boolean NOT NULL,
                        nombre varchar(16) CHARACTER SET utf8 NOT NULL,
                        apellidos varchar(16) CHARACTER SET utf8 NOT NULL,
                        fecha_Nacimiento varchar(16) CHARACTER SET utf8 NOT NULL,
                        fecha_Inscripcion varchar(16) CHARACTER SET utf8 DEFAULT '1/1/1900'

)ENGINE=InnoDB;


-- TABLA BONO
/*
 * ATRIBUTOS
 * - id_Bono: PRIMARY KEY. Entero que indica la id de un bono. Atributo autoincrementable
 * - usuario: String que contiene el correo de un usuario. Atributo de tipo NOT NULL.
 * - tipo_Bono: Enum que determina el tipo de bono creado. Atributo de tipo NOT NULL.
 * - fecha_caducidad: String (formato dd/mm/yyyy) que indica la fecha de caducidad del bono. Atributo tipo NOT NULL.
 *
 * CONSTRAINTS
 * - fk_bono_usuario: Foreign key que relaciona el atributo usuario con la primary key correo de la tabla USUARIO. Solo permitimos usuarios registrados
 */
CREATE TABLE BONO(
                     id_Bono int(3) PRIMARY KEY AUTO_INCREMENT,
                     numero_sesiones int(1) NOT NULL DEFAULT 0,
                     usuario varchar(64) CHARACTER SET utf8 NOT NULL,
                     tipo_Bono enum('INFANTIL','FAMILIAR','ADULTOS') NOT NULL,
                     fecha_caducidad date DEFAULT '1970-1-1',

                     CONSTRAINT fk_bono_usuario foreign key (usuario) references USUARIO(correo)
)ENGINE=InnoDB;

-- TABLA RESERVAS
/*
 * ATRIBUTOS
 * - id_Reserva: PRIMARY KEY. Entero que indica el id de una reserva. Variable autoincrementable.
 * - usuario: String que contiene el correo de un ususario. Atributo de tipo NOT NULL.
 * - id_Bono: Entero que contiene el id de un bono.
 * - modalidad_reserva: Enum que indica la modalidad de la reserva. Atributo de tipo NOT NULL
 * - tipo_reserva: Enum que indica el publico de la reserva. Atributo de tipo NOT NULL
 * - participantes_infantiles: Entero que indica el numero de participantes infantiles
 * - participantes_adultos: Entero que indica el numero de participantes adultos
 * - fecha: Date que contiene la fecha de la reserva. Atributo tipo NOT NULL
 * - duracion: Entero que indica la duracion de la reserva. Atributo NOT NULL
 * - descuento: Flotante indicando el descuento segun antiguedad o si es bono. Atributo NOT NULL.
 * - precio: Flotante qur contiene el precio de la reserva. Atributo NOT NULL
 * - pista: String que indica la pista de la reserva. Atributo NOT NULL.
 *
 * CONSTRAINTS
 * - fk_reserva_usuario: Foreign key que relaciona el atributo usuario con la PK correo de la tabla USUARIO. Para solo permitir reservas de usuarios registrados.
 * - fk_resera_pista: Foreign key que relaciona el atributo pista con la PK nombre de la tabla PISTA. Solo se pueden añadir pistas que existan.
 * - fk_reserva_bono: Foreign key que realciona el atributo id_Bono con la PK id_Bono de la table BONO. Solo se puede relacionar una reserva con un bono si este existe
 */

CREATE TABLE RESERVA(
                        id_Reserva int(8) PRIMARY KEY AUTO_INCREMENT,
                        usuario varchar(64) CHARACTER SET utf8 NOT NULL,
                        id_Bono int(8),
                        numero_sesion int(1),
                        modalidad_reserva enum('INDIVIDUAL','BONO') NOT NULL,
                        tipo_Reserva enum('INFANTIL','FAMILIAR','ADULTOS') NOT NULL,
                        participantes_infantiles int(3),
                        participantes_adultos int(3),
                        fecha datetime NOT NULL,
                        duracion int(3) NOT NULL,
                        descuento float NOT NULL,
                        precio float NOT NULL,
                        pista varchar(16) CHARACTER SET utf8 NOT NULL,

                        CONSTRAINT fk_reserva_usuario foreign key (usuario) references USUARIO(correo),
                        CONSTRAINT fk_reserva_pista foreign key (pista) references PISTA(nombre),
                        CONSTRAINT fk_reserva_bono foreign key (id_Bono) references BONO(id_Bono)
)ENGINE=InnoDB;