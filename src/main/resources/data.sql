INSERT INTO LINE(name, start_time, end_time, interval_time, color)
VALUES ('1호선', PARSEDATETIME('06:00', 'HH:mm'), PARSEDATETIME('23:00', 'HH:mm'), 10, 'bg-blue-700');
INSERT INTO LINE(name, start_time, end_time, interval_time, color)
VALUES ('2호선', PARSEDATETIME('07:00', 'HH:mm'), PARSEDATETIME('00:00', 'HH:mm'), 10,
        'bg-green-500');
INSERT INTO LINE(name, start_time, end_time, interval_time, color)
VALUES ('4호선', PARSEDATETIME('08:00', 'HH:mm'), PARSEDATETIME('22:00', 'HH:mm'), 10, 'bg-blue-500');
INSERT INTO LINE(name, start_time, end_time, interval_time, color)
VALUES ('8호선', PARSEDATETIME('08:00', 'HH:mm'), PARSEDATETIME('22:00', 'HH:mm'), 10, 'bg-pink-500');

-- 1
INSERT INTO STATION(name)
VALUES ('시청');
INSERT INTO STATION(name)
VALUES ('서울역');
INSERT INTO STATION(name)
VALUES ('남영');
INSERT INTO STATION(name)
VALUES ('용산');
INSERT INTO STATION(name)
VALUES ('노량진');

-- 6
INSERT INTO STATION(name)
VALUES ('당산');
INSERT INTO STATION(name)
VALUES ('합정');
INSERT INTO STATION(name)
VALUES ('홍대입구');
INSERT INTO STATION(name)
VALUES ('신촌');
INSERT INTO STATION(name)
VALUES ('이대');
INSERT INTO STATION(name)
VALUES ('아현');
INSERT INTO STATION(name)
VALUES ('충정로');

-- 13
INSERT INTO STATION(name)
VALUES ('숙대입구');
INSERT INTO STATION(name)
VALUES ('삼각지');
INSERT INTO STATION(name)
VALUES ('신용산');
INSERT INTO STATION(name)
VALUES ('이촌');
INSERT INTO STATION(name)
VALUES ('동작');
INSERT INTO STATION(name)
VALUES ('이수');

-- 19
INSERT INTO STATION(name)
VALUES ('잠실');

-- 20, 1호선
INSERT INTO STATION(name)
VALUES ('대방');
INSERT INTO STATION(name)
VALUES ('신길');
INSERT INTO STATION(name)
VALUES ('영등포');
INSERT INTO STATION(name)
VALUES ('신도림');

-- 24, 2호선
INSERT INTO STATION(name)
VALUES ('영등포구청');
INSERT INTO STATION(name)
VALUES ('문래');

INSERT INTO LINE_STATION(LINE, pre_station_id, station_id, DISTANCE, DURATION)
VALUES (1, null, 1, 2, 1);
INSERT INTO LINE_STATION(LINE, pre_station_id, station_id, DISTANCE, DURATION)
VALUES (1, 1, 2, 2, 1);
INSERT INTO LINE_STATION(LINE, pre_station_id, station_id, DISTANCE, DURATION)
VALUES (1, 2, 3, 2, 1);
INSERT INTO LINE_STATION(LINE, pre_station_id, station_id, DISTANCE, DURATION)
VALUES (1, 3, 4, 2, 1);
INSERT INTO LINE_STATION(LINE, pre_station_id, station_id, DISTANCE, DURATION)
VALUES (1, 4, 5, 2, 1);
INSERT INTO LINE_STATION(LINE, pre_station_id, station_id, DISTANCE, DURATION)
VALUES (1, 5, 20, 2, 1);
INSERT INTO LINE_STATION(LINE, pre_station_id, station_id, DISTANCE, DURATION)
VALUES (1, 20, 21, 2, 1);
INSERT INTO LINE_STATION(LINE, pre_station_id, station_id, DISTANCE, DURATION)
VALUES (1, 21, 22, 2, 1);
INSERT INTO LINE_STATION(LINE, pre_station_id, station_id, DISTANCE, DURATION)
VALUES (1, 22, 23, 2, 1);

INSERT INTO LINE_STATION(LINE, pre_station_id, station_id, DISTANCE, DURATION)
VALUES (2, null, 23, 1, 1);
INSERT INTO LINE_STATION(LINE, pre_station_id, station_id, DISTANCE, DURATION)
VALUES (2, 23, 25, 1, 1);
INSERT INTO LINE_STATION(LINE, pre_station_id, station_id, DISTANCE, DURATION)
VALUES (2, 25, 24, 1, 1);
INSERT INTO LINE_STATION(LINE, pre_station_id, station_id, DISTANCE, DURATION)
VALUES (2, 24, 6, 1, 1);
INSERT INTO LINE_STATION(LINE, pre_station_id, station_id, DISTANCE, DURATION)
VALUES (2, 6, 7, 1, 1);
INSERT INTO LINE_STATION(LINE, pre_station_id, station_id, DISTANCE, DURATION)
VALUES (2, 7, 8, 1, 1);
INSERT INTO LINE_STATION(LINE, pre_station_id, station_id, DISTANCE, DURATION)
VALUES (2, 8, 9, 1, 1);
INSERT INTO LINE_STATION(LINE, pre_station_id, station_id, DISTANCE, DURATION)
VALUES (2, 9, 10, 1, 1);
INSERT INTO LINE_STATION(LINE, pre_station_id, station_id, DISTANCE, DURATION)
VALUES (2, 10, 11, 1, 1);
INSERT INTO LINE_STATION(LINE, pre_station_id, station_id, DISTANCE, DURATION)
VALUES (2, 11, 12, 1, 1);
INSERT INTO LINE_STATION(LINE, pre_station_id, station_id, DISTANCE, DURATION)
VALUES (2, 12, 1, 1, 1);


INSERT INTO LINE_STATION(LINE, pre_station_id, station_id, DISTANCE, DURATION)
VALUES (3, null, 2, 1, 1);
INSERT INTO LINE_STATION(LINE, pre_station_id, station_id, DISTANCE, DURATION)
VALUES (3, 2, 13, 1, 1);
INSERT INTO LINE_STATION(LINE, pre_station_id, station_id, DISTANCE, DURATION)
VALUES (3, 13, 14, 1, 1);
INSERT INTO LINE_STATION(LINE, pre_station_id, station_id, DISTANCE, DURATION)
VALUES (3, 14, 15, 1, 1);
INSERT INTO LINE_STATION(LINE, pre_station_id, station_id, DISTANCE, DURATION)
VALUES (3, 15, 16, 1, 1);
INSERT INTO LINE_STATION(LINE, pre_station_id, station_id, DISTANCE, DURATION)
VALUES (3, 16, 17, 1, 1);
INSERT INTO LINE_STATION(LINE, pre_station_id, station_id, DISTANCE, DURATION)
VALUES (3, 17, 18, 1, 1);

INSERT INTO LINE_STATION(LINE, pre_station_id, station_id, DISTANCE, DURATION)
VALUES (4, null, 19, 1, 1);