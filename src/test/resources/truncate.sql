TRUNCATE TABLE STATION;
TRUNCATE TABLE LINE;
TRUNCATE TABLE LINE_STATION;

INSERT INTO LINE (name, start_time, end_time, interval_time, color) VALUES('1호선', '03:00', '19:00', 15, 'bg-blue-800');
INSERT INTO LINE (name, start_time, end_time, interval_time, color) VALUES('2호선', '05:00', '23:00', 5, 'bg-green-500');
INSERT INTO LINE (name, start_time, end_time, interval_time, color) VALUES('5호선', '05:00', '23:00', 10, 'bg-purple-500');
INSERT INTO LINE (name, start_time, end_time, interval_time, color) VALUES('4호선', '05:00', '23:00', 10, 'bg-blue-300');
INSERT INTO LINE (name, start_time, end_time, interval_time, color) VALUES('9호선', '05:00', '23:00', 9, 'bg-yellow-500');

INSERT INTO STATION (name) VALUES('구로');
INSERT INTO STATION (name) VALUES('신도림');
INSERT INTO STATION (name) VALUES('신길');
INSERT INTO STATION (name) VALUES('용산');
INSERT INTO STATION (name) VALUES('서울역');
INSERT INTO STATION (name) VALUES('충정로');
INSERT INTO STATION (name) VALUES('당산');
INSERT INTO STATION (name) VALUES('영등포구청');
INSERT INTO STATION (name) VALUES('대림');
INSERT INTO STATION (name) VALUES('여의도');
INSERT INTO STATION (name) VALUES('동작');
INSERT INTO STATION (name) VALUES('삼각지');