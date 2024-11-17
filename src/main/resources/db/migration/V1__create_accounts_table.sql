CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE accounts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    due_date DATE NOT NULL,
    payment_date DATE,
    value DECIMAL(10, 2) NOT NULL,
    description VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL
);
