delete from user_role;
delete from consultation;
delete from usr;

insert into usr(id, active, password, username, email) values(1, true, '123', 'lol', 'aroma7752424@gmial.com');
insert into usr(id, active, password, username, email) values(2, true, '123', 'lolMaster', 'aroma7752424@gmial.com');
insert into usr(id, active, password, username, email) values(3, true, '123', 'lolAdmin', 'aroma7752424@gmail.com');

insert into user_role(user_id, roles) values(1, 'STUDENT');
insert into user_role(user_id, roles) values(2, 'MASTER');
insert into user_role(user_id, roles) values(3, 'ADMIN');

insert into consultation(id, date, time, user_who_created_id, user_who_reserved) values(3,'2020-08-20', '19:00:00', 2, 1);
insert into consultation(id, date, time, user_who_created_id) values(4,'2020-08-11', '12:00:00', 2);