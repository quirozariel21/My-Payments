DROP TABLE IF EXISTS tx_user;
CREATE TABLE tx_user (
	id bigint PRIMARY KEY,
	firstname VARCHAR(50) NOT NULL,
	lastname VARCHAR(50) NOT NULL,
	username VARCHAR(50) UNIQUE NOT NULL,
	email VARCHAR(50) UNIQUE NOT NULL,
	created_by VARCHAR(50) NOT NULL,
	created_at TIMESTAMP DEFAULT NOW() NOT NULL
);

DROP TABLE IF EXISTS tx_category;
CREATE TABLE tx_category(
	id bigint PRIMARY KEY,
	"code" VARCHAR(100),
	"name" VARCHAR(100) NOT NULL,
    "description" VARCHAR(150),
    parent_id bigint,
    created_by VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW() NOT NULL
);

DROP TABLE IF EXISTS tx_personal_finance;
CREATE TABLE tx_personal_finance(
	id bigint PRIMARY KEY,
	"year" INTEGER NOT NULL,
	"month" VARCHAR(15) NOT NULL,
	created_by VARCHAR(50) NOT NULL,
	created_at TIMESTAMP DEFAULT NOW() NOT NULL
);

DROP TABLE IF EXISTS tx_income;
CREATE TABLE tx_income(
	id bigint PRIMARY KEY,
	personal_finance_id bigint NOT NULL,
	"name" VARCHAR(20) NOT NULL,
	currency VARCHAR(5) NOT NULL,
	amount NUMERIC(7,2) NOT NULL DEFAULT 0,
	note VARCHAR(50),
	created_by VARCHAR(50) NOT NULL,
	created_at TIMESTAMP DEFAULT NOW() NOT NULL,
	CONSTRAINT fk_personal_finance_id
	    FOREIGN KEY (personal_finance_id)
	        REFERENCES tx_personal_finance (id)
);

DROP TABLE IF EXISTS tx_expense;
CREATE TABLE tx_expense(
	id bigint PRIMARY KEY,
	category_id bigint NOT NULL,
	subcategory_id bigint NOT NULL,
	"note" VARCHAR(500),
	amount NUMERIC(7,2) NOT NULL,
	currency VARCHAR(5) NOT NULL,
	expensed_date DATE NOT NULL,
	personal_finance_id bigint NOT NULL,
	created_by VARCHAR(50) NOT NULL,
	created_at TIMESTAMP DEFAULT NOW() NOT NULL,
	CONSTRAINT fk_category_id FOREIGN KEY(category_id) REFERENCES tx_category(id),
	CONSTRAINT fk_subcategory_id FOREIGN KEY(subcategory_id) REFERENCES tx_category(id),
    CONSTRAINT fk_personal_finance_id FOREIGN KEY (personal_finance_id) REFERENCES tx_personal_finance (id)
);