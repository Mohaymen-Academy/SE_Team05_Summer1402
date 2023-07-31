--q1: Add user (insert) password:pass
insert into users (full_name, user_name,password, email, phone_number, bio)
values ('Ali Ahmadi', 'ali698','d74ff0ee8da3b9806b18c877dbf29bbde50b5bd8e4dad7a3a725000feb82e8f1', 'ali.ahmadi@gmail.com', '09112552550', null),
       ('Hossein Razi', 'hosraz','d74ff0ee8da3b9806b18c877dbf29bbde50b5bd8e4dad7a3a725000feb82e8f1', 'hossein.razi@gmail.com', '09381234523', 'sth');

--q2: Delete account (delete)
update users
set deleted_at=current_timestamp
where user_name = 'ali698';

--q3 Change Bio (update)
update users
set bio='some bio'
where user_name = 'ali698';

--q4 Send message (insert)
insert into chats (title, link, chat_type)
values (null, null, (select id from chat_types where type_name = 'PV'));
insert into user_chat (user_id, chat_id)
values ((select id from users where user_name = 'ali698'), 1),
       ((select id from users where user_name = 'hosraz'), 1);
insert into user_chat_permission (user_chat_id, permission_id)
values (1, (select id from permissions where title = 'message')),
         (1,(select id from permissions where title='image')),
         (1,(select id from permissions where title='video')),
         (1,(select id from permissions where title='voice')),
         (1,(select id from permissions where title='file')),
          (2,(select id from permissions where title='message')),
         (2,(select id from permissions where title='image')),
         (2,(select id from permissions where title='video')),
         (2,(select id from permissions where title='voice')),
         (2,(select id from permissions where title='file'));

insert into messages (data, message_type, sender_id, chat_id)
values ('Salam', (Select id from message_types where type_name = 'message'),
        (select id from users where user_name = 'hosraz'), 1);

--q5: Edit message (update)
update messages
set data='Salam Khobi?',
    edited_at=current_timestamp
where id = 1;

--q6: Delete message (delete)
update messages
set deleted_at=current_timestamp
where id = 1;

--q7: Get all messages of a user
select m.*
from messages m
where m.sender_id = (select id from users where user_name = 'hosraz')
  and m.deleted_at is null;

--q8: Number of messages of a user
select count(m.id)
from messages m
where m.sender_id = (select id from users where user_name = 'hosraz')
  and m.deleted_at is null;


--q9: Number of users has relationship with user X

with x_username as (select 'hosraz')
select count(u.*) as user_count
from users u
         inner join user_chat uc on u.id = uc.user_id
         inner join chats c on c.id = uc.chat_id
where c.id in (select uxc.chat_id
               from users ux
                        inner join user_chat uxc on ux.id = uxc.user_id
               where ux.user_name = (select * from x_username)
)  and c.deleted_at is null
  and u.deleted_at is null
    and u.user_name <>  (select * from x_username);



--q10: Average number of messages sent by a single user
select count(m.id)::decimal / (select count(id) from users where deleted_at is null)
from messages m
where m.deleted_at is null;


--bonus
insert into read_message (user_id, message_id, read_at)
values ((select id from users where user_name='ali698'),1,current_timestamp);