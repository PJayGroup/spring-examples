-- Table: address

-- DROP TABLE address;

CREATE TABLE address
(
  id integer NOT NULL,
  address character(1000),
  customer_id integer NOT NULL DEFAULT 0,
  CONSTRAINT address_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE address
  OWNER TO postgres;

  
-- Table: customer

-- DROP TABLE customer;

CREATE TABLE customer
(
  id integer NOT NULL,
  name character(120),
  "number" integer NOT NULL DEFAULT 0,
  CONSTRAINT customer_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE customer
  OWNER TO postgres;

  
-- Table: products

-- DROP TABLE products;

CREATE TABLE products
(
  id integer NOT NULL,
  product_name character(120),
  quantity integer NOT NULL DEFAULT 0,
  CONSTRAINT products_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE products
  OWNER TO postgres;


 
INSERT INTO "products"("id", "product_name", "quantity") VALUES (1, 'iPhone 7s', 8);

INSERT INTO "products"("id", "product_name", "quantity") VALUES (2, 'Samsung Galaxy S7 Edge', 2);
