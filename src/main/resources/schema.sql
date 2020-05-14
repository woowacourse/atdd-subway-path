create table if not exists STATION
(
   id bigint auto_increment not null,
   name varchar(255) not null unique,
   created_at datetime,
   primary key(id)
);

create table if not exists LINE
(
   id bigint auto_increment not null,
   name varchar(255) not null unique,
   start_time time not null,
   end_time time not null,
   interval_time int not null,
   color varchar(20) not null unique,
   created_at datetime,
   updated_at datetime,
   primary key(id)
);

create table if not exists LINE_STATION
(
    line bigint not null,
    line_key int,
    station_id bigint not null,
    pre_station_id bigint,
    distance int,
    duration int,
    created_at datetime,
    updated_at datetime
);

-- 테스트시 주석 처리가 필요합니다.
INSERT INTO LINE (name, start_time, end_time, interval_time, color) VALUES('1호선', '03:00', '19:00', 15, 'bg-blue-800');
INSERT INTO LINE (name, start_time, end_time, interval_time, color) VALUES('2호선', '05:00', '23:00', 5, 'bg-green-500');
INSERT INTO LINE (name, start_time, end_time, interval_time, color) VALUES('4호선', '05:00', '23:00', 10, 'bg-blue-300');
INSERT INTO LINE (name, start_time, end_time, interval_time, color) VALUES('5호선', '05:00', '23:00', 10, 'bg-purple-500');
INSERT INTO LINE (name, start_time, end_time, interval_time, color) VALUES('9호선', '05:00', '23:00', 9, 'bg-yellow-500');

INSERT INTO STATION (name)
VALUES
('구로'),
('신도림'),
('신길'),
('용산'),
('서울역'),
('충정로'),
('당산'),
('영등포구청'),
('대림'),
('여의도'),
('동작'),
('삼각지'),
('시청');

INSERT INTO LINE_STATION (line, line_key, pre_station_id, station_id, distance, duration)
VALUES
(1, 0, null, 1, 10, 10),
(1, 1, 1, 2, 10, 10),
(1, 2, 2, 3, 10, 10),
(1, 3, 3, 4, 10, 10),
(1, 4, 4, 5, 10, 10),
(1, 5, 5, 13, 10, 10),

(2, 0, null, 13, 10, 10),
(2, 1, 13, 6, 10, 10),
(2, 2, 6, 7, 10, 10),
(2, 3, 7, 8, 10, 10),
(2, 4, 8, 2, 10, 10),
(2, 5, 2, 9, 10, 10),

(3, 0, null, 11, 10, 10),
(3, 1, 11, 12, 10, 10),
(3, 2, 12, 5, 10, 10),

(4, 0, null, 8, 10, 10),
(4, 1, 8, 3, 10, 10),
(4, 2, 3, 10, 10, 10),
(4, 3, 10, 6, 10, 10),

(5, 0, null, 7, 10, 10),
(5, 1, 7, 10, 10, 10),
(5, 2, 10, 11, 10, 10);