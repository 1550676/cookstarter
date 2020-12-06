drop table if exists users_info;
create table users_info (
id                    bigserial,
first_name            varchar(50),
last_name             varchar(50),
email                 varchar(50),

PRIMARY KEY (id)
);

drop table if exists roles;
create table roles (
id                    serial,
name                  varchar(50) not null,

PRIMARY KEY (id)
);

drop table if exists users;
create table users (
id                    bigserial,
restaurant_id         bigint,
info_id               bigint not null,
username              varchar(30) not null UNIQUE,
password              varchar(80) not null,
role_id               int not null,
enable                boolean,

PRIMARY KEY (id),
constraint fk_role_id foreign key (role_id) references roles (id),
constraint fk_restaurant_id foreign key (info_id) references users_info (id)
);




insert into roles (name)
values
('CUSTOMER'), ('RESTAURANT_MANAGER'), ('RESTAURANT_ADMIN');

insert into users_info (first_name, last_name, email)
values
('customer','customer', 'customer@gmail.com'),
('rest1','rest1', 'r1@gmail.com'),
('r2','r2', 'r2@gmail.com');


insert into users (id, restaurant_id, info_id, username, password, role_id, enable)
values
(1, 0, 1, '100', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', '1', 'true'),
(2, 304, 2, '1', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', '2', 'true'),
(3, 405, 3, '2', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', '3', 'true');
