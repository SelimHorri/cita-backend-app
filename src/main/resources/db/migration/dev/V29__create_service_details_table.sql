
CREATE TABLE service_details (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(255),
	description VARCHAR(255),
	is_available BOOLEAN DEFAULT true,
	duration INT DEFAULT 15, -- in min
	price_unit DECIMAL(6, 3),
	category_id INT NOT NULL,
	created_at TIMESTAMP DEFAULT LOCALTIMESTAMP NOT NULL NULL_TO_DEFAULT,
	updated_at TIMESTAMP DEFAULT LOCALTIMESTAMP NOT NULL NULL_TO_DEFAULT
);



