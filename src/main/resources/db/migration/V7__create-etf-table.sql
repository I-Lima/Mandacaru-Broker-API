CREATE TABLE etf (
    id VARCHAR(255) PRIMARY KEY UNIQUE NOT NULL,
    name VARCHAR(50) NOT NULL CHECK (LENGTH(name) > 0),
    company_name VARCHAR(255) NOT NULL,
    symbol VARCHAR(10) NOT NULL UNIQUE,
    amount INTEGER NOT NULL,
    price FLOAT NOT NULL
);
