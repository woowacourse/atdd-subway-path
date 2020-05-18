create table if not exists STATION
(
   id bigint auto_increment not null,
   name varchar(255) not null unique,
   created_at timestamp default current_timestamp,
   primary key(id),
   unique (name)
);

create table if not exists LINE
(
   id bigint auto_increment not null,
   name varchar(255) not null,
   start_time time not null,
   end_time time not null,
   interval_time int not null,
   created_at timestamp default current_timestamp,
   updated_at timestamp default current_timestamp on update current_timestamp,
   primary key(id),
   unique (name)
);

create table if not exists LINE_STATION
(
    line bigint not null,
    line_key bigint not null,
    pre_station_id bigint,
    station_id bigint not null,
    distance int,
    duration int,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp
);