-- ============================================
-- COMPANIES
-- ============================================

CREATE TABLE companies
(
    id             BIGSERIAL PRIMARY KEY,
    company_name   VARCHAR(150) NOT NULL,
    gst_number     VARCHAR(30),
    phone          VARCHAR(20),
    email          VARCHAR(255),
    address        TEXT,
    bank_name      VARCHAR(100),
    account_number VARCHAR(50),
    ifsc           VARCHAR(20),
    upi_id         VARCHAR(50),
    logo           TEXT,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at     TIMESTAMP
);

-- ============================================
-- USERS
-- ============================================

CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    company_id BIGINT              NOT NULL REFERENCES companies(id),
    name       VARCHAR(100)        NOT NULL,
    email      VARCHAR(255) UNIQUE NOT NULL,
    password   VARCHAR(255)        NOT NULL,
    role       VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- ============================================
-- SETTINGS
-- ============================================

CREATE TABLE settings
(
    id                    BIGSERIAL PRIMARY KEY,
    company_id            BIGINT UNIQUE NOT NULL REFERENCES companies(id),
    gst_percentage        DECIMAL(5, 2),
    cgst_percentage       DECIMAL(5, 2),
    sgst_percentage       DECIMAL(5, 2),
    default_profit_margin DECIMAL(5, 2),
    currency              VARCHAR(10),
    invoice_prefix        VARCHAR(20),
    financial_year        VARCHAR(20),
    vehicle_numbers       TEXT[],
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at            TIMESTAMP
);

-- ============================================
-- CUSTOMERS
-- ============================================

CREATE TABLE customers
(
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL REFERENCES companies(id),
    customer_name       VARCHAR(150) NOT NULL,
    gst_number          VARCHAR(30),
    phone               VARCHAR(20),
    email               VARCHAR(255),
    address             TEXT,
    bank_name           VARCHAR(100),
    account_number      VARCHAR(50),
    ifsc                VARCHAR(20),
    upi_id              VARCHAR(50),
    created_by_user_id  BIGINT REFERENCES users(id),
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at          TIMESTAMP
);

-- ============================================
-- PRODUCTS
-- ============================================

CREATE TABLE products
(
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT       NOT NULL REFERENCES companies(id),
    product_name        VARCHAR(150) NOT NULL,
    description         TEXT,
    active              BOOLEAN   DEFAULT TRUE,
    hsn_code            VARCHAR(30),
    created_by_user_id  BIGINT REFERENCES users(id),
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at          TIMESTAMP
);

-- ============================================
-- MATERIALS
-- ============================================

CREATE TABLE materials
(
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT REFERENCES companies(id),
    material_name       VARCHAR(100) NOT NULL,
    hsn_code            VARCHAR(20),
    unit                VARCHAR(20),
    current_price       DECIMAL(12, 2),
    active              BOOLEAN   DEFAULT TRUE,
    created_by_user_id  BIGINT REFERENCES users(id),
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at          TIMESTAMP
);

-- ============================================
-- PRODUCT <-> MATERIAL (many-to-many)
-- ============================================

CREATE TABLE product_materials
(
    product_id  BIGINT NOT NULL REFERENCES products(id),
    material_id BIGINT NOT NULL REFERENCES materials(id),
    PRIMARY KEY (product_id, material_id)
);

-- ============================================
-- MATERIAL PRICE HISTORY
-- ============================================

CREATE TABLE material_price_history
(
    id             BIGSERIAL PRIMARY KEY,
    material_id    BIGINT         NOT NULL REFERENCES materials(id),
    price          DECIMAL(12, 2) NOT NULL,
    effective_from DATE           NOT NULL,
    effective_to   DATE,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at     TIMESTAMP
);

-- ============================================
-- PRODUCT PRICE HISTORY
-- ============================================

CREATE TABLE product_price_history
(
    id                 BIGSERIAL PRIMARY KEY,
    product_id         BIGINT NOT NULL REFERENCES products(id),
    manufacturing_cost DECIMAL(12, 2),
    labour_charges     DECIMAL(12, 2),
    selling_price      DECIMAL(12, 2),
    profit_margin      DECIMAL(5, 2),
    effective_from     DATE,
    effective_to       DATE,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at         TIMESTAMP
);

-- ============================================
-- INVOICE SEQUENCES
-- ============================================

CREATE TABLE invoice_sequences
(
    id             BIGSERIAL PRIMARY KEY,
    company_id     BIGINT NOT NULL REFERENCES companies(id),
    invoice_id     BIGINT NOT NULL,
    invoice_number VARCHAR(50),
    invoice_date   DATE   NOT NULL,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at     TIMESTAMP
);

-- ============================================
-- INVOICES
-- ============================================

CREATE TABLE invoices
(
    id                  BIGSERIAL PRIMARY KEY,
    invoice_number      VARCHAR(50) UNIQUE,
    company_id          BIGINT NOT NULL REFERENCES companies(id),
    created_by          BIGINT NOT NULL REFERENCES users(id),
    customer_id         BIGINT NOT NULL,
    invoice_date        DATE   NOT NULL,
    subtotal            DECIMAL(12, 2) DEFAULT 0,
    po_number           VARCHAR(30),
    cgst                DECIMAL(12, 2) DEFAULT 0,
    sgst                DECIMAL(12, 2) DEFAULT 0,
    cgst_percentage     DECIMAL(5, 2),
    sgst_percentage     DECIMAL(5, 2),
    discount            DECIMAL(12, 2) DEFAULT 0,
    grand_total         DECIMAL(12, 2) DEFAULT 0,
    grand_total_words   TEXT NOT NULL,
    status              VARCHAR(30)    DEFAULT 'GENERATED',
    remarks             TEXT,
    created_at          TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    deleted_at          TIMESTAMP
);

-- ============================================
-- INVOICE LINE ITEMS
-- ============================================

CREATE TABLE invoice_line_items
(
    id           BIGSERIAL PRIMARY KEY,
    invoice_id   BIGINT         NOT NULL REFERENCES invoices(id) ON DELETE CASCADE,
    product_id   BIGINT         NOT NULL,
    product_name VARCHAR(150)   NOT NULL,
    hsn_code     VARCHAR(30),
    quantity     DECIMAL(12, 2) NOT NULL,
    unit_price   DECIMAL(12, 2) NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at   TIMESTAMP
);

-- ============================================
-- PAYMENTS
-- ============================================

CREATE TABLE payments
(
    id                    BIGSERIAL PRIMARY KEY,
    invoice_id            BIGINT         NOT NULL REFERENCES invoices(id) ON DELETE CASCADE,
    payment_date          DATE,
    amount                DECIMAL(12, 2) NOT NULL,
    payment_method        VARCHAR(30),
    transaction_reference VARCHAR(100),
    remarks               TEXT,
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at            TIMESTAMP
);

-- ============================================
-- ANALYTICS CACHE
-- ============================================

CREATE TABLE analytics_cache
(
    id            BIGSERIAL PRIMARY KEY,
    company_id    BIGINT NOT NULL REFERENCES companies(id),
    analysis_type VARCHAR(50),
    period_start  DATE,
    period_end    DATE,
    analysis_json JSONB,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at    TIMESTAMP
);
