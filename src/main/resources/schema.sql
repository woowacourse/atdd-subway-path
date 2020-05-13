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
INSERT INTO LINE (name, start_time, end_time, interval_time, color) VALUES('1호선', '03:00', '19:00', 15, 'bg-blue-600');
INSERT INTO LINE (name, start_time, end_time, interval_time, color) VALUES('2호선', '05:00', '23:00', 10, 'bg-green-500');

INSERT INTO STATION (name) VALUES('구로');
INSERT INTO STATION (name) VALUES('신도림');
INSERT INTO STATION (name) VALUES('신길');
INSERT INTO STATION (name) VALUES('문래');
INSERT INTO STATION (name) VALUES('대림');