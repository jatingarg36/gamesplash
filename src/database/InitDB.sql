DROP DATABASE IF EXISTS gamesplash;
CREATE DATABASE gamesplash;

USE gamesplash;

DROP TABLE IF EXISTS users;

CREATE TABLE users (
    user_id int NOT NULL AUTO_INCREMENT,
    username varchar(255) NOT NULL,
--     email varchar(255) check (email like '%_@_%_._%'),
    hashed_password varchar(255) NOT NULL,
    salt varchar(255) NOT NULL,
    isActive boolean NOT NULL DEFAULT false,
    status enum('AVAILABLE','BUSY','IN_MATCH','IN_PRACTICE','AWAY') NOT NULL DEFAULT 'AVAILABLE',
    score int NOT NULL DEFAULT 0,
    rank int,
    badge enum('FRESHMAN','BEGINNER','INTERMEDIATE','ADVANCED','EXPERT','GURU','HEADMASTER') NOT NULL DEFAULT 'FRESHMAN',
    joined_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_visited timestamp,
    PRIMARY KEY (user_id),
    UNIQUE (username),
    check (score>=0)
);

DROP TABLE IF EXISTS matches;

CREATE TABLE matches(
    match_id int NOT NULL AUTO_INCREMENT,
--    game enum('SUDOKU') NOT NULL DEFAULT 'SUDOKU',
--    match_type enum('private','open','tournament') NOT NULL,
    player_started int NOT NULL,
    difficulty enum('EASY','MEDIUM','HARD','EXTREME') NOT NULL,
    winner int,
    isLive boolean NOT NULL DEFAULT true,
    canJoin boolean NOT NULL DEFAULT true,
    status enum('NOT_STARTED','ENDED','STARTED','TERMINATED') NOT NULL DEFAULT 'NOT_STARTED',
    max_participation int NOT NULL default 1,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    start_time timestamp,
    end_time timestamp,
    PRIMARY KEY(match_id),
    FOREIGN KEY(player_started) REFERENCES users(user_id),
    FOREIGN KEY(winner) REFERENCES users(user_id),
    CHECK (max_participation <= 16)
);

DROP TABLE IF EXISTS codes;

CREATE TABLE codes(
    code_id int NOT NULL AUTO_INCREMENT,
    match_id int NOT NULL,
    code varchar(8) NOT NULL,
    PRIMARY KEY(code_id),
    UNIQUE(code),
    FOREIGN KEY(match_id) REFERENCES matches(match_id)
);

DROP TABLE IF EXISTS participants;

CREATE TABLE participants(
    participation_id int NOT NULL AUTO_INCREMENT,
    match_id int NOT NULL,
    player_id int NOT NULL,
    hasLeft boolean NOT NULL DEFAULT false,
    duration TIME DEFAULT 0, -- ( 0 for leaving in between or not completing )
    rank int,   -- ( null for leaving in between or not completing )
    score int NOT NULL DEFAULT 0,
    PRIMARY KEY(participation_id),
    FOREIGN KEY(match_id) REFERENCES matches(match_id),
    FOREIGN KEY(player_id) REFERENCES users(user_id),
    CHECK (score>=0)
);

DROP TABLE IF EXISTS self_practice;

CREATE TABLE self_practice(
    practice_id int NOT NULL AUTO_INCREMENT,
    player_id int NOT NULL,
    start_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    end_time timestamp,
    difficulty enum('EASY','MEDIUM','HARD','EXTREME') NOT NULL,
    score int NOT NULL DEFAULT 0,
    PRIMARY KEY(practice_id),
    FOREIGN KEY(player_id) REFERENCES users(user_id),
    CHECK(score>=0)
);
