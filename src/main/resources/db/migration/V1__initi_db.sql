DROP TABLE IF EXISTS customer;
CREATE TABLE customer (
    id                          UUID NOT NULL PRIMARY KEY,
    customer_id                 UUID,
    end_user_id                 UUID,
    name                        TEXT,
    address                     TEXT,
    company_name                TEXT,
    tax_id                      TEXT,
    type                        TEXT,
    connected                   BOOLEAN,
    updated_at                  TIMESTAMP
);

DROP TABLE IF EXISTS invoice;
CREATE TABLE invoice (
    id                          UUID NOT NULL PRIMARY KEY,
    customer_id                 UUID,
    end_user_id                 UUID,
    payment_intent_id           UUID,
    payment_status              TEXT,
    currency                    TEXT,
    amount                      NUMERIC,
    issue_date                  DATE,
    due_date                    DATE,
    updated_at                  TIMESTAMP,
    number                      TEXT
);
