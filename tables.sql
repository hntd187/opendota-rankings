CREATE TABLE leagues (
    leagueid bigint PRIMARY KEY,
    ticket varchar(255),
    banner varchar(255),
    tier varchar(255),
    name varchar(255)
)
CREATE TABLE matches (
    match_id bigint primary key,
    start_time bigint,
    leagueid bigint,
    radiant_team_id bigint,
    dire_team_id bigint,
    radiant_win boolean
)
CREATE TABLE players (
    account_id bigint primary key,
    name varchar(255),
    country_code varchar(2),
    fantasy_role integer,
    team_id integer,
    team_name varchar(255),
    team_tag varchar(255),
    is_locked boolean,
    is_pro boolean,
    locked_until integer,
)
CREATE TABLE ratings (
    ratingid bigint primary key,
    rating integer,
    account_id bigint,
    deviation integer,
    sigma double precision,
    created timestamp with time zone,
)
CREATE TABLE ratings (
    ratingid bigint primary key,
    rating integer,
    team_id bigint,
    deviation integer,
    sigma double precision,
    created timestamp with time zone,
)
CREATE TABLE teams (
    team_id bigint primary key,
    name varchar(255),
    tag varchar(255),
)
