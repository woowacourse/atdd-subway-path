INSERT INTO LINE (name, start_time, end_time, interval_time)
VALUES ('2호선', '02:00:00', '13:00:00', 10), ('7호선', '02:00:00', '13:00:00', 10), ('분당선', '02:00:00', '13:00:00', 10);
INSERT INTO STATION (name) VALUES ('왕십리'), ('한양대'), ('뚝섬'), ('성수'), ('건대입구'), ('뚝섬유원지'), ('청담'), ('강남구청'), ('압구정로데오'), ('서울숲');
INSERT INTO LINE_STATION (line, station_id, pre_station_id, distance, duration)
VALUES (1, 1, null, 0, 0), (1, 2, 1, 5, 2), (1, 3, 2, 5, 2),(1, 4, 3, 5, 2),(1, 5, 4, 5, 2),
(2, 5, null, 0, 0), (2, 6, 5, 7, 4),(2, 7, 6, 7, 4),(2, 8, 7, 7, 4),
(3, 8, null, 0, 0),(3, 9, 8, 3, 1),(3, 10, 9, 3, 1),(3, 1, 10, 3, 1);