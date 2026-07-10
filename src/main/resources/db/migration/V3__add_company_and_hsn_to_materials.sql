ALTER TABLE materials ADD COLUMN IF NOT EXISTS company_id BIGINT REFERENCES companies(id);
ALTER TABLE materials ADD COLUMN IF NOT EXISTS hsn_code VARCHAR(20);

-- backfill company_id from existing product FK
UPDATE materials m
SET company_id = p.company_id
FROM products p
WHERE m.product_id = p.id AND m.company_id IS NULL;
