insert into location (id , address_name , category_group_code, lat, lon, place_name, road_address_name )
 values (131213, '지번 주소', 17, 1.1, 3.3, '장소 이름', '도로명 주소');

insert into user (username, password, nick) values ('user1', '$2a$10$WzeYhQjxmb3a4r13HOxWO.gZgU4H96LmEr43ZsKndYZt.HeiRh6L6', 'nick1');
insert into user (username, password, nick) values ('user2', '$2a$10$WzeYhQjxmb3a4r13HOxWO.gZgU4H96LmEr43ZsKndYZt.HeiRh6L6', 'nick2');
insert into user (username, password, nick) values ('user3', '$2a$10$WzeYhQjxmb3a4r13HOxWO.gZgU4H96LmEr43ZsKndYZt.HeiRh6L6', 'nick3');
insert into user (username, password, nick) values ('user4', '$2a$10$WzeYhQjxmb3a4r13HOxWO.gZgU4H96LmEr43ZsKndYZt.HeiRh6L6', 'nick4');

insert into departure (username, location_id) values ('user1', 131213);
insert into departure (username, location_id) values ('user2', 131213);
insert into departure (username, location_id) values ('user3', 131213);
insert into departure (username, location_id) values ('user4', 131213);

-- user1이 호스트
-- user2, user3이 게스트 
-- 모임 이름 : abcdefghijklm
insert into meeting (id, name, at_least, host) values ('abcdefghijklm', '모임 이름', 3, 'user1');
insert into participant (meeting_id, username, departure_id) value ('abcdefghijklm', 'user1', 131213);
insert into participant (meeting_id, username, departure_id) value ('abcdefghijklm', 'user2', 131213);
insert into participant (meeting_id, username, departure_id) value ('abcdefghijklm', 'user3', 131213);

insert into meeting (id, name, at_least, host) values ('algorithmsStudy', '알고리즘모임byUser2', 3, 'user2');
insert into participant (meeting_id, username, departure_id) value ('algorithmsStudy', 'user2', 131213);