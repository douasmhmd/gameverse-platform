

ALTER TABLE users ADD COLUMN IF NOT EXISTS user_role VARCHAR(20) DEFAULT 'USER';

UPDATE users SET user_role = 'USER' WHERE user_role IS NULL OR TRIM(COALESCE(user_role, '')) = '';


