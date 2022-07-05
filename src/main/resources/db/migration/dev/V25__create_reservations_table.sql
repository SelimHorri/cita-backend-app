
CREATE TABLE reservations (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	code VARCHAR(255) NOT NULL, -- should be UNIQUE & indexed
	description VARCHAR(255),
	start_date TIMESTAMP DEFAULT LOCALTIMESTAMP NOT NULL NULL_TO_DEFAULT,
	cancel_date TIMESTAMP DEFAULT NULL,
	complete_date TIMESTAMP DEFAULT NULL,
	status VARCHAR(255) NOT NULL DEFAULT 'NOT_STARTED',
	customer_id INT NOT NULL,
	saloon_id INT NOT NULL,
	created_at TIMESTAMP DEFAULT LOCALTIMESTAMP NOT NULL NULL_TO_DEFAULT,
	updated_at TIMESTAMP DEFAULT LOCALTIMESTAMP NOT NULL NULL_TO_DEFAULT
);



