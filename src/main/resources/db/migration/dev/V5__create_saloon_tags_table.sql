
CREATE TABLE saloon_tags (
	saloon_id INT NOT NULL,
	tag_id INT NOT NULL,
	tagged_date TIMESTAMP DEFAULT LOCALTIMESTAMP NOT NULL NULL_TO_DEFAULT,
	identifier VARCHAR(255) NOT NULL DEFAULT RANDOM_UUID(),
	created_at TIMESTAMP DEFAULT LOCALTIMESTAMP NOT NULL NULL_TO_DEFAULT,
	updated_at TIMESTAMP DEFAULT LOCALTIMESTAMP NOT NULL NULL_TO_DEFAULT,
	PRIMARY KEY (saloon_id, tag_id)
);



