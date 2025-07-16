-- Drop existing tables and sequences


DROP TABLE IF EXISTS activity_customer;
DROP TABLE IF EXISTS customer_group;
DROP TABLE IF EXISTS travel;
DROP TABLE IF EXISTS activity;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS advisor;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS price;
DROP TABLE IF EXISTS addresses;
DROP TABLE IF EXISTS cities;
DROP TABLE IF EXISTS countries;
DROP TABLE IF EXISTS company;



-- Create tables
CREATE TABLE company (
                         id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
                         code VARCHAR(255) NOT NULL UNIQUE,
                         name VARCHAR(255) NOT NULL
);

CREATE TABLE advisor (
                         id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
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
                           id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
                           iso_code CHAR(2) NOT NULL UNIQUE,
                           country_name VARCHAR(100) NOT NULL
);

CREATE TABLE cities (
                        id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
                        city_name VARCHAR(100) NOT NULL,
                        postal_code VARCHAR(20) NOT NULL,
                        country_id INTEGER NOT NULL,
                        CONSTRAINT fk_country FOREIGN KEY (country_id) REFERENCES countries(id)
);

CREATE TABLE addresses (
                           id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
                           address_line VARCHAR(255) NOT NULL,
                           city_id INTEGER NOT NULL,
                           CONSTRAINT fk_city FOREIGN KEY (city_id) REFERENCES cities(id)
);

CREATE TABLE customer (
                          id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
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
                          id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL UNIQUE,
                          company_id INTEGER NOT NULL,
                          CONSTRAINT fk_category_company FOREIGN KEY (company_id) REFERENCES company(id)
);

CREATE TABLE price (
                       id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
                       net_price NUMERIC(10,2) NOT NULL,
                       vat_rate NUMERIC(5,2) NOT NULL,
                       vat_amount NUMERIC(10,2),
                       gross_price NUMERIC(10,2)
);
CREATE TABLE groups (
                         id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL UNIQUE,
                         visible BOOLEAN NOT NULL DEFAULT TRUE,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE travel (
                        id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        description TEXT,
                        start_date DATE NOT NULL,
                        end_date DATE NOT NULL,
                        destination VARCHAR(255) NOT NULL,
                        budget DECIMAL(10,2),
                        group_id INTEGER,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        CONSTRAINT fk_travel_group FOREIGN KEY (group_id) REFERENCES groups(id)
);

CREATE TABLE activity (
                          id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
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
                                   CONSTRAINT fk_activity_customer_advisor FOREIGN KEY (group_id) REFERENCES groups(id)
);

CREATE TABLE customer_group (
    customer_id INTEGER NOT NULL,
    group_id INTEGER NOT NULL,
    PRIMARY KEY (customer_id, group_id),
    CONSTRAINT fk_customer_group_customer FOREIGN KEY (customer_id) REFERENCES customer(id),
    CONSTRAINT fk_customer_group_group FOREIGN KEY (group_id) REFERENCES groups(id)
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
    (2, 'Alice', 'Martin', 'alice.martin@email.com', '0000000000', '1900-01-01', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1),
    (3, 'Bob', 'Durand', 'bob.durand@email.com', '+33123456789', '1990-01-15', '+25gpiK1S4Iva7TxoGp0wu38j+pbGLcD6gK5xYqMDLs=', '2024-12-25', 's3.eu-central-003.backblazeb2.com/ProjetGosquad/image_9e3ce013-7024-4aae-99b2-54843cceee4b.bin', NULL, NULL, NULL, 1, 3, 4, 1),
    (4, 'Carla', 'Bernard', 'carla.bernard@email.com', '+33123456789', '1990-01-15', 'Cnri1wVXJDaIsnp7L61r9PRyVhcDAr2txx025C0IqLY=', '2030-01-15', 's3.eu-central-003.backblazeb2.com/ProjetGosquad/image_325234cd-4705-4c9e-bb40-43a019afee7d.bin', NULL, NULL, NULL, 1, 3, 4, 1),
    (5, 'David', 'Nguyen', 'david.nguyen@email.com', '+33123456789', '1990-01-15', NULL, NULL, NULL, 'nsjBUxFTHiK3HPhr91jvIUHzRQ9hkQsSRb7450okbqE=', '2029-01-15', 's3.eu-central-003.backblazeb2.com/ProjetGosquad/image_96ba8862-dc3c-49b6-b215-5e72f203a0a5.bin', 1, 3, 4, 1),
    (6, 'Emma', 'Dubois', 'emma.dubois@email.com', '+33123456789', '1990-01-15', NULL, NULL, NULL, 'b1hnGcWZeNYd+SKHsErsxcV9BgmKAhaidtJOGcfCaAc=', '2029-01-15', 's3.eu-central-003.backblazeb2.com/ProjetGosquad/image_51e4e08d-23ed-43b2-aa6d-50596e0e0a22.bin', 1, 5, 4, 1),
    (7, 'Fatou', 'Sy', 'fatou.sy@email.com', '+33123456789', '1990-01-15', NULL, NULL, NULL, 'JU2Y1tU6DrRVybCqnrq3SSzD064TK5bwciQoXFVUFlk=', '2029-01-15', 's3.eu-central-003.backblazeb2.com/ProjetGosquad/image_ae32d887-5af0-4406-b1e8-e5f53d0f5b8c.bin', 1, 5, 4, 1);
INSERT INTO category (id, name, company_id) VALUES
                                                        (1, 'Sport', 1),
                                                        (2, 'Repas', 1);

INSERT INTO groups (id, name, visible, created_at, updated_at) VALUES
    (1, 'Test Group', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO price(id, net_price, vat_rate, vat_amount, gross_price) VALUES
                                                        (1, 25.00, 20.00, 5.00, 30.00),
                                                        (2, 25.00, 20.00, 5.00, 30.00);

INSERT INTO activity (id, name, description, address_id, price_id, category_id, company_id) VALUES
    (1, 'Sport Class', 'A Sport class', 1, 1, 2, 1);

-- Voyages d'exemple
INSERT INTO travel (id, title, description, start_date, end_date, destination, budget, group_id, created_at, updated_at) VALUES
    (1, 'Voyage Paris', 'Découverte de Paris', '2025-07-20', '2025-07-25', 'Paris', 1200.00, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'Trip New York', 'Découverte de la Grosse Pomme', '2025-10-01', '2025-10-10', 'New York', 2500.00, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 'Voyage Tokyo', 'Sakura et traditions japonaises', '2025-11-15', '2025-11-25', 'Tokyo', 3000.00, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (4, 'Séjour Londres', 'Visite de la capitale britannique', '2025-12-05', '2025-12-12', 'Londres', 1800.00, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (5, 'Roadtrip Canada', 'Aventure au Québec', '2026-01-10', '2026-01-20', 'Montréal', 2200.00, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO activity_customer
(activity_id, customer_id, participation, end_date, start_date, group_id)
VALUES
    (1, 3, '1', '2025-07-06 12:00:00', '2025-07-06 09:30:00', 1),
    (1, 5, '1', '2025-07-06 12:00:00', '2025-07-06 09:30:00', 1),
    (1, 6, '1', '2025-07-06 12:00:00', '2025-07-06 08:30:00', 1),
    (1, 6, '0', '2025-07-06 12:00:00', '2025-07-06 08:30:00', 1);

-- Index pour améliorer les performances
CREATE INDEX IF NOT EXISTS idx_travel_group_id ON travel(group_id);
CREATE INDEX IF NOT EXISTS idx_travel_dates ON travel(start_date, end_date);
CREATE INDEX IF NOT EXISTS idx_customer_group_customer ON customer_group(customer_id);
CREATE INDEX IF NOT EXISTS idx_customer_group_group ON customer_group(group_id);

