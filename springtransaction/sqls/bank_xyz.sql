-- Table: account

-- DROP TABLE account;

CREATE TABLE account
(
  acc_number integer NOT NULL,
  customer_name character(120),
  acc_balance integer NOT NULL DEFAULT 0,
  CONSTRAINT account_pkey PRIMARY KEY (acc_number)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE account
  OWNER TO postgres;

  

INSERT INTO account(acc_number, customer_name, acc_balance) VALUES (101, 'Kumar', 900);
