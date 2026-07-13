# inv-master-001

Spring Boot backend for the Invoice Master application — multi-tenant invoicing with role-based access control, audit trails, and PDF generation.

## Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 3.3.2 |
| Language | Java 17 |
| Database | PostgreSQL 16 |
| Migrations | Flyway (single consolidated `V1`) |
| Auth | JWT (access + refresh tokens), Spring Security `@PreAuthorize` |
| ORM | Spring Data JPA / Hibernate |
| PDF | openhtmltopdf + Thymeleaf template |

## Getting Started

### 1. Start the database

```bash
docker-compose up -d
```

This starts a PostgreSQL 16 instance on port `5432` with:
- Database: `inv_master_001`
- User: `postgres`
- Password: `postgres`

### 2. Run the application

```bash
./mvnw spring-boot:run
```

Flyway applies `src/main/resources/db/migration/V1__create_all_basic_tables.sql` on first run — the complete schema lives in this single migration (pre-MVP convention; existing databases should be dropped and recreated when it changes).

The API is available at `http://localhost:8080`.

## Data Model

```
companies
  └── users           (company_id FK, role: ADMIN | MANAGER | EMPLOYEE)
  └── settings        (1-to-1: cgst/sgst percentages, currency, invoicePrefix, financialYear, vehicleNumbers)
  └── customers       (customerName, address, gstNumber, bank details, created_by_user_id)
  └── materials       (materialName, unit, currentPrice, hsnCode, created_by_user_id)
  │     └── material_price_history   (price, effectiveFrom, effectiveTo)
  └── products        (productName, description, hsnCode, active, created_by_user_id)
  │     └── product_materials        (many-to-many join to materials)
  │     └── product_price_history    (manufacturingCost, labourCharges, sellingPrice, profitMargin)
  └── invoice_sequences
  └── invoices        (invoiceNumber, customer, created_by, status, subtotal, cgst, sgst, discount, grandTotal, grandTotalWords)
        └── invoice_line_items  (productId, productName, hsnCode, quantity, unitPrice)
        └── payments            (paymentDate, amount, paymentMethod, transactionReference)
```

Every business entity carries a `created_by` audit reference; responses expose it as `createdByName`.

## API Endpoints

All endpoints except `/auth/**` require a JWT. Roles: **ADMIN**, **MANAGER**, **EMPLOYEE**.

### Auth — `/auth`

| Method | Path | Description |
|---|---|---|
| POST | `/auth/login` | Returns `{ accessToken, refreshToken }` |
| POST | `/auth/refresh` | Rotate tokens |
| POST | `/auth/company/register` | Create a new company + first admin |
| POST | `/auth/user/register` | Add a user to a company |

### Materials — `/materials`

| Method | Path | Role |
|---|---|---|
| GET | `/materials`, `/materials/{id}` | any |
| POST / PUT / DELETE | `/materials`, `/materials/{id}` | ADMIN, MANAGER |

### Products — `/products`

| Method | Path | Role |
|---|---|---|
| GET | `/products`, `/products/{id}` | any |
| POST / PUT | `/products`, `/products/{id}` | ADMIN, MANAGER |
| DELETE | `/products/{id}` | ADMIN |

### Customers — `/customers`

| Method | Path | Role |
|---|---|---|
| GET | `/customers`, `/customers/{id}` | any |
| POST / PUT / DELETE | `/customers`, `/customers/{id}` | ADMIN, MANAGER |

### Invoices — `/invoices`

| Method | Path | Role | Description |
|---|---|---|---|
| GET | `/invoices` | any | List (includes `customerName`, `createdByName`) |
| GET | `/invoices/{id}` | any | Detail with line items |
| POST | `/invoices` | ADMIN, MANAGER | Create — prices resolved server-side from product price history; taxes from company settings |
| PUT | `/invoices/{id}` | ADMIN, MANAGER | Update status / remarks |
| GET | `/invoices/{id}/line-items` | any | Line items |
| GET / POST | `/invoices/{id}/payments` | any / ADMIN, MANAGER | Payments; status auto-advances to `PARTIALLY_PAID` / `PAID` |
| GET | `/invoices/{id}/pdf` | ADMIN, MANAGER | Download tax invoice PDF |

### Company & Settings

| Method | Path | Description |
|---|---|---|
| GET / PUT / DELETE | `/company` | Company profile of the authenticated user |
| GET / POST / PUT / DELETE | `/api/company/settings` | Tax + invoice settings |

### User Management — `/admin/users` (ADMIN only)

| Method | Path | Description |
|---|---|---|
| GET | `/admin/users` | List users in the admin's company |
| POST | `/admin/users` | Create user `{ name, email, password, role }` — company inherited from the admin, password BCrypt-hashed |

## Invoice Status

```
GENERATED → PARTIALLY_PAID → PAID
         → CANCELLED
```

## User Roles

| Role | Permissions |
|---|---|
| ADMIN | Full access: all writes, product delete, user management |
| MANAGER | Create, update, delete (except products); no user management |
| EMPLOYEE | Read only |

## Multi-tenancy

Every query is scoped by the company on the JWT principal — company IDs are never accepted from request bodies, so cross-company access is not possible.

## Frontend

See [inv-master-ui](https://github.com/pjba11-11/inv-master-ui) for the Next.js frontend.
