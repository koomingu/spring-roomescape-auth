CREATE TABLE store
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE member
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name     VARCHAR(255) NOT NULL,
    role     VARCHAR(20)  NOT NULL DEFAULT 'USER',
    store_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (store_id) REFERENCES store (id) ON DELETE SET NULL
);

CREATE TABLE themes
(
    id          BIGINT        NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255)  NOT NULL UNIQUE,
    description VARCHAR(255)  NOT NULL,
    thumbnail   VARCHAR(2048) NOT NULL,
    store_id    BIGINT        NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (store_id) REFERENCES store (id) ON DELETE CASCADE
);

CREATE TABLE reservation_time
(
    id       BIGINT NOT NULL AUTO_INCREMENT,
    start_at TIME   NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE reservation
(
    id        BIGINT NOT NULL AUTO_INCREMENT,
    member_id BIGINT NOT NULL,
    date      DATE   NOT NULL,
    time_id   BIGINT,
    theme_id  BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE,
    FOREIGN KEY (time_id) REFERENCES reservation_time (id) ON DELETE CASCADE,
    FOREIGN KEY (theme_id) REFERENCES themes (id) ON DELETE CASCADE,
    CONSTRAINT unique_reservation UNIQUE (date, time_id, theme_id)
);
