-- Script pour mettre à jour le schéma de la base de données pour correspondre aux entités Java
-- Exécuter ce script après avoir nettoyé la base de données existante

-- =======================
-- UPDATE TRAVEL_INFORMATION TABLE
-- =======================

-- Ajouter les colonnes manquantes à la table travel_information
ALTER TABLE travel_information 
ADD COLUMN title VARCHAR(255),
ADD COLUMN description TEXT,
ADD COLUMN destination VARCHAR(255),
ADD COLUMN budget DECIMAL(10, 2),
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Modifier les colonnes existantes pour correspondre aux entités Java
-- Changer start_date et end_date de timestamp à date
ALTER TABLE travel_information 
ALTER COLUMN start_date TYPE DATE,
ALTER COLUMN end_date TYPE DATE;

-- Rendre destination_id optionnel (peut être null si on utilise destination string)
ALTER TABLE travel_information 
ALTER COLUMN destination_id DROP NOT NULL;

-- =======================
-- UPDATE GROUP TABLE
-- =======================

-- Ajouter les colonnes manquantes à la table group
ALTER TABLE "group" 
ADD COLUMN visible BOOLEAN DEFAULT TRUE,
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- =======================
-- TRIGGERS FOR UPDATED_AT
-- =======================

-- Créer une fonction pour mettre à jour updated_at automatiquement
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Créer les triggers pour les tables avec updated_at
CREATE TRIGGER update_travel_information_updated_at 
    BEFORE UPDATE ON travel_information 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_group_updated_at 
    BEFORE UPDATE ON "group" 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- =======================
-- OPTIONAL: SAMPLE DATA
-- =======================

-- Vous pouvez ajouter des données de test si nécessaire
-- INSERT INTO "group" (name, visible) VALUES 
-- ('Groupe Test 1', TRUE),
-- ('Groupe Test 2', FALSE);

-- INSERT INTO travel_information (group_id, title, description, destination, start_date, end_date, budget) VALUES 
-- (1, 'Voyage à Paris', 'Description du voyage à Paris', 'Paris, France', '2024-06-01', '2024-06-07', 1500.00),
-- (2, 'Voyage à Tokyo', 'Description du voyage à Tokyo', 'Tokyo, Japon', '2024-07-15', '2024-07-25', 2500.00);

-- =======================
-- INDEXES FOR PERFORMANCE
-- =======================

-- Créer des index pour améliorer les performances
CREATE INDEX IF NOT EXISTS idx_travel_information_group_id ON travel_information(group_id);
CREATE INDEX IF NOT EXISTS idx_travel_information_start_date ON travel_information(start_date);
CREATE INDEX IF NOT EXISTS idx_travel_information_end_date ON travel_information(end_date);
CREATE INDEX IF NOT EXISTS idx_group_visible ON "group"(visible);

-- =======================
-- VERIFY SCHEMA
-- =======================

-- Requêtes pour vérifier le schéma mis à jour
-- SELECT column_name, data_type, is_nullable, column_default 
-- FROM information_schema.columns 
-- WHERE table_name = 'travel_information' 
-- ORDER BY ordinal_position;

-- SELECT column_name, data_type, is_nullable, column_default 
-- FROM information_schema.columns 
-- WHERE table_name = 'group' 
-- ORDER BY ordinal_position;
