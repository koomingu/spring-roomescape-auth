-- member
INSERT INTO member (id, email, password, name)
VALUES (1, 'user1@email.com', 'password123', 'User1'),
       (2, 'user2@email.com', 'password123', 'User2'),
       (3, 'user3@email.com', 'password123', 'User3'),
       (4, 'user4@email.com', 'password123', 'User4'),
       (5, 'user5@email.com', 'password123', 'User5'),
       (6, 'user6@email.com', 'password123', 'User6'),
       (7, 'user7@email.com', 'password123', 'User7'),
       (8, 'user8@email.com', 'password123', 'User8'),
       (9, 'user9@email.com', 'password123', 'User9'),
       (10, 'user10@email.com', 'password123', 'User10'),
       (11, 'user11@email.com', 'password123', 'User11'),
       (12, 'user12@email.com', 'password123', 'User12'),
       (13, 'user13@email.com', 'password123', 'User13'),
       (14, 'user14@email.com', 'password123', 'User14'),
       (15, 'user15@email.com', 'password123', 'User15'),
       (16, 'user16@email.com', 'password123', 'User16'),
       (17, 'user17@email.com', 'password123', 'User17'),
       (18, 'user18@email.com', 'password123', 'User18');

-- themes
INSERT INTO themes (id, name, description, thumbnail)
VALUES (1, 'Theme A', 'Desc A', 'https://picsum.photos/id/1011/200/300'),
       (2, 'Theme B', 'Desc B', 'https://picsum.photos/id/1015/200/300'),
       (3, 'Theme C', 'Desc C', 'https://picsum.photos/id/1025/200/300'),
       (4, 'Theme D', 'Desc D', 'https://picsum.photos/id/1035/200/300'),
       (5, 'Theme E', 'Desc E', 'https://picsum.photos/id/1043/200/300'),
       (6, 'Theme F', 'Desc F', 'https://picsum.photos/id/1050/200/300');

-- reservation_time
INSERT INTO reservation_time (id, start_at)
VALUES (1, '10:00:00'),
       (2, '11:00:00'),
       (3, '12:00:00'),
       (4, '13:00:00'),
       (5, '14:00:00'),
       (6, '15:00:00'),
       (7, '16:00:00'),
       (8, '17:00:00');

-- reservation
-- 2026-05-01 ~ 2026-05-06 사이 예약
-- Theme A: 6건
INSERT INTO reservation (id, member_id, date, time_id, theme_id)
VALUES (1, 1, '2026-05-01', 1, 1),
       (2, 2, '2026-05-02', 2, 1),
       (3, 3, '2026-05-03', 3, 1),
       (4, 4, '2026-05-04', 4, 1),
       (5, 5, '2026-05-05', 1, 1),
       (6, 6, '2026-05-06', 5, 1);

-- Theme B: 4건
INSERT INTO reservation (id, member_id, date, time_id, theme_id)
VALUES (7, 7, '2026-05-01', 2, 2),
       (8, 8, '2026-05-02', 3, 2),
       (9, 9, '2026-05-06', 4, 2),
       (10, 10, '2026-05-03', 5, 2);

-- Theme C: 2건
INSERT INTO reservation (id, member_id, date, time_id, theme_id)
VALUES (11, 11, '2026-05-06', 1, 3),
       (12, 12, '2026-05-02', 2, 3);

-- Theme D: 1건
INSERT INTO reservation (id, member_id, date, time_id, theme_id)
VALUES (13, 13, '2026-05-05', 3, 4);

-- Theme E: 3건
INSERT INTO reservation (id, member_id, date, time_id, theme_id)
VALUES (14, 14, '2026-05-02', 6, 5),
       (15, 15, '2026-05-04', 7, 5),
       (16, 16, '2026-05-06', 8, 5);

-- Theme F: 2건
INSERT INTO reservation (id, member_id, date, time_id, theme_id)
VALUES (17, 17, '2026-05-03', 6, 6),
       (18, 18, '2026-05-05', 7, 6);

ALTER TABLE member
    ALTER COLUMN id RESTART WITH 20;
ALTER TABLE themes
    ALTER COLUMN id RESTART WITH 20;
ALTER TABLE reservation_time
    ALTER COLUMN id RESTART WITH 20;
ALTER TABLE reservation
    ALTER COLUMN id RESTART WITH 40;
