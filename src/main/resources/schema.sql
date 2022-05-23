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
    distance        bigint,
    primary key (id),
    constraint section_line_id_fk foreign key (line_id) references LINE (id)
        on update cascade on delete cascade
);
