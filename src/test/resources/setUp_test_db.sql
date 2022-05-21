INSERT INTO station (name) VALUES ('강남역');
INSERT INTO station (name) VALUES ('역삼역');
INSERT INTO station (name) VALUES ('선릉역');

-- line
INSERT INTO line (name, color, extraFare) VALUES ('2호선', 'rg-red-600', 1000);

-- section
INSERT INTO section (line_id, up_station_id, down_station_id, distance) VALUES (1, 1, 2, 10);
