--q1: Add user (insert) password:pass
INSERT INTO users (full_name, user_name, password, email, phone_number, bio)
VALUES ('Ali Ahmadi', 'ali698', 'd74ff0ee8da3b9806b18c877dbf29bbde50b5bd8e4dad7a3a725000feb82e8f1',
        'ali.ahmadi@gmail.com', '09112552550', NULL),
       ('Hossein Razi', 'hosraz', 'd74ff0ee8da3b9806b18c877dbf29bbde50b5bd8e4dad7a3a725000feb82e8f1',
        'hossein.razi@gmail.com', '09381234523', 'sth');


--q2: Delete account (delete)
UPDATE users
SET deleted_at = CURRENT_TIMESTAMP
WHERE user_name = 'ali698';


--q3 Change Bio (update)
UPDATE users
SET bio = 'some bio'
WHERE user_name = 'ali698';


--q4 Send message (insert)
INSERT INTO chats (title, link, chat_type)
VALUES (NULL, NULL, (SELECT id FROM chat_types WHERE type_name = 'PV'));
INSERT INTO user_chat (user_id, chat_id)
VALUES ((SELECT id FROM users WHERE user_name = 'ali698'), 1),
       ((SELECT id FROM users WHERE user_name = 'hosraz'), 1);
INSERT INTO user_chat_permISsion (user_chat_id, permISsion_id)
VALUES (1, (SELECT id FROM permISsions WHERE title = 'message')),
       (1, (SELECT id FROM permISsions WHERE title = 'image')),
       (1, (SELECT id FROM permISsions WHERE title = 'video')),
       (1, (SELECT id FROM permISsions WHERE title = 'voice')),
       (1, (SELECT id FROM permISsions WHERE title = 'file')),
       (2, (SELECT id FROM permISsions WHERE title = 'message')),
       (2, (SELECT id FROM permISsions WHERE title = 'image')),
       (2, (SELECT id FROM permISsions WHERE title = 'video')),
       (2, (SELECT id FROM permISsions WHERE title = 'voice')),
       (2, (SELECT id FROM permISsions WHERE title = 'file'));
INSERT INTO messages (data, message_type, sender_id, chat_id)
VALUES ('Salam', (SELECT id FROM message_types WHERE type_name = 'message'),
        (SELECT id FROM users WHERE user_name = 'hosraz'), 1);


--q5: Edit message (update)
UPDATE messages
SET data      = 'Salam Khobi?',
    edited_at = CURRENT_TIMESTAMP
WHERE id = 1;


--q6: Delete message (delete)
UPDATE messages
SET deleted_at = CURRENT_TIMESTAMP
WHERE id = 1;


--q7: Get all messages of a user
SELECT m.*
FROM messages m
WHERE m.sender_id = (SELECT id FROM users WHERE user_name = 'hosraz')
  AND m.deleted_at IS NULL;


--q8: Number of messages of a user
SELECT COUNT(m.id)
FROM messages m
WHERE m.sender_id = (SELECT id FROM users WHERE user_name = 'hosraz')
  AND m.deleted_at IS NULL;


--q9: Number of users has relationship with user X
WITH x_username AS (SELECT 'hosraz')
SELECT COUNT(u.*) AS user_count
FROM users u
         INNER JOIN user_chat uc ON u.id = uc.user_id
         INNER JOIN chats c ON c.id = uc.chat_id
WHERE c.id IN (SELECT uxc.chat_id
               FROM users ux
                        INNER JOIN user_chat uxc ON ux.id = uxc.user_id
               WHERE ux.user_name = (SELECT * FROM x_username)
)
  AND c.deleted_at IS NULL
  AND u.deleted_at IS NULL
  AND u.user_name <> (SELECT * FROM x_username);


--q10: Average number of messages sent by a single user
SELECT COUNT(m.id)::DECIMAL / (SELECT COUNT(id) FROM users WHERE deleted_at IS NULL)
FROM messages m
WHERE m.deleted_at IS NULL;


--bonus
INSERT INTO read_message (user_id, message_id, read_at)
VALUES ((SELECT id FROM users WHERE user_name = 'ali698'), 1, CURRENT_TIMESTAMP);