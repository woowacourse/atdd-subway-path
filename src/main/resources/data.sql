INSERT INTO Station (name)
VALUES ('답십리');
INSERT INTO Station (name)
VALUES ('장한평');
INSERT INTO Station (name)
VALUES ('군자');
INSERT INTO Station (name)
VALUES ('어린이대공원');
INSERT INTO Station (name)
VALUES ('건대입구');
INSERT INTO Station (name)
VALUES ('뚝섬유원지');
INSERT INTO Station (name)
VALUES ('청담');
INSERT INTO Station (name)
VALUES ('강남구청');
INSERT INTO Station (name)
VALUES ('선정릉');
INSERT INTO Station (name)
VALUES ('선릉');
INSERT INTO Station (name)
VALUES ('역삼');
INSERT INTO Station (name)
VALUES ('강남');
INSERT INTO Station (name)
VALUES ('교대');
INSERT INTO Station (name)
VALUES ('고속터미널');
INSERT INTO Station (name)
VALUES ('내방');

INSERT INTO Line(name, color, extra_fare)
VALUES ('2호선', '#009D3E', 200);
INSERT INTO Line(name, color, extra_fare)
VALUES ('3호선', '#EF7C1C', 300);
INSERT INTO Line(name, color, extra_fare)
VALUES ('5호선', '#996CAC', 500);
INSERT INTO Line(name, color, extra_fare)
VALUES ('7호선', '#747F00', 700);
INSERT INTO Line(name, color, extra_fare)
VALUES ('수인분당선', '#DBA829', 800);

INSERT INTO section(line_id, up_station_id, down_station_id, distance)
VALUES (3, 1, 2, 1);
INSERT INTO section(line_id, up_station_id, down_station_id, distance)
VALUES (3, 2, 3, 1);

INSERT INTO section(line_id, up_station_id, down_station_id, distance)
VALUES (4, 3, 4, 1);
INSERT INTO section(line_id, up_station_id, down_station_id, distance)
VALUES (4, 4, 5, 1);
INSERT INTO section(line_id, up_station_id, down_station_id, distance)
VALUES (4, 5, 6, 1);
INSERT INTO section(line_id, up_station_id, down_station_id, distance)
VALUES (4, 6, 7, 2);
INSERT INTO section(line_id, up_station_id, down_station_id, distance)
VALUES (4, 7, 8, 1);

INSERT INTO section(line_id, up_station_id, down_station_id, distance)
VALUES (5, 8, 9, 1);
INSERT INTO section(line_id, up_station_id, down_station_id, distance)
VALUES (5, 9, 10, 1);

INSERT INTO section(line_id, up_station_id, down_station_id, distance)
VALUES (1, 10, 11, 1);
INSERT INTO section(line_id, up_station_id, down_station_id, distance)
VALUES (1, 11, 12, 1);
INSERT INTO section(line_id, up_station_id, down_station_id, distance)
VALUES (1, 12, 13, 1);

INSERT INTO section(line_id, up_station_id, down_station_id, distance)
VALUES (2, 13, 14, 1);

INSERT INTO section(line_id, up_station_id, down_station_id, distance)
VALUES (4, 8, 14, 4);
INSERT INTO section(line_id, up_station_id, down_station_id, distance)
VALUES (4, 14, 15, 1);
