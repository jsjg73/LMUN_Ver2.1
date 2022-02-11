insert into location (id , address_name , category_group_code, lat, lon, place_name, road_address_name )
 values (131213, '지번 주소', 17, 1.1, 3.3, '장소 이름', '도로명 주소');

insert into user (username, password, nick, create_at, update_at) values ('user1', '$2a$10$WzeYhQjxmb3a4r13HOxWO.gZgU4H96LmEr43ZsKndYZt.HeiRh6L6', 'nick1', now(), now());
insert into user (username, password, nick, create_at, update_at) values ('user2', '$2a$10$WzeYhQjxmb3a4r13HOxWO.gZgU4H96LmEr43ZsKndYZt.HeiRh6L6', 'nick2', now(), now());
insert into user (username, password, nick, create_at, update_at) values ('user3', '$2a$10$WzeYhQjxmb3a4r13HOxWO.gZgU4H96LmEr43ZsKndYZt.HeiRh6L6', 'nick3', now(), now());
insert into user (username, password, nick, create_at, update_at) values ('user4', '$2a$10$WzeYhQjxmb3a4r13HOxWO.gZgU4H96LmEr43ZsKndYZt.HeiRh6L6', 'nick4', now(), now());

insert into departure (username, location_id) values ('user1', 131213);
insert into departure (username, location_id) values ('user2', 131213);
insert into departure (username, location_id) values ('user3', 131213);
insert into departure (username, location_id) values ('user4', 131213);

-- user1이 호스트
-- user2, user3이 게스트 
-- 모임 이름 : abcdefghijklm
insert into meeting (id, name, at_least, host, create_at, update_at) values ('abcdefghijklm', '모임 이름', 3, 'user1', now(), now());
insert into participant (meeting_id, username, departure_id, create_at, update_at) value ('abcdefghijklm', 'user1', 131213, now(), now());
insert into participant (meeting_id, username, departure_id, create_at, update_at) value ('abcdefghijklm', 'user2', 131213, now(), now());
insert into participant (meeting_id, username, departure_id, create_at, update_at) value ('abcdefghijklm', 'user3', 131213, now(), now());

insert into meeting (id, name, at_least, host, create_at, update_at) values ('algorithmsStudy', '알고리즘모임byUser2', 3, 'user2', now(), now());
insert into participant (meeting_id, username, departure_id, create_at, update_at) value ('algorithmsStudy', 'user2', 131213, now(), now());