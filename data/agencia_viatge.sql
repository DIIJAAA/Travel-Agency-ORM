CREATE TABLE client(
id_client INT AUTO_INCREMENT PRIMARY KEY,
dni VARCHAR(100) NOT NULL UNIQUE,
nom VARCHAR(100) NOT NULL,
cognom VARCHAR(100) NOT NULL,
email VARCHAR(100) NOT NULL,
telefon VARCHAR(100) NOT NULL
);

CREATE TABLE reserva(
id_reserva INT AUTO_INCREMENT PRIMARY KEY,
id_client INT NOT NULL,
data_inici DATE NOT NULL,
data_fi DATE NOT NULL,
preu_total DECIMAL(10,2) NOT NULL,
persones_total INT NOT NULL,
FOREIGN KEY (id_client) REFERENCES client(id_client) ON DELETE CASCADE
);

CREATE TABLE allotjament(
id_allotjament INT AUTO_INCREMENT PRIMARY KEY,
nom VARCHAR(100) NOT NULL,
lloc VARCHAR(100) NOT NULL,
tipus VARCHAR(100) NOT NULL,
preu_per_nit DECIMAL(10,2) NOT NULL
);

CREATE TABLE activitat(
id_activitat INT AUTO_INCREMENT PRIMARY KEY,
nom VARCHAR(100) NOT NULL,
descripcio VARCHAR(100) NOT NULL,
lloc VARCHAR(100) NOT NULL,
preu_per_persona DECIMAL(10,2) NOT NULL
);

CREATE TABLE estada(
id_client INT NOT NULL,
id_reserva INT NOT NULL,
data_inici DATE NOT NULL,
data_fi DATE NOT NULL,
preu_total DECIMAL(10,2) NOT NULL,
FOREIGN KEY (id_client) REFERENCES client(id_client) ON DELETE CASCADE,
FOREIGN KEY (id_reserva) REFERENCES reserva(id_reserva) ON DELETE CASCADE,
PRIMARY KEY (id_client, id_reserva)
);

CREATE TABLE reserva_activitat(
id_activitat INT NOT NULL,
id_reserva INT NOT NULL,
data_inici DATE NOT NULL,
data_fi DATE NOT NULL,
preu_total DECIMAL(10,2) NOT NULL,
FOREIGN KEY (id_activitat) REFERENCES activitat(id_activitat) ON DELETE CASCADE,
FOREIGN KEY (id_reserva) REFERENCES reserva(id_reserva) ON DELETE CASCADE,
PRIMARY KEY (id_activitat, id_reserva)
);
