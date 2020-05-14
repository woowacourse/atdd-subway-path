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
   start_time time,  -- Todo: not null 추가
   end_time time,  --  Todo: not null 추가
   interval_time int not null,
   created_at datetime,
   updated_at datetime,
   primary key(id)
);

create table if not exists LINE_STATION
(
    line bigint not null,
    sequence int not null,
    station_id bigint not null,
    pre_station_id bigint,
    distance int,
    duration int,
    created_at datetime,
    updated_at datetime
);
--
-- INSERT INTO STATION(name) VALUES('신정역');
-- INSERT INTO STATION(name) VALUES('여의도역');
-- INSERT INTO STATION(name) VALUES('천호역');
-- INSERT INTO STATION(name) VALUES('잠실역');
-- INSERT INTO STATION(name) VALUES('석촌역');
-- INSERT INTO STATION(name) VALUES('호돌역');
--
-- INSERT INTO LINE(name, interval_time) VALUES ('5호선', 10);
-- INSERT INTO LINE(name, interval_time) VALUES ('8호선', 10);
-- INSERT INTO LINE(name, interval_time) VALUES ('9호선', 10);
--
-- INSERT INTO LINE_STATION(line, sequence, station_id, distance, duration) VALUES (1, 1, 1,1,2); -- 출발 - 신정역
-- INSERT INTO LINE_STATION(line, sequence, station_id, pre_station_id, distance, duration) VALUES (1, 2, 2, 1,1,2); -- 신정 - 여의도
-- INSERT INTO LINE_STATION(line, sequence, station_id, pre_station_id, distance, duration) VALUES (1, 3, 3, 2,1,2); -- 여의도 - 천호
-- INSERT INTO LINE_STATION(line, sequence, station_id, distance, duration) VALUES (2, 4, 3,1,2); -- 출발 - 천호
-- INSERT INTO LINE_STATION(line, sequence, station_id, pre_station_id, distance, duration) VALUES (2, 4, 4, 3,1,2); -- 천호 - 잠실