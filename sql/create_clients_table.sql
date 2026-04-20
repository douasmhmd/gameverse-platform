-- Table clients (CRUD) — TP5.
-- Gestion des clients de GameVerse Academy.

CREATE TABLE IF NOT EXISTS clients (
    id                SERIAL PRIMARY KEY,
    first_name        VARCHAR(100) NOT NULL,
    last_name         VARCHAR(100) NOT NULL,
    email             VARCHAR(150) UNIQUE NOT NULL,
    phone             VARCHAR(20),
    city              VARCHAR(100),
    subscription_type VARCHAR(20) DEFAULT 'FREE',
    created_at        TIMESTAMP DEFAULT NOW()
);

-- Exemples de données (optionnel)
INSERT INTO clients (first_name, last_name, email, phone, city, subscription_type)
VALUES
    ('Ahmed',  'Benali',   'ahmed.benali@example.ma',   '0612345678', 'Rabat',       'PREMIUM'),
    ('Fatima', 'El Alami', 'fatima.elalami@example.ma', '0698765432', 'Casablanca',  'FREE'),
    ('Youssef','Amrani',   'youssef.amrani@example.ma', '0655443322', 'Marrakech',   'VIP')
ON CONFLICT (email) DO NOTHING;