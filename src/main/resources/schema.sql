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
--web 프론트 테스트시 사용
--insert into station values(1,'강남역',null);
--insert into station values(2,'역삼역',null);
--insert into station values(3,'선릉역',null);
--insert into station values(4,'삼성역',null);
--insert into station values(5,'몽촌토성역',null);
--insert into station values(6,'강동구청역',null);
--insert into station values(7,'지름길역',null);
--
--insert into line values(1, '2호선', '12:00', '12:01', 10, null, null);
--insert into line values(2, '8호선', '12:00', '12:01', 10, null, null);
--insert into line values(3, '지름길호선', '12:00', '12:01', 10, null, null);
--
--insert into line_station values(1, 1, null, 10, 10, null, null);
--insert into line_station values(1, 2, 1, 10, 10, null, null);
--insert into line_station values(1, 3, 2, 10, 10, null, null);
--insert into line_station values(1, 4, 3, 10, 10, null, null);
--insert into line_station values(2, 6, 5, 10, 10, null, null);
--insert into line_station values(3, 7, 2, 10, 1, null, null);
--insert into line_station values(3, 4, 7, 10, 1, null, null);
--
--insert into line_station values(1, 1, 2, 10, 10, null, null);
--insert into line_station values(1, 2, 3, 10, 10, null, null);
--insert into line_station values(1, 3, 4, 10, 10, null, null);
--insert into line_station values(2, 5, 6, 10, 10, null, null);
--insert into line_station values(3, 2, 7, 10, 1, null, null);
--insert into line_station values(3, 7, 4, 10, 1, null, null);
