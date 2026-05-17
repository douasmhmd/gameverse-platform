
CREATE TABLE IF NOT EXISTS users (
    email     VARCHAR(150) PRIMARY KEY,
    password  VARCHAR(255) NOT NULL,
    user_role VARCHAR(20)  DEFAULT 'USER'
);

-- Comptes de test 
INSERT INTO users (email, password, user_role) VALUES
    ('admin@gameverseacademy.ma', 'admin123', 'ADMIN'),
    ('user@gameverseacademy.ma',  'user123',  'USER')
ON CONFLICT (email) DO NOTHING;