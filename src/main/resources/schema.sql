CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    user_role VARCHAR(50),
    enabled BOOLEAN NOT NULL
);

INSERT INTO users (id, username, password, email, user_role, enabled)
SELECT 1, 'admin', 'asdf111', 's85812bht@gmail.com', 'ADMIN', true
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'admin'
);