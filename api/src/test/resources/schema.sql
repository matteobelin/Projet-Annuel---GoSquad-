DROP TABLE IF EXISTS advisor;
DROP SEQUENCE IF EXISTS PERSON_ID_SEQ;

DROP TABLE IF EXISTS company;
DROP SEQUENCE IF EXISTS COMPANY_ID_SEQ;

CREATE SEQUENCE PERSON_ID_SEQ INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

CREATE SEQUENCE COMPANY_ID_SEQ INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

CREATE TABLE company (
                         id INTEGER DEFAULT NEXT VALUE FOR COMPANY_ID_SEQ NOT NULL PRIMARY KEY,
                         code VARCHAR(255) NOT NULL UNIQUE,
                         name VARCHAR(255) NOT NULL
);

CREATE TABLE advisor (
                         id INTEGER DEFAULT NEXT VALUE FOR PERSON_ID_SEQ NOT NULL PRIMARY KEY,
                         firstname VARCHAR(255) NOT NULL,
                         lastname VARCHAR(255) NOT NULL,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         phone_number VARCHAR(255) NOT NULL,
                         compagny_id INTEGER NOT NULL,
                         password VARCHAR(255) NOT NULL,
                         role VARCHAR(255) NOT NULL,
                         CONSTRAINT fk_company FOREIGN KEY (compagny_id) REFERENCES company(id)
);

INSERT INTO company (id, code, name) VALUES
    (1, 'GOSQUAD', 'Entreprise Gosquad');

INSERT INTO advisor (id, firstname, lastname, email, phone_number, compagny_id, password, role) VALUES
                                                                                                    (1, 'Gedeon', 'Mutikanga', 'gedeon.mutikanga@gosquad.com', '77777777', 1, '$2a$10$EQnOV47sKy0qubaXOR6WYOXGrJ7MXkR9D4xzgfktXk0XRH2aBCw3K', 'user'),
                                                                                                    (2, 'Jonas', 'Amozigh', 'jonas.amozigh@gmail.com', '612345678', 1, '$2a$10$EQnOV47sKy0qubaXOR6WYOXGrJ7MXkR9D4xzgfktXk0XRH2aBCw3K', 'admin'),
                                                                                                    (3, 'Matteo', 'Belin', 'matteo.belin@gosquad.com', '678787878', 1, '$2a$10$EQnOV47sKy0qubaXOR6WYOXGrJ7MXkR9D4xzgfktXk0XRH2aBCw3K', 'admin');
