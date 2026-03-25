-- Colonne rôle pour les utilisateurs (TP4). Exécuter sur PostgreSQL.
-- Nom de colonne user_role (évite le mot réservé role en SQL).

ALTER TABLE users ADD COLUMN IF NOT EXISTS user_role VARCHAR(20) DEFAULT 'USER';

UPDATE users SET user_role = 'USER' WHERE user_role IS NULL OR TRIM(COALESCE(user_role, '')) = '';

-- Exemple : promouvoir un compte administrateur (adapter l'email)
-- UPDATE users SET user_role = 'ADMIN' WHERE email = 'admin@gameverseacademy.ma';
