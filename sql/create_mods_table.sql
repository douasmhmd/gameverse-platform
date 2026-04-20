-- Table mods (nouvelle installation) 

CREATE TABLE IF NOT EXISTS mods (
    id           SERIAL PRIMARY KEY,
    title        VARCHAR(150) NOT NULL,
    category     VARCHAR(50),
    author       VARCHAR(100),
    description  TEXT,
    downloads    INT DEFAULT 0,
    created_at   TIMESTAMP DEFAULT NOW(),
    developer    VARCHAR(100),
    publisher    VARCHAR(100),
    platform     VARCHAR(50),
    release_date VARCHAR(20),
    metacritic   INT,
    status       VARCHAR(20) DEFAULT 'PENDING'
);
