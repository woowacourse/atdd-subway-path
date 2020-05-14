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
   name varchar(255) not null,
   color varchar(30) not null,
   start_time time not null,
   end_time time not null,
   interval_time int not null,
   created_at datetime,
   updated_at datetime,
   primary key(id)
);

create table if not exists LINE_STATION
(
    line bigint not null,
    station_id bigint not null,
    pre_station_id bigint,
    distance int,
    duration int,
    created_at datetime,
    updated_at datetime
);

INSERT INTO LINE (name, start_time, end_time, interval_time, color) VALUES('1호선', '03:00', '19:00', 15, 'bg-blue-600');
INSERT INTO LINE (name, start_time, end_time, interval_time, color) VALUES('2호선', '05:00', '23:00', 10, 'bg-green-500');

INSERT INTO STATION (name) VALUES('구로');
INSERT INTO STATION (name) VALUES('신도림');
INSERT INTO STATION (name) VALUES('영등포');
INSERT INTO STATION (name) VALUES('신길');

INSERT INTO STATION (name) VALUES('문래');
INSERT INTO STATION (name) VALUES('대림');

-- null <-> 구로
INSERT INTO LINE_STATION (line, station_id, pre_station_id, distance, duration) VALUES (1, 1, null, 10, 10);
-- 구로 <-> 신도림
INSERT INTO LINE_STATION (line, station_id, pre_station_id, distance, duration) VALUES (1, 2, 1, 10, 10);
-- 신도림 <-> 영등포
INSERT INTO LINE_STATION (line, station_id, pre_station_id, distance, duration) VALUES (1, 3, 2, 10, 10);
-- 영등포 <-> 신길
INSERT INTO LINE_STATION (line, station_id, pre_station_id, distance, duration) VALUES (1, 4, 3, 10, 10);

-- null <-> 문래
INSERT INTO LINE_STATION (line, station_id, pre_station_id, distance, duration) VALUES (2, 5, null, 10, 10);
-- 문래 <-> 신도림
INSERT INTO LINE_STATION (line, station_id, pre_station_id, distance, duration) VALUES (2, 2, 5, 10, 10);
-- 신도림 <-> 대림
INSERT INTO LINE_STATION (line, station_id, pre_station_id, distance, duration) VALUES (2, 6, 2, 10, 10);