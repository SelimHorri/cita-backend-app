
CREATE TABLE reservations (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	code VARCHAR(255) NOT NULL, -- should be UNIQUE & indexed
	description VARCHAR(255),
	start_date DATE DEFAULT CURRENT_DATE,
	-- canceled_date DATE DEFAULT CURRENT_DATE,
	status VARCHAR(255) NOT NULL DEFAULT 'NOT_STARTED',
	customer_id INT NOT NULL,
	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
	updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP()
);



