create table if not exists comments
(
    comment_id serial  not null primary key,
    parent_id  integer not null,
    article_id integer not null,
    author_id  integer not null,
    to_user_id integer not null,
    content    text    not null
);

alter table if exists comments
    add constraint comments_articles_fk foreign key (article_id) references articles;
alter table if exists comments
    add constraint comments_users_fk foreign key (author_id) references users;