-- Drop existing tables and sequences


DROP TABLE IF EXISTS advisor;
DROP SEQUENCE IF EXISTS PERSON_ID_SEQ;


DROP TABLE IF EXISTS activity_customer;
DROP SEQUENCE IF EXISTS ACTIVITY_CUSTOMER_ID_SEQ;


DROP TABLE IF EXISTS "group";
DROP SEQUENCE IF EXISTS GROUP_ID_SEQ;


DROP TABLE IF EXISTS activity;
DROP SEQUENCE IF EXISTS ACTIVITY_ID_SEQ;

DROP TABLE IF EXISTS customer;
DROP SEQUENCE IF EXISTS CUSTOMER_ID_SEQ;

DROP TABLE IF EXISTS category;
DROP SEQUENCE IF EXISTS CATEGORY_ID_SEQ;

DROP TABLE IF EXISTS price;
DROP SEQUENCE IF EXISTS PRICE_ID_SEQ;

DROP TABLE IF EXISTS addresses;
DROP SEQUENCE IF EXISTS ADDRESSES_ID_SEQ;

DROP TABLE IF EXISTS cities;
DROP SEQUENCE IF EXISTS CITIES_ID_SEQ;

DROP TABLE IF EXISTS countries;
DROP SEQUENCE IF EXISTS COUNTRIES_ID_SEQ;

DROP TABLE IF EXISTS company;
DROP SEQUENCE IF EXISTS COMPANY_ID_SEQ;



-- Create sequences
CREATE SEQUENCE ADDRESSES_ID_SEQ INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE SEQUENCE CITIES_ID_SEQ INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE SEQUENCE COUNTRIES_ID_SEQ INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE SEQUENCE CUSTOMER_ID_SEQ INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE SEQUENCE PERSON_ID_SEQ INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE SEQUENCE COMPANY_ID_SEQ INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE SEQUENCE ACTIVITY_ID_SEQ INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE SEQUENCE ACTIVITY_CUSTOMER_ID_SEQ INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE SEQUENCE CATEGORY_ID_SEQ INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE SEQUENCE GROUP_ID_SEQ INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;
CREATE SEQUENCE PRICE_ID_SEQ INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

-- Create tables
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

CREATE TABLE countries (
                           id INTEGER DEFAULT NEXT VALUE FOR COUNTRIES_ID_SEQ NOT NULL PRIMARY KEY,
                           iso_code CHAR(2) NOT NULL UNIQUE,
                           country_name VARCHAR(100) NOT NULL
);

CREATE TABLE cities (
                        id INTEGER DEFAULT NEXT VALUE FOR CITIES_ID_SEQ NOT NULL PRIMARY KEY,
                        city_name VARCHAR(100) NOT NULL,
                        postal_code VARCHAR(20) NOT NULL,
                        country_id INTEGER NOT NULL,
                        CONSTRAINT fk_country FOREIGN KEY (country_id) REFERENCES countries(id)
);

CREATE TABLE addresses (
                           id INTEGER DEFAULT NEXT VALUE FOR ADDRESSES_ID_SEQ NOT NULL PRIMARY KEY,
                           address_line VARCHAR(255) NOT NULL,
                           city_id INTEGER NOT NULL,
                           CONSTRAINT fk_city FOREIGN KEY (city_id) REFERENCES cities(id)
);

CREATE TABLE customer (
                          id INTEGER DEFAULT NEXT VALUE FOR CUSTOMER_ID_SEQ NOT NULL PRIMARY KEY,
                          firstname VARCHAR(255) NOT NULL,
                          lastname VARCHAR(255) NOT NULL,
                          email VARCHAR(255) NOT NULL,
                          phone_number VARCHAR(15) NOT NULL,
                          birth_date DATE NOT NULL,
                          id_card_number VARCHAR(50),
                          id_card_expiration_date DATE,
                          id_card_copy_url VARCHAR(255),
                          passport_number VARCHAR(50),
                          passport_expiration_date DATE,
                          passport_copy_url VARCHAR(255),
                          country_id INTEGER,
                          address_id INTEGER,
                          billing_address_id INTEGER,
                          company_id INTEGER NOT NULL,
                          CONSTRAINT fk_customer_country FOREIGN KEY (country_id) REFERENCES countries(id),
                          CONSTRAINT fk_customer_address FOREIGN KEY (address_id) REFERENCES addresses(id),
                          CONSTRAINT fk_customer_billing_address FOREIGN KEY (billing_address_id) REFERENCES addresses(id),
                          CONSTRAINT fk_customer_company FOREIGN KEY (company_id) REFERENCES company(id)
);

CREATE TABLE category (
                          id INTEGER DEFAULT NEXT VALUE FOR CATEGORY_ID_SEQ NOT NULL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL UNIQUE,
                          company_id INTEGER NOT NULL,
                          CONSTRAINT fk_category_company FOREIGN KEY (company_id) REFERENCES company(id)
);

CREATE TABLE price (
                       id INTEGER DEFAULT NEXT VALUE FOR PRICE_ID_SEQ NOT NULL PRIMARY KEY,
                       net_price NUMERIC(10,2) NOT NULL,
                       vat_rate NUMERIC(5,2) NOT NULL,
                       vat_amount NUMERIC(10,2) AS ((net_price * vat_rate) / 100),
                       gross_price NUMERIC(10,2) AS (net_price + ((net_price * vat_rate) / 100))
);
CREATE TABLE "group" (
                         id INTEGER DEFAULT NEXT VALUE FOR GROUP_ID_SEQ NOT NULL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE activity (
                          id INTEGER DEFAULT NEXT VALUE FOR ACTIVITY_ID_SEQ NOT NULL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          address_id INTEGER NOT NULL,
                          price_id INTEGER NOT NULL,
                          category_id INTEGER NOT NULL,
                          company_id INTEGER NOT NULL,
                          CONSTRAINT fk_activity_category FOREIGN KEY (category_id) REFERENCES category(id),
                          CONSTRAINT fk_activity_price FOREIGN KEY (price_id) REFERENCES price(id),
                          CONSTRAINT fk_activity_address FOREIGN KEY (address_id) REFERENCES addresses(id),
                          CONSTRAINT fk_activity_company FOREIGN KEY (company_id) REFERENCES company(id)
);

CREATE TABLE activity_customer (
                                   activity_id INTEGER NOT NULL,
                                   customer_id INTEGER NOT NULL,
                                   participation BOOLEAN NOT NULL DEFAULT FALSE,
                                   end_date timestamp NOT NULL,
                                   start_date timestamp NOT NULL,
                                   group_id INTEGER NOT NULL,
                                   CONSTRAINT fk_activity_customer_activity FOREIGN KEY (activity_id) REFERENCES activity(id),
                                   CONSTRAINT fk_activity_customer_customer FOREIGN KEY (customer_id) REFERENCES customer(id),
                                   CONSTRAINT fk_activity_customer_advisor FOREIGN KEY (group_id) REFERENCES "group"(id)
);







-- Insert data
INSERT INTO company (id, code, name) VALUES
    (1, 'GOSQUAD', 'Entreprise Gosquad');

INSERT INTO advisor (id, firstname, lastname, email, phone_number, compagny_id, password, role) VALUES
                                                                                                    (1, 'Gedeon', 'Mutikanga', 'gedeon.mutikanga@gosquad.com', '77777777', 1, '$2a$10$EQnOV47sKy0qubaXOR6WYOXGrJ7MXkR9D4xzgfktXk0XRH2aBCw3K', 'user'),
                                                                                                    (2, 'Jonas', 'Amozigh', 'jonas.amozigh@gmail.com', '612345678', 1, '$2a$10$EQnOV47sKy0qubaXOR6WYOXGrJ7MXkR9D4xzgfktXk0XRH2aBCw3K', 'admin'),
                                                                                                    (3, 'Matteo', 'Belin', 'matteo.belin@gosquad.com', '678787878', 1, '$2a$10$EQnOV47sKy0qubaXOR6WYOXGrJ7MXkR9D4xzgfktXk0XRH2aBCw3K', 'admin');

INSERT INTO countries (id, iso_code, country_name) VALUES
    (1, 'FR', 'France');

INSERT INTO cities (id, city_name, postal_code, country_id) VALUES
                                                                (1, 'Nantes', '44200', 1),
                                                                (2, 'Paris', '75009', 1),
                                                                (3, 'Marseille', '13001', 1),
                                                                (4, 'Paris', '75001', 1),
                                                                (5, 'Lyon', '69001', 1);

INSERT INTO addresses (id, address_line, city_id) VALUES
                                                      (1, '10 Boulevard Haussmann', 2),
                                                      (2, '5 Rue Victor Hugo', 3),
                                                      (3, '123 Rue de la Paix', 4),
                                                      (4, '456 Avenue des Champs', 5),
                                                      (5, '12 Rue de la Paix', 4);

INSERT INTO customer (id, firstname, lastname, email, phone_number, birth_date, id_card_number, id_card_expiration_date, id_card_copy_url, passport_number, passport_expiration_date, passport_copy_url, country_id, address_id, billing_address_id, company_id) VALUES
                                                                                                                                                                                                                                                                     (2, 'anonym', 'anonym', 'anonym@gmail.com', '0000000000', '1900-01-01', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1),
                                                                                                                                                                                                                                                                     (3, 'Jean', 'Dupont', 'jean.dupont@email.com', '+33123456789', '1990-01-15', '+25gpiK1S4Iva7TxoGp0wu38j+pbGLcD6gK5xYqMDLs=', '2024-12-25', 's3.eu-central-003.backblazeb2.com/ProjetGosquad/image_9e3ce013-7024-4aae-99b2-54843cceee4b.bin', NULL, NULL, NULL, 1, 3, 4, 1),
                                                                                                                                                                                                                                                                     (4, 'Jean', 'Dupont', 'jean.dupont@email.com', '+33123456789', '1990-01-15', 'Cnri1wVXJDaIsnp7L61r9PRyVhcDAr2txx025C0IqLY=', '2030-01-15', 's3.eu-central-003.backblazeb2.com/ProjetGosquad/image_325234cd-4705-4c9e-bb40-43a019afee7d.bin', NULL, NULL, NULL, 1, 3, 4, 1),
                                                                                                                                                                                                                                                                     (5, 'Jean', 'DEPUIS', 'jean.dupont@email.com', '+33123456789', '1990-01-15', NULL, NULL, NULL, 'nsjBUxFTHiK3HPhr91jvIUHzRQ9hkQsSRb7450okbqE=', '2029-01-15', 's3.eu-central-003.backblazeb2.com/ProjetGosquad/image_96ba8862-dc3c-49b6-b215-5e72f203a0a5.bin', 1, 3, 4, 1),
                                                                                                                                                                                                                                                                     (6, 'Jean', 'DEPUIS', 'jean.dupont@email.com', '+33123456789', '1990-01-15', NULL, NULL, NULL, 'b1hnGcWZeNYd+SKHsErsxcV9BgmKAhaidtJOGcfCaAc=', '2029-01-15', 's3.eu-central-003.backblazeb2.com/ProjetGosquad/image_51e4e08d-23ed-43b2-aa6d-50596e0e0a22.bin', 1, 5, 4, 1),
                                                                                                                                                                                                                                                                     (7, 'Matteo', 'BE', 'jean.dupont@email.com', '+33123456789', '1990-01-15', NULL, NULL, NULL, 'JU2Y1tU6DrRVybCqnrq3SSzD064TK5bwciQoXFVUFlk=', '2029-01-15', 's3.eu-central-003.backblazeb2.com/ProjetGosquad/image_ae32d887-5af0-4406-b1e8-e5f53d0f5b8c.bin', 1, 5, 4, 1);
INSERT INTO category (id, name, company_id) VALUES
                                                        (1, 'Sport', 1),
                                                        (2, 'Repas', 1);

INSERT INTO "group" (id, name) VALUES
    (1, 'test');

INSERT INTO price(id, net_price, vat_rate) VALUES
                                                        (1, 25.00, 20.00),
                                                        (2, 25.00, 20.00);

INSERT INTO activity (id, name, description, address_id, price_id, category_id, company_id) VALUES
    (1, 'Sport Class', 'A Sport class', 1, 1, 2, 1);

INSERT INTO activity_customer
(activity_id, customer_id, participation, end_date, start_date, group_id)
VALUES
    (1, 3, '1', '2025-07-06 12:00:00', '2025-07-06 09:30:00', 1),
    (1, 5, '1', '2025-07-06 12:00:00', '2025-07-06 09:30:00', 1),
    (1, 6, '1', '2025-07-06 12:00:00', '2025-07-06 08:30:00', 1),
    (1, 6, '0', '2025-07-06 12:00:00', '2025-07-06 08:30:00', 1);

-- Synchroniser les séquences après les insertions
ALTER SEQUENCE ADDRESSES_ID_SEQ RESTART WITH 6;  -- Prochain ID libre pour addresses
ALTER SEQUENCE CITIES_ID_SEQ RESTART WITH 6;     -- Prochain ID libre pour cities
ALTER SEQUENCE COUNTRIES_ID_SEQ RESTART WITH 2;  -- Prochain ID libre pour countries
ALTER SEQUENCE CUSTOMER_ID_SEQ RESTART WITH 8;   -- Prochain ID libre pour customer
ALTER SEQUENCE PERSON_ID_SEQ RESTART WITH 4;     -- Prochain ID libre pour advisor
ALTER SEQUENCE COMPANY_ID_SEQ RESTART WITH 2;    -- Prochain ID libre pour company
ALTER SEQUENCE CATEGORY_ID_SEQ RESTART WITH 3; -- Prochain ID libre pour category
ALTER SEQUENCE GROUP_ID_SEQ RESTART WITH 2; -- Prochain ID libre pour group
ALTER SEQUENCE ACTIVITY_ID_SEQ RESTART WITH 2; -- Prochain ID libre pour activity
ALTER SEQUENCE PRICE_ID_SEQ RESTART WITH 3; -- Prochain ID libre pour price


