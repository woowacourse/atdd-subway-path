INSERT INTO Station(name)
VALUES ('역곡');
INSERT INTO Station(name)
VALUES ('온수');
INSERT INTO Station(name)
VALUES ('오류동');
INSERT INTO Station(name)
VALUES ('개봉');
INSERT INTO Station(name)
VALUES ('구일');
INSERT INTO Station(name)
VALUES ('구로');
INSERT INTO Station(name)
VALUES ('신도림');
INSERT INTO Station(name)
VALUES ('대림');
INSERT INTO Station(name)
VALUES ('구로디지털단지');
INSERT INTO Station(name)
VALUES ('신대방');
INSERT INTO Station(name)
VALUES ('신림');
INSERT INTO Station(name)
VALUES ('봉천');
INSERT INTO Station(name)
VALUES ('서울대입구');
INSERT INTO Station(name)
VALUES ('낙성대');

INSERT INTO Line(name, color, extra_fare)
VALUES ('1호선', '#0d3692', 0);
INSERT INTO Line(name, color, extra_fare)
VALUES ('2호선', '#33a23d', 200);

INSERT INTO section(line_id, up_station_id, down_station_id, distance)
VALUES (1, 1, 2, 2);
INSERT INTO section(line_id, up_station_id, down_station_id, distance)
VALUES (1, 2, 3, 2);
INSERT INTO section(line_id, up_station_id, down_station_id, distance)
VALUES (1, 3, 4, 2);
INSERT INTO section(line_id, up_station_id, down_station_id, distance)
VALUES (1, 4, 5, 2);
INSERT INTO section(line_id, up_station_id, down_station_id, distance)
VALUES (1, 5, 6, 2);
INSERT INTO section(line_id, up_station_id, down_station_id, distance)
VALUES (1, 6, 7, 2);
INSERT INTO section(line_id, up_station_id, down_station_id, distance)
VALUES (2, 6, 7, 1);
INSERT INTO section(line_id, up_station_id, down_station_id, distance)
VALUES (2, 7, 8, 2);
INSERT INTO section(line_id, up_station_id, down_station_id, distance)
VALUES (2, 8, 9, 2);
INSERT INTO section(line_id, up_station_id, down_station_id, distance)
VALUES (2, 9, 2, 2);
INSERT INTO section(line_id, up_station_id, down_station_id, distance)
VALUES (2, 2, 11, 2);
INSERT INTO section(line_id, up_station_id, down_station_id, distance)
VALUES (2, 11, 12, 2);
INSERT INTO section(line_id, up_station_id, down_station_id, distance)
VALUES (2, 12, 13, 2);
INSERT INTO section(line_id, up_station_id, down_station_id, distance)
VALUES (2, 13, 14, 2);