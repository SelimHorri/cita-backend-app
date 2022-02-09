
CREATE TABLE ordered_details (
	reservation_id INT NOT NULL,
	service_detail_id INT NOT NULL,
	ordered_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
	updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP(),
	PRIMARY KEY (reservation_id, service_detail_id, ordered_date)
);



