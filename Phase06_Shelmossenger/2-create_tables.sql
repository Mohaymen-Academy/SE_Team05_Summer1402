DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

CREATE TABLE users
(
    id           SERIAL PRIMARY KEY,
    full_name    VARCHAR(100),
    user_name    VARCHAR(100) NOT NULL UNIQUE,
    password     VARCHAR(100) NOT NULL,
    email        VARCHAR(100) NOT NULL UNIQUE,
    phone_number VARCHAR(20)  NOT NULL UNIQUE,
    bio          TEXT,
    deleted_at   TIMESTAMP DEFAULT NULL
);

CREATE TABLE profile_images
(
    user_id           SERIAL PRIMARY KEY REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE,
    profile_image_url TEXT      NOT NULL DEFAULT '',
    created_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE chat_types
(
    id        SERIAL PRIMARY KEY,
    type_name VARCHAR(100) NOT NULL
);

CREATE TABLE chats
(
    id         SERIAL PRIMARY KEY,
    title      TEXT,
    link       TEXT,
    chat_type  SERIAL REFERENCES chat_types (id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP          DEFAULT NULL
);

CREATE TABLE user_chat
(
    id      SERIAL PRIMARY KEY,
    user_id SERIAL REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE,
    chat_id SERIAL REFERENCES chats (id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE permissions
(
    id    SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL
);

CREATE TABLE user_chat_permission
(
    user_chat_id  SERIAL REFERENCES user_chat (id) ON UPDATE CASCADE ON DELETE CASCADE,
    permission_id SERIAL REFERENCES permissions (id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (user_chat_id, permission_id)
);

CREATE TABLE message_types
(
    id        SERIAL PRIMARY KEY,
    type_name VARCHAR(100) NOT NULL
);

CREATE TABLE messages
(
    id           SERIAL PRIMARY KEY,
    data         TEXT   NOT NULL,
    message_type SERIAL REFERENCES message_types (id) ON UPDATE CASCADE ON DELETE CASCADE,
    sent_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    edited_at    TIMESTAMP,
    deleted_at   TIMESTAMP,
    sender_id    SERIAL NOT NULL REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE,
    chat_id      SERIAL NOT NULL REFERENCES chats (id) ON UPDATE CASCADE ON DELETE CASCADE,
    reply_id     int REFERENCES messages (id)
);

CREATE TABLE read_message
(
    user_id    SERIAL REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE,
    message_id SERIAL REFERENCES messages (id) ON UPDATE CASCADE ON DELETE CASCADE,
    read_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
