-- Add user admin to the user table
INSERT INTO tx_user(id, firstname, lastname, username, email, created_by, created_at)
VALUES (1, 'admin', 'admin', 'admin', 'admin@myfinance.com', 'admin', NOW());