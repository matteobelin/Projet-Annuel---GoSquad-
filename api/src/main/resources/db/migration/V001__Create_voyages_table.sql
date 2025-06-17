-- Table pour les voyages
CREATE TABLE IF NOT EXISTS voyages (
    id SERIAL PRIMARY KEY,
    titre VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    date_depart DATE NOT NULL,
    date_retour DATE NOT NULL,
    participants INTEGER NOT NULL CHECK (participants > 0),
    budget DECIMAL(10, 2) NOT NULL CHECK (budget > 0),
    client_id INTEGER NOT NULL,
    statut VARCHAR(20) NOT NULL DEFAULT 'PLANIFIE' CHECK (statut IN ('PLANIFIE', 'EN_COURS', 'TERMINE')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Index pour améliorer les performances
CREATE INDEX IF NOT EXISTS idx_voyages_client_id ON voyages(client_id);
CREATE INDEX IF NOT EXISTS idx_voyages_statut ON voyages(statut);
CREATE INDEX IF NOT EXISTS idx_voyages_date_depart ON voyages(date_depart);

-- Trigger pour mettre à jour automatiquement updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_voyages_updated_at 
    BEFORE UPDATE ON voyages 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

-- Données de test (optionnel)
INSERT INTO voyages (titre, destination, date_depart, date_retour, participants, budget, client_id, statut) VALUES
('Voyage à Paris', 'Paris, France', '2024-07-15', '2024-07-22', 2, 1500.00, 1, 'PLANIFIE'),
('Séjour à Rome', 'Rome, Italie', '2024-08-10', '2024-08-17', 4, 2800.00, 2, 'PLANIFIE'),
('Aventure au Japon', 'Tokyo, Japon', '2024-09-05', '2024-09-20', 2, 4500.00, 1, 'EN_COURS'),
('Détente aux Maldives', 'Maldives', '2024-06-01', '2024-06-10', 2, 3200.00, 3, 'TERMINE');