insert into location (id , address_name , category_group_code, lat, lon, place_name, road_address_name )
 values (131213, '지번 주소', 17, 1.1, 3.3, '장소 이름', '도로명 주소');

insert into user (username, password, nick) values ('user1', '$2a$10$WzeYhQjxmb3a4r13HOxWO.gZgU4H96LmEr43ZsKndYZt.HeiRh6L6', 'nick1');
insert into user (username, password, nick) values ('user2', '$2a$10$WzeYhQjxmb3a4r13HOxWO.gZgU4H96LmEr43ZsKndYZt.HeiRh6L6', 'nick2');
insert into user (username, password, nick) values ('user3', '$2a$10$WzeYhQjxmb3a4r13HOxWO.gZgU4H96LmEr43ZsKndYZt.HeiRh6L6', 'nick3');

insert into user_departures (user_id, location_id) values ('user1', 131213);
insert into user_departures (user_id, location_id) values ('user2', 131213);
insert into user_departures (user_id, location_id) values ('user3', 131213);

insert into meeting (id, name, at_least, host) values ('abcdefghijklm', '모임 이름', 3, 'user1');

insert into participant (meeting_id, username) value ('abcdefghijklm', 'user1');