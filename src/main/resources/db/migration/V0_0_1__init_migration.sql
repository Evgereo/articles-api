create table if not exists users
(
    user_id      serial       not null primary key,
    user_name    varchar(255) not null,
    user_surname varchar(255) not null,
    age          integer      not null,
    email        varchar(255) not null unique,
    password     varchar(255) not null
);

create table if not exists articles
(
    article_id   serial       not null primary key,
    author_id    integer,
    article_name varchar(255) not null,
    article_text varchar(255),
    posting_date timestamp    not null
);

create table if not exists roles
(
    role_id   serial      not null primary key,
    role_name varchar(20) not null unique
);

create table if not exists users_roles
(
    role_id integer not null,
    user_id integer not null,
    primary key (role_id, user_id)
);

alter table if exists articles
    add constraint articles_users_fk foreign key (author_id) references users;
alter table if exists users_roles
    add constraint users_roles_roles_fk foreign key (role_id) references roles;
alter table if exists users_roles
    add constraint users_roles_users_fk foreign key (user_id) references users;
insert into roles (role_name)
values ('ROLE_USER'),
       ('ROLE_ADMIN'),
       ('ROLE_MODER'),
       ('ROLE_OWNER');