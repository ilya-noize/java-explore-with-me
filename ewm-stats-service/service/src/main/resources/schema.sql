create table if not exists stat
(
    id bigint generated always as identity primary key,
    app varchar not null,
    uri varchar not null,
    ip varchar not null,
    created timestamp without time zone not null
);