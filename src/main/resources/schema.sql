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

    INSERT INTO LINE (name, start_time, end_time, interval_time, color) VALUES('2호선', '03:00', '19:00', 15, 'bg-blue-600');
    INSERT INTO LINE (name, start_time, end_time, interval_time, color) VALUES('8호선', '05:00', '23:00', 10, 'bg-green-500');

    INSERT INTO STATION (name) VALUES('잠실');
    INSERT INTO STATION (name) VALUES('잠실새내');
    INSERT INTO STATION (name) VALUES('종합운동장');
    INSERT INTO STATION (name) VALUES('삼전');
    INSERT INTO STATION (name) VALUES('석촌고분');
    INSERT INTO STATION (name) VALUES('석촌');

--     -- null <-> 잠실
--     INSERT INTO LINE_STATION (line, station_id, pre_station_id, distance, duration) VALUES (1, 1, null, 0, 0);
--     -- 잠실 <-> 잠실새내
--     INSERT INTO LINE_STATION (line, station_id, pre_station_id, distance, duration) VALUES (1, 2, 1, 1, 20);
--     -- 잠실새내 <-> 종합운동장
--     INSERT INTO LINE_STATION (line, station_id, pre_station_id, distance, duration) VALUES (1, 3, 2, 1, 30);
--
--     -- null <-> 종합운동장
--     INSERT INTO LINE_STATION (line, station_id, pre_station_id, distance, duration) VALUES (2, 3, null, 0, 0);
--     -- 종합운동장 <-> 삼전
--     INSERT INTO LINE_STATION (line, station_id, pre_station_id, distance, duration) VALUES (2, 4, 3, 20, 1);
--     -- 삼전 <-> 석촌고분
--     INSERT INTO LINE_STATION (line, station_id, pre_station_id, distance, duration) VALUES (2, 5, 4, 30, 1);
--     -- 석촌고분 <-> 석촌
--     INSERT INTO LINE_STATION (line, station_id, pre_station_id, distance, duration) VALUES (2, 6, 5, 40, 1);
--     -- 석촌 <-> 잠실
--     INSERT INTO LINE_STATION (line, station_id, pre_station_id, distance, duration) VALUES (2, 1, 6, 50, 1);