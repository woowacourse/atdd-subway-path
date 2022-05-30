create table if not exists STATION
(
    id              bigint                  primary key     auto_increment,
    name            varchar(255)            not null        unique
);

create table if not exists SECTION
(
    id bigint           auto_increment      primary key,
    up_station_Id       bigint              not null,
    down_station_Id     bigint              not null,
    distance            bigint              not null,
    line_id             bigint              not null
);

create table if not exists LINE
(
    id              bigint              primary key     auto_increment,
    name            varchar(255)        not null        unique,
    color           varchar(20)         not null        unique,
    extra_fare      int                 not null
);
