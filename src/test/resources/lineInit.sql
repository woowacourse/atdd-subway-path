ALTER TABLE line ALTER COLUMN id RESTART WITH 1;
ALTER TABLE station ALTER COLUMN id RESTART WITH 1;
ALTER TABLE section ALTER COLUMN id RESTART WITH 1;

INSERT INTO line (name, color, extra_fare) VALUES ('7호선', 'red', 1000);
INSERT INTO station (name) VALUES ('하계역');
INSERT INTO station (name) VALUES ('중계역');
INSERT INTO section (line_id, up_station_id, down_station_id, distance)
VALUES (1, 1, 2, 10);
