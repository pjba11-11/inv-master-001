ALTER TABLE materials ADD COLUMN IF NOT EXISTS created_by_user_id BIGINT REFERENCES users(id);
ALTER TABLE products ADD COLUMN IF NOT EXISTS created_by_user_id BIGINT REFERENCES users(id);
ALTER TABLE customers ADD COLUMN IF NOT EXISTS created_by_user_id BIGINT REFERENCES users(id);
