-- Migration TP3 : ajouter status sur une table mods existante (TP2).

ALTER TABLE mods ADD COLUMN IF NOT EXISTS status VARCHAR(20);

-- Anciens enregistrements : visibles sur la liste publique
UPDATE mods SET status = 'APPROVED' WHERE status IS NULL;

ALTER TABLE mods ALTER COLUMN status SET DEFAULT 'PENDING';
