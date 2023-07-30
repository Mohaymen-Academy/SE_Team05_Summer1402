DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
create table users
(
    id           serial primary key,
    full_name    varchar(100),
    user_name    varchar(100) not null unique,
    email        varchar(100) not null unique,
    phone_number varchar(20)  not null unique,
    bio          text,
    deleted_at timestamptz          default null

);
create table profile_images
(
    user_id           serial primary key references users (id) on update cascade on delete cascade,
    profile_image_url text        not null default '',--TODO:
    created_at        timestamptz not null default current_timestamp
);
create table chat_types(
    id    serial primary key,
    type_name varchar(100) not null
);
create table chats
(
    id         serial primary key,
    title text,
    link text,
    chat_type serial references chat_types(id),
    created_at timestamptz not null default current_timestamp,
    deleted_at timestamptz          default null
);
create table user_chat
(
    id      serial primary key,
    user_id serial references users (id) on update cascade on delete cascade,
    chat_id serial references chats (id) on update cascade on delete cascade
);
create table permissions
(
    id    serial primary key,
    title varchar(100) not null
);
create table user_chat_permission
(
    user_chat_id  serial references user_chat (id) on update cascade on delete cascade,
    permission_id serial references permissions (id) on update cascade on delete cascade,
    primary key (user_chat_id, permission_id)
);
create table message_types(
    id    serial primary key,
    type_name varchar(100) not null
);
create table messages
(
    id serial primary key,
    data       text not null,
    message_type  serial references message_types(id) on update cascade on delete cascade,
    sent_at  timestamptz default current_timestamp,
    edited_at  timestamptz,
    deleted_at timestamptz,
    sender_id serial not null references users(id) on update cascade on delete cascade,
    chat_id serial not null references chats(id) on update cascade on delete cascade,
    reply_id serial references messages(id) default null
);
create table read_message
(
    user_id serial references users (id) on update cascade on delete cascade,
    message_id serial references messages (id) on update cascade on delete cascade,
    read_at timestamptz not null default current_timestamp
);
