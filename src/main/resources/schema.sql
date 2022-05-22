CREATE TABLE IF NOT EXISTS Station
(
    id   bigint       not null auto_increment,
    name varchar(255) not null unique,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS Line
(
    id         bigint       not null auto_increment,
    name       varchar(255) not null unique,
    color      varchar(20)  not null,
    extra_fare bigint,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS Section
(
    id              bigint not null auto_increment,
    line_id         bigint not null,
    up_station_id   bigint not null,
    down_station_id bigint not null,
    distance        int,
    primary key (id),
    constraint section_line_id_fk foreign key (line_id) references LINE (id)
        on update cascade on delete cascade
);

insert into line(name, color, extra_fare)
values ('2호선', 'bg-200-green', 900);
insert into line(name, color, extra_fare)
values ('3호선', 'bg-300-orange', 800);


insert into section(line_id, up_station_id, down_station_id, distance)
values (1, 1, 2, 10);
insert into section(line_id, up_station_id, down_station_id, distance)
values (1, 4, 5, 10);
insert into section(line_id, up_station_id, down_station_id, distance)
values (1, 3, 4, 10);
insert into section(line_id, up_station_id, down_station_id, distance)
values (1, 2, 3, 10);

insert into section(line_id, up_station_id, down_station_id, distance)
values (2, 6, 7, 10);
insert into section(line_id, up_station_id, down_station_id, distance)
values (2, 7, 8, 10);
insert into section(line_id, up_station_id, down_station_id, distance)
values (2, 8, 9, 10);
