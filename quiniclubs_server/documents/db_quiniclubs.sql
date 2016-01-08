# Base de datos
# Creado en 19/04/2015
# @author: kamarena

CREATE DATABASE IF NOT EXISTS db_quiniclubs 
CHARACTER SET utf8 COLLATE utf8_general_ci;

USE db_quiniclubs;

# Tabla db_usuario
CREATE TABLE IF NOT EXISTS db_usuario (
	id_usuario VARCHAR(50) NOT NULL PRIMARY KEY,
        pass VARCHAR(50) NOT NULL,
	nombre VARCHAR(50),
	apellidos VARCHAR(100),
        email VARCHAR(50),
	fecha_creacion DATE
) ENGINE = INNODB;

# Tabla db_comunidad
CREATE TABLE IF NOT EXISTS db_comunidad (
	id_comunidad INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	nombre VARCHAR(50),
	fecha_creacion DATE,
	id_administrador VARCHAR(50),
	id_gestor VARCHAR(50),
	FOREIGN KEY (id_administrador) 
	REFERENCES db_usuario (id_usuario) 
	ON UPDATE CASCADE ON DELETE SET NULL,
	FOREIGN KEY (id_gestor) 
	REFERENCES db_usuario (id_usuario) 
	ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE = INNODB;

# Tabla db_usuario_comunidad
CREATE TABLE IF NOT EXISTS db_usuario_comunidad (
	id_usuario VARCHAR(50) NOT NULL,
	id_comunidad INT NOT NULL,
	fecha_inicio DATE,
	PRIMARY KEY (id_usuario, id_comunidad),
	FOREIGN KEY (id_usuario) 
	REFERENCES db_usuario (id_usuario) 
	ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (id_comunidad) 
	REFERENCES db_comunidad (id_comunidad) 
	ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = INNODB;

# Tabla db_temporada
CREATE TABLE IF NOT EXISTS db_temporada (
	id_temporada INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	nombre VARCHAR(50), 
	fecha_inicio DATE, 
	fecha_final DATE
) ENGINE = INNODB;

# Tabla db_jornada
CREATE TABLE IF NOT EXISTS db_jornada (
	id_jornada INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	nombre VARCHAR(50), 
	fecha_inicio DATE, 
	fecha_final DATE, 
	id_temporada INT, 
	FOREIGN KEY (id_temporada) 
	REFERENCES db_temporada(id_temporada) 
	ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = INNODB;

# Tabla db_equipo
CREATE TABLE IF NOT EXISTS db_equipo (
	id_equipo INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	nombre VARCHAR(50),
	division VARCHAR(20)
) ENGINE = INNODB;

# Tabla db_partido
CREATE TABLE IF NOT EXISTS db_partido (
	id_partido INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	nombre VARCHAR(50),
	resultado CHAR(1) NOT NULL,
        fecha_partido DATE NOT NULL,
        numero_partido INT NOT NULL, 
	id_jornada INT NOT NULL, 
        id_equipo1 INT, 
        id_equipo2 INT, 
	FOREIGN KEY (id_jornada) 
	REFERENCES db_jornada (id_jornada) 
	ON UPDATE CASCADE ON DELETE CASCADE, 
        FOREIGN KEY (id_equipo1) 
        REFERENCES db_equipo (id_equipo) 
        ON UPDATE CASCADE ON DELETE SET NULL,
        FOREIGN KEY (id_equipo2) 
        REFERENCES db_equipo (id_equipo) 
        ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE = INNODB;

# Tabla db_pronostico_usuario
CREATE TABLE IF NOT EXISTS db_pronostico_usuario (
	id_partido INT NOT NULL,
	id_usuario VARCHAR(50) NOT NULL,
	apuesta CHAR(1) NOT NULL,
	fecha_confirmacion DATE,
	PRIMARY KEY (id_partido, id_usuario), 
	FOREIGN KEY (id_partido) 
	REFERENCES db_partido (id_partido) 
	ON UPDATE CASCADE ON DELETE CASCADE, 
	FOREIGN KEY (id_usuario) 
	REFERENCES db_usuario (id_usuario) 
	ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = INNODB;

# Tabla db_pronostico_comunidad
CREATE TABLE IF NOT EXISTS db_pronostico_comunidad (
	id_partido INT NOT NULL,
	id_comunidad INT NOT NULL,
	apuesta CHAR(1) NOT NULL,
	fecha_confirmacion DATE,
	PRIMARY KEY (id_partido, id_comunidad), 
	FOREIGN KEY (id_partido) 
	REFERENCES db_partido (id_partido) 
	ON UPDATE CASCADE ON DELETE CASCADE, 
	FOREIGN KEY (id_comunidad) 
	REFERENCES db_comunidad (id_comunidad)
	ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = INNODB;

# Insertar usuarios en la base de datos
INSERT INTO db_usuario 
(id_usuario,pass,nombre,apellidos,email,fecha_creacion) 
VALUES 
("xuspitin","abc123","Vicent","Camarena","xuspitin@kamarena.com","2014-06-17 16:45:37"),
("paquito_online","AMJ84ZLL7ME","Robin","Luna","ipsum@eratneque.net","2015-04-11 01:27:53"),
("jugando23","RWP51WKB2ZA","Bethany","Hall","congue@egestas.net","2016-04-02 16:14:14"),
("al_azar","JAC69IAV3WS","Connor","Martinez","non@Proinegetodio.com","2016-03-30 03:25:12"),
("diga_33","GKW13HZN4XS","Audrey","Lambert","mi.felis@rutrum.org","2016-01-08 07:40:32"),
("unoquellega","QHE58LVL4RX","Tate","Kennedy","malesuada@ametultricies.net","2015-05-27 21:54:01"),
("te_quiero_mar","YOH44HNW7HA","Aubrey","Bryan","malesuada@arcu.org","2015-07-13 09:36:54"),
("joanrules","KPG91BHX6NE","Lucius","Mann","nec.eleifend.non@indolor.net","2014-10-10 23:42:19"),
("alberto22","EDV08UOV2LJ","Winifred","Huber","eget.varius@consectetuer.com","2015-08-26 13:48:02"),
("calimero","PMC43EPF7MS","Ignatius","Rush","fames.ac@quisurnaNunc.ca","2014-06-17 20:28:14"),
("pepelu","MOV44YBC5BT","Nichole","Madden","elit.elit.fermentum@miac.com","2015-02-19 02:55:29"),
("ximotralla","VCG61MCL7IF","Dustin","Justice","est@fermentumvel.com","2015-05-19 03:33:09"),
("losgremlins","CYX06ECE2MH","Aretha","Levine","vulputate.posuere.vulputate@Phasellus.net","2015-06-14 11:40:02"),
("ete_12","JZI28RRS0IP","Quail","Cook","egestas.Aliquam@placeratvelitQuisque.org","2015-03-21 18:54:29"),
("vikingossucks","TYJ20AZV9WC","Burton","Mosley","Etiam@vehicularisus.org","2015-08-08 18:00:45"),
("duerme.zzzz","LPG97ZGO5DD","Zorita","Palmer","Praesent@nonnisi.net","2016-04-10 12:09:04"),
("bibalabida","TFF05SBC7QS","Aurelia","Sheppard","Praesent.luctus.Curabitur@vestibulumMauris.co.uk","2014-10-02 01:35:30"),
("moderfaca","GKM51TSB2QS","Cain","Roy","elementum.lorem@atnisiCum.org","2016-01-11 12:59:18"),
("IloveJoan","IOD69JSD9AE","Elijah","Eaton","ultricies.ligula@iaculislacus.org","2014-06-12 08:22:15"),
("maristhebest","WOX84HOK3ZJ","Oleg","Jarvis","sem.eget.massa@sapien.org","2014-10-17 14:12:19");

# Insertar comunidades en la base de datos
INSERT INTO db_comunidad 
(id_comunidad,nombre,fecha_creacion,id_administrador,id_gestor) 
VALUES 
(1,"Viejas Glórias","2015-11-11 03:43:56","xuspitin","joanrules"),
(2,"Ganadores","2015-11-23 16:30:18","IloveJoan","maristhebest"),
(3,"Ludópatas","2016-03-17 01:08:32","bibalabida","jugando23");

# Insertar datos en tabla db_usuario_comunidad
INSERT INTO db_usuario_comunidad 
(id_usuario,id_comunidad,fecha_inicio) 
VALUES 
("xuspitin",3,"2015-11-16 04:19:58"),
("paquito_online",1,"2014-05-18 13:40:51"),
("jugando23",1,"2015-06-20 07:49:53"),
("al_azar",2,"2014-10-24 05:08:44"),
("diga_33",2,"2014-06-11 06:38:24"),
("unoquellega",1,"2016-02-04 12:37:29"),
("te_quiero_mar",2,"2015-05-15 18:40:09"),
("joanrules",3,"2016-03-04 17:28:56"),
("alberto22",2,"2016-03-14 07:45:00"),
("calimero",2,"2015-08-19 07:38:45"),
("pepelu",2,"2014-07-15 02:58:14"),
("ximotralla",3,"2014-08-25 21:41:50"),
("losgremlins",3,"2015-10-09 06:51:38"),
("ete_12",1,"2015-09-13 14:27:13"),
("vikingossucks",2,"2015-06-02 09:35:55"),
("duerme.zzzz",3,"2015-08-09 10:38:33"),
("bibalabida",1,"2015-03-19 00:29:21"),
("moderfaca",2,"2015-10-26 18:27:42"),
("IloveJoan",1,"2016-04-18 09:25:41"),
("maristhebest",3,"2014-09-08 05:16:39");

# Insertar datos en tabla db_temporada
INSERT INTO db_temporada 
(id_temporada,nombre, fecha_inicio, fecha_final) 
VALUES 
(1,"Temporada 2014-2015", "2014-08-27","2015-06-07");

# Insertar datos en tabla db_jornada
INSERT INTO db_jornada 
(id_jornada,nombre, fecha_inicio, fecha_final, id_temporada) 
VALUES 
(1,"Jornada 1", "2014-10-21", "2014-10-24", 1);

# Insertar datos en tabla db_equipo
INSERT INTO db_equipo 
(id_equipo,nombre, division) 
VALUES 
(1,"Málaga", "1"),
(2,"Sevilla", "1"),
(3,"Granada", "1"),
(4,"Almería", "1"),
(5,"Eibar", "1"),
(6,"Celta", "1"),
(7,"Barcelona", "1"),
(8,"Levante", "1"),
(9,"Real Madrid", "1"),
(10,"Everton", "Inglaterra_1"),
(11,"Tottemham", "Inglaterra_1"),
(12,"Hull City", "Inglaterra_1"),
(13,"Sunderland", "Inglaterra_1"),
(14,"Manchester City", "Inglaterra_1"),
(15,"Rayo Vallecano", "1"),
(16,"Athletic", "1"),
(17,"Valencia", "1"),
(18,"Deportivo", "1"),
(19,"Espanyol", "1"),
(20,"Real Sociedad", "1"),
(21,"Getafe", "1"),
(22,"Elche", "1"),
(23,"Villarreal", "1"),
(24,"Córdoba", "1"),
(25,"Arsenal", "Inglaterra_1"),
(26,"Queens PR", "Inglaterra_1"),
(27,"Stoke City", "Inglaterra_1"),
(28,"Manchester United", "Inglaterra_1"),
(29,"Liverpool", "Inglaterra_1"),
(30,"Atlético", "1");

# Insertar datos en tabla db_partido
INSERT INTO db_partido 
(id_partido,nombre, resultado, id_jornada, fecha_partido,id_equipo1, id_equipo2) 
VALUES 
(1,"Málaga - Athletic", "1", 1, "2014-08-23", 1, 2), 
(2,"Sevilla - Valencia", "X", 1, "2014-08-23", 3, 4), 
(3,"Granada - Deportivo", "1", 1, "2014-08-23", 5, 6), 
(4,"Almería - Espanyol", "X", 1, "2014-08-23", 7 ,8), 
(5,"Eibar - Real Sociedad", "1", 1, "2014-08-23",9,10), 
(6,"Celta - Getafe", "1", 1,"2014-08-23", 11, 12), 
(7,"Barcelona - Elche", "1", 1, "2014-08-23", 13, 14), 
(8,"Levante - Villarreal", "2", 1, "2014-08-23", 15, 16), 
(9,"Real Madrid - Córdoba", "1", 1, "2014-08-23", 17, 18), 
(10,"Everton - Arsenal", "X", 1, "2014-08-23", 19, 20), 
(11,"Tottenham - Queens PR", "1", 1, "2014-08-23", 21, 22), 
(12,"Hull City - Stoke City", "X", 1, "2014-08-23", 23, 24), 
(13,"Sunderland - Manchester United", "X", 1, "2014-08-23", 25, 26), 
(14,"Manchester City - Liverpool", "1", 1, "2014-08-23", 27, 28), 
(15,"Rayo Vallecano - Atlético", "X", 1, "2014-08-23", 29, 30);

# Insertar datos en tabla db_pronostico_usuario
INSERT INTO db_pronostico_usuario 
(id_partido,id_usuario, apuesta, fecha_confirmacion) 
VALUES 
(1,"xuspitin","X","2015-04-26"),
(2,"xuspitin","1","2015-04-26"),
(3,"xuspitin","1","2015-04-26"),
(4,"xuspitin","1","2015-04-26"),
(5,"xuspitin","2","2015-04-26"),
(6,"xuspitin","X","2015-04-26"),
(7,"xuspitin","X","2015-04-26"),
(8,"xuspitin","1","2015-04-26"),
(9,"xuspitin","X","2015-04-26"),
(10,"xuspitin","1","2015-04-26"),
(11,"xuspitin","1","2015-04-26"),
(12,"xuspitin","2","2015-04-26"),
(13,"xuspitin","2","2015-04-26"),
(14,"xuspitin","1","2015-04-26"),
(15,"xuspitin","X","2015-04-26"),
(1,"paquito_online","1","2015-04-26"),
(2,"paquito_online","1","2015-04-26"),
(3,"paquito_online","2","2015-04-26"),
(4,"paquito_online","2","2015-04-26"),
(5,"paquito_online","2","2015-04-26"),
(6,"paquito_online","1","2015-04-26"),
(7,"paquito_online","X","2015-04-26"),
(8,"paquito_online","1","2015-04-26"),
(9,"paquito_online","X","2015-04-26"),
(10,"paquito_online","X","2015-04-26"),
(11,"paquito_online","X","2015-04-26"),
(12,"paquito_online","2","2015-04-26"),
(13,"paquito_online","1","2015-04-26"),
(14,"paquito_online","1","2015-04-26"),
(15,"paquito_online","X","2015-04-26"),
(1,"jugando23","1","2015-04-26"),
(2,"jugando23","2","2015-04-26"),
(3,"jugando23","2","2015-04-26"),
(4,"jugando23","2","2015-04-26"),
(5,"jugando23","1","2015-04-26"),
(6,"jugando23","1","2015-04-26"),
(7,"jugando23","X","2015-04-26"),
(8,"jugando23","1","2015-04-26"),
(9,"jugando23","X","2015-04-26"),
(10,"jugando23","X","2015-04-26"),
(11,"jugando23","X","2015-04-26"),
(12,"jugando23","2","2015-04-26"),
(13,"jugando23","X","2015-04-26"),
(14,"jugando23","1","2015-04-26"),
(15,"jugando23","X","2015-04-26"),
(1,"al_azar","1","2015-04-26"),
(2,"al_azar","X","2015-04-26"),
(3,"al_azar","X","2015-04-26"),
(4,"al_azar","1","2015-04-26"),
(5,"al_azar","2","2015-04-26"),
(6,"al_azar","2","2015-04-26"),
(7,"al_azar","2","2015-04-26"),
(8,"al_azar","1","2015-04-26"),
(9,"al_azar","1","2015-04-26"),
(10,"al_azar","1","2015-04-26"),
(11,"al_azar","2","2015-04-26"),
(12,"al_azar","X","2015-04-26"),
(13,"al_azar","2","2015-04-26"),
(14,"al_azar","1","2015-04-26"),
(15,"al_azar","1","2015-04-26"),
(1,"diga_33","1","2015-04-26"),
(2,"diga_33","2","2015-04-26"),
(3,"diga_33","X","2015-04-26"),
(4,"diga_33","X","2015-04-26"),
(5,"diga_33","2","2015-04-26"),
(6,"diga_33","1","2015-04-26"),
(7,"diga_33","X","2015-04-26"),
(8,"diga_33","X","2015-04-26"),
(9,"diga_33","1","2015-04-26"),
(10,"diga_33","1","2015-04-26"),
(11,"diga_33","1","2015-04-26"),
(12,"diga_33","1","2015-04-26"),
(13,"diga_33","1","2015-04-26"),
(14,"diga_33","X","2015-04-26"),
(15,"diga_33","X","2015-04-26"),
(1,"unoquellega","X","2015-04-26"),
(2,"unoquellega","X","2015-04-26"),
(3,"unoquellega","X","2015-04-26"),
(4,"unoquellega","2","2015-04-26"),
(5,"unoquellega","2","2015-04-26"),
(6,"unoquellega","X","2015-04-26"),
(7,"unoquellega","2","2015-04-26"),
(8,"unoquellega","1","2015-04-26"),
(9,"unoquellega","X","2015-04-26"),
(10,"unoquellega","2","2015-04-26"),
(11,"unoquellega","2","2015-04-26"),
(12,"unoquellega","X","2015-04-26"),
(13,"unoquellega","2","2015-04-26"),
(14,"unoquellega","1","2015-04-26"),
(15,"unoquellega","X","2015-04-26"),
(1,"te_quiero_mar","X","2015-04-26"),
(2,"te_quiero_mar","X","2015-04-26"),
(3,"te_quiero_mar","X","2015-04-26"),
(4,"te_quiero_mar","1","2015-04-26"),
(5,"te_quiero_mar","1","2015-04-26"),
(6,"te_quiero_mar","1","2015-04-26"),
(7,"te_quiero_mar","1","2015-04-26"),
(8,"te_quiero_mar","1","2015-04-26"),
(9,"te_quiero_mar","X","2015-04-26"),
(10,"te_quiero_mar","1","2015-04-26"),
(11,"te_quiero_mar","X","2015-04-26"),
(12,"te_quiero_mar","X","2015-04-26"),
(13,"te_quiero_mar","2","2015-04-26"),
(14,"te_quiero_mar","1","2015-04-26"),
(15,"te_quiero_mar","1","2015-04-26"),
(1,"joanrules","1","2015-04-26"),
(2,"joanrules","1","2015-04-26"),
(3,"joanrules","2","2015-04-26"),
(4,"joanrules","2","2015-04-26"),
(5,"joanrules","1","2015-04-26"),
(6,"joanrules","X","2015-04-26"),
(7,"joanrules","X","2015-04-26"),
(8,"joanrules","1","2015-04-26"),
(9,"joanrules","1","2015-04-26"),
(10,"joanrules","2","2015-04-26"),
(11,"joanrules","2","2015-04-26"),
(12,"joanrules","1","2015-04-26"),
(13,"joanrules","2","2015-04-26"),
(14,"joanrules","1","2015-04-26"),
(15,"joanrules","1","2015-04-26"),
(1,"alberto22","X","2015-04-26"),
(2,"alberto22","X","2015-04-26"),
(3,"alberto22","X","2015-04-26"),
(4,"alberto22","1","2015-04-26"),
(5,"alberto22","2","2015-04-26"),
(6,"alberto22","X","2015-04-26"),
(7,"alberto22","1","2015-04-26"),
(8,"alberto22","1","2015-04-26"),
(9,"alberto22","1","2015-04-26"),
(10,"alberto22","2","2015-04-26"),
(11,"alberto22","2","2015-04-26"),
(12,"alberto22","X","2015-04-26"),
(13,"alberto22","X","2015-04-26"),
(14,"alberto22","1","2015-04-26"),
(15,"alberto22","X","2015-04-26"),
(1,"calimero","1","2015-04-26"),
(2,"calimero","1","2015-04-26"),
(3,"calimero","1","2015-04-26"),
(4,"calimero","2","2015-04-26"),
(5,"calimero","1","2015-04-26"),
(6,"calimero","X","2015-04-26"),
(7,"calimero","X","2015-04-26"),
(8,"calimero","1","2015-04-26"),
(9,"calimero","1","2015-04-26"),
(10,"calimero","2","2015-04-26"),
(11,"calimero","X","2015-04-26"),
(12,"calimero","X","2015-04-26"),
(13,"calimero","X","2015-04-26"),
(14,"calimero","X","2015-04-26"),
(15,"calimero","X","2015-04-26"),
(1,"pepelu","1","2015-04-26"),
(2,"pepelu","1","2015-04-26"),
(3,"pepelu","1","2015-04-26"),
(4,"pepelu","1","2015-04-26"),
(5,"pepelu","2","2015-04-26"),
(6,"pepelu","2","2015-04-26"),
(7,"pepelu","X","2015-04-26"),
(8,"pepelu","1","2015-04-26"),
(9,"pepelu","1","2015-04-26"),
(10,"pepelu","X","2015-04-26"),
(11,"pepelu","1","2015-04-26"),
(12,"pepelu","X","2015-04-26"),
(13,"pepelu","X","2015-04-26"),
(14,"pepelu","1","2015-04-26"),
(15,"pepelu","1","2015-04-26"),
(1,"ximotralla","2","2015-04-26"),
(2,"ximotralla","1","2015-04-26"),
(3,"ximotralla","1","2015-04-26"),
(4,"ximotralla","2","2015-04-26"),
(5,"ximotralla","1","2015-04-26"),
(6,"ximotralla","2","2015-04-26"),
(7,"ximotralla","2","2015-04-26"),
(8,"ximotralla","X","2015-04-26"),
(9,"ximotralla","X","2015-04-26"),
(10,"ximotralla","X","2015-04-26"),
(11,"ximotralla","1","2015-04-26"),
(12,"ximotralla","X","2015-04-26"),
(13,"ximotralla","X","2015-04-26"),
(14,"ximotralla","2","2015-04-26"),
(15,"ximotralla","1","2015-04-26"),
(1,"losgremlins","2","2015-04-26"),
(2,"losgremlins","2","2015-04-26"),
(3,"losgremlins","1","2015-04-26"),
(4,"losgremlins","1","2015-04-26"),
(5,"losgremlins","1","2015-04-26"),
(6,"losgremlins","1","2015-04-26"),
(7,"losgremlins","1","2015-04-26"),
(8,"losgremlins","X","2015-04-26"),
(9,"losgremlins","X","2015-04-26"),
(10,"losgremlins","X","2015-04-26"),
(11,"losgremlins","1","2015-04-26"),
(12,"losgremlins","1","2015-04-26"),
(13,"losgremlins","1","2015-04-26"),
(14,"losgremlins","2","2015-04-26"),
(15,"losgremlins","1","2015-04-26"),
(1,"ete_12","X","2015-04-26"),
(2,"ete_12","1","2015-04-26"),
(3,"ete_12","X","2015-04-26"),
(4,"ete_12","1","2015-04-26"),
(5,"ete_12","X","2015-04-26"),
(6,"ete_12","2","2015-04-26"),
(7,"ete_12","X","2015-04-26"),
(8,"ete_12","X","2015-04-26"),
(9,"ete_12","X","2015-04-26"),
(10,"ete_12","X","2015-04-26"),
(11,"ete_12","1","2015-04-26"),
(12,"ete_12","2","2015-04-26"),
(13,"ete_12","X","2015-04-26"),
(14,"ete_12","1","2015-04-26"),
(15,"ete_12","X","2015-04-26"),
(1,"vikingossucks","2","2015-04-26"),
(2,"vikingossucks","2","2015-04-26"),
(3,"vikingossucks","1","2015-04-26"),
(4,"vikingossucks","1","2015-04-26"),
(5,"vikingossucks","X","2015-04-26"),
(6,"vikingossucks","X","2015-04-26"),
(7,"vikingossucks","X","2015-04-26"),
(8,"vikingossucks","2","2015-04-26"),
(9,"vikingossucks","2","2015-04-26"),
(10,"vikingossucks","2","2015-04-26"),
(11,"vikingossucks","1","2015-04-26"),
(12,"vikingossucks","2","2015-04-26"),
(13,"vikingossucks","2","2015-04-26"),
(14,"vikingossucks","1","2015-04-26"),
(15,"vikingossucks","1","2015-04-26"),
(1,"duerme.zzzz","1","2015-04-26"),
(2,"duerme.zzzz","1","2015-04-26"),
(3,"duerme.zzzz","1","2015-04-26"),
(4,"duerme.zzzz","1","2015-04-26"),
(5,"duerme.zzzz","1","2015-04-26"),
(6,"duerme.zzzz","1","2015-04-26"),
(7,"duerme.zzzz","1","2015-04-26"),
(8,"duerme.zzzz","1","2015-04-26"),
(9,"duerme.zzzz","X","2015-04-26"),
(10,"duerme.zzzz","X","2015-04-26"),
(11,"duerme.zzzz","1","2015-04-26"),
(12,"duerme.zzzz","1","2015-04-26"),
(13,"duerme.zzzz","1","2015-04-26"),
(14,"duerme.zzzz","1","2015-04-26"),
(15,"duerme.zzzz","X","2015-04-26"),
(1,"bibalabida","X","2015-04-26"),
(2,"bibalabida","X","2015-04-26"),
(3,"bibalabida","1","2015-04-26"),
(4,"bibalabida","X","2015-04-26"),
(5,"bibalabida","X","2015-04-26"),
(6,"bibalabida","X","2015-04-26"),
(7,"bibalabida","1","2015-04-26"),
(8,"bibalabida","1","2015-04-26"),
(9,"bibalabida","1","2015-04-26"),
(10,"bibalabida","1","2015-04-26"),
(11,"bibalabida","2","2015-04-26"),
(12,"bibalabida","2","2015-04-26"),
(13,"bibalabida","1","2015-04-26"),
(14,"bibalabida","2","2015-04-26"),
(15,"bibalabida","X","2015-04-26"),
(1,"moderfaca","1","2015-04-26"),
(2,"moderfaca","1","2015-04-26"),
(3,"moderfaca","2","2015-04-26"),
(4,"moderfaca","1","2015-04-26"),
(5,"moderfaca","1","2015-04-26"),
(6,"moderfaca","X","2015-04-26"),
(7,"moderfaca","X","2015-04-26"),
(8,"moderfaca","1","2015-04-26"),
(9,"moderfaca","2","2015-04-26"),
(10,"moderfaca","1","2015-04-26"),
(11,"moderfaca","2","2015-04-26"),
(12,"moderfaca","X","2015-04-26"),
(13,"moderfaca","X","2015-04-26"),
(14,"moderfaca","X","2015-04-26"),
(15,"moderfaca","X","2015-04-26"),
(1,"IloveJoan","1","2015-04-26"),
(2,"IloveJoan","2","2015-04-26"),
(3,"IloveJoan","2","2015-04-26"),
(4,"IloveJoan","1","2015-04-26"),
(5,"IloveJoan","1","2015-04-26"),
(6,"IloveJoan","1","2015-04-26"),
(7,"IloveJoan","2","2015-04-26"),
(8,"IloveJoan","2","2015-04-26"),
(9,"IloveJoan","X","2015-04-26"),
(10,"IloveJoan","1","2015-04-26"),
(11,"IloveJoan","X","2015-04-26"),
(12,"IloveJoan","2","2015-04-26"),
(13,"IloveJoan","X","2015-04-26"),
(14,"IloveJoan","1","2015-04-26"),
(15,"IloveJoan","2","2015-04-26"),
(1,"maristhebest","1","2015-04-26"),
(2,"maristhebest","1","2015-04-26"),
(3,"maristhebest","X","2015-04-26"),
(4,"maristhebest","X","2015-04-26"),
(5,"maristhebest","2","2015-04-26"),
(6,"maristhebest","1","2015-04-26"),
(7,"maristhebest","1","2015-04-26"),
(8,"maristhebest","1","2015-04-26"),
(9,"maristhebest","X","2015-04-26"),
(10,"maristhebest","1","2015-04-26"),
(11,"maristhebest","X","2015-04-26"),
(12,"maristhebest","1","2015-04-26"),
(13,"maristhebest","2","2015-04-26"),
(14,"maristhebest","X","2015-04-26"),
(15,"maristhebest","X","2015-04-26");

# Insertar datos en tabla db_pronostico_comunidad
INSERT INTO db_pronostico_comunidad 
(id_partido,id_comunidad, apuesta, fecha_confirmacion) 
VALUES 
(1,1,"1","2015-04-26"),
(2,1,"1","2015-04-26"),
(3,1,"1","2015-04-26"),
(4,1,"1","2015-04-26"),
(5,1,"1","2015-04-26"),
(6,1,"1","2015-04-26"),
(7,1,"1","2015-04-26"),
(8,1,"1","2015-04-26"),
(9,1,"X","2015-04-26"),
(10,1,"X","2015-04-26"),
(11,1,"1","2015-04-26"),
(12,1,"1","2015-04-26"),
(13,1,"1","2015-04-26"),
(14,1,"1","2015-04-26"),
(15,1,"X","2015-04-26"),
(1,2,"X","2015-04-26"),
(2,2,"X","2015-04-26"),
(3,2,"1","2015-04-26"),
(4,2,"X","2015-04-26"),
(5,2,"X","2015-04-26"),
(6,2,"X","2015-04-26"),
(7,2,"1","2015-04-26"),
(8,2,"1","2015-04-26"),
(9,2,"1","2015-04-26"),
(10,2,"1","2015-04-26"),
(11,2,"2","2015-04-26"),
(12,2,"2","2015-04-26"),
(13,2,"1","2015-04-26"),
(14,2,"2","2015-04-26"),
(15,2,"X","2015-04-26"),
(1,3,"1","2015-04-26"),
(2,3,"1","2015-04-26"),
(3,3,"2","2015-04-26"),
(4,3,"1","2015-04-26"),
(5,3,"1","2015-04-26"),
(6,3,"X","2015-04-26"),
(7,3,"X","2015-04-26"),
(8,3,"1","2015-04-26"),
(9,3,"2","2015-04-26"),
(10,3,"1","2015-04-26"),
(11,3,"2","2015-04-26"),
(12,3,"X","2015-04-26"),
(13,3,"X","2015-04-26"),
(14,3,"X","2015-04-26"),
(15,3,"X","2015-04-26");