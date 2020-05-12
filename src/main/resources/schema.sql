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
   start_time time not null,
   end_time time not null,
   interval_time int not null,
   created_at datetime,
   updated_at datetime,
   bg_color varchar(64),
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

-- INSERT INTO LINE (name, start_time, end_time, interval_time, bg_color)
-- VALUES
-- ('1호선', '10:10', '10:20', 10, 'bg-gray-200'),
-- ('2호선', '10:10', '10:20', 10, 'bg-red-500'),
-- ('3호선', '10:10', '10:20', 10, 'bg-green-500'),
-- ('4호선', '10:10', '10:20', 10, 'bg-orange-500'),
-- ('5호선', '10:10', '10:20', 10, 'bg-purple-500'),
-- ('6호선', '10:10', '10:20', 10, 'bg-yellow-500'),
-- ('7호선', '10:10', '10:20', 10, 'bg-pink-500'),
-- ('8호선', '10:10', '10:20', 10, 'bg-blue-500');
--
-- INSERT INTO STATION(name)
-- VALUES
-- ('당산'),
-- ('합정'),
-- ('홍대입구'),
-- ('신촌'),
-- ('이대'),
-- ('아현'),
-- ('충정로'),
-- ('시청'),
-- ('서울역'),
-- ('남영'),
-- ('용산'),
-- ('노량진'),
-- ('숙대입구'),
-- ('삼각지'),
-- ('신용산'),
-- ('이촌'),
-- ('동작'),
-- ('이수');
--
-- INSERT into line_station (line, station_id, pre_station_id, duration, distance)
-- values (2, 1, null, 10, 2),
-- (2, 2, 1, 10, 2),
-- (2, 3, 2, 10, 2),
-- (2, 4, 3, 10, 2),
-- (2, 5, 4, 10, 2);