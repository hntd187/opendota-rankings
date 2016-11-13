CREATE TABLE IF NOT EXISTS leagues (
  leagueid BIGINT PRIMARY KEY,
  ticket   VARCHAR(255),
  banner   VARCHAR(255),
  tier     VARCHAR(255),
  name     VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS matches (
  match_id        BIGINT PRIMARY KEY,
  start_time      BIGINT,
  leagueid        BIGINT,
  radiant_team_id BIGINT,
  dire_team_id    BIGINT,
  radiant_win     BOOLEAN
);

CREATE TABLE IF NOT EXISTS players (
  account_id   BIGINT PRIMARY KEY,
  name         VARCHAR(255),
  country_code VARCHAR(2),
  fantasy_role INTEGER,
  team_id      INTEGER,
  team_name    VARCHAR(255),
  team_tag     VARCHAR(255),
  is_locked    BOOLEAN,
  is_pro       BOOLEAN,
  locked_until INTEGER
);

CREATE TABLE IF NOT EXISTS teams (
  team_id BIGINT PRIMARY KEY,
  name    VARCHAR(255),
  tag     VARCHAR(255)
);

CREATE SEQUENCE rating_id_seq;
CREATE SEQUENCE player_rating_id_seq;

CREATE TABLE IF NOT EXISTS ratings (
  ratingid  BIGINT NOT NULL PRIMARY KEY DEFAULT nextval(rating_id_seq),
  rating    INTEGER,
  team_id   BIGINT,
  deviation INTEGER,
  sigma     DOUBLE PRECISION,
  created   TIMESTAMP WITH TIME ZONE
);
CREATE TABLE IF NOT EXISTS player_ratings (
  ratingid   BIGINT NOT NULL PRIMARY KEY DEFAULT nextval(player_rating_id_seq),
  rating     INTEGER,
  account_id BIGINT,
  deviation  INTEGER,
  sigma      DOUBLE PRECISION,
  created    TIMESTAMP WITH TIME ZONE
);

ALTER SEQUENCE rating_id_seq OWNED BY ratings.ratingid;
ALTER SEQUENCE player_rating_id_seq OWNED BY player_ratings.ratingid;