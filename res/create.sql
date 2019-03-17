

-- Box Table
CREATE TABLE box(
	box_id INTEGER PRIMARY KEY AUTO_INCREMENT,
	photo VARCHAR(500),
	window BOOLEAN NOT NULL,
	litter VARCHAR(50) NOT NULL,
	inside BOOLEAN NOT NULL,
	box_size NUMERIC(8,2) NOT NULL,
	area VARCHAR(50) NOT NULL,
	daily_rate NUMERIC(8,2) NOT NULL,
	isDeleted BOOLEAN NOT NULL

);

-- Reservierung Table
CREATE TABLE reservation(
	res_id LONG PRIMARY KEY AUTO_INCREMENT,
	customer VARCHAR(100) NOT NULL,
	res_from DATE NOT NULL,
	res_to DATE NOT NULL,
	res_isDeleted BOOLEAN NOT NULL

);

--BoxRes Table
CREATE TABLE box_res(
	box_id INTEGER REFERENCES box(box_id),
	res_id LONG REFERENCES reservation(res_id),
	price NUMERIC(20,2) NOT NULL,
	horse VARCHAR(100) NOT NULL,

	PRIMARY KEY(box_id, res_id)
);


