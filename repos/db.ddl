create table if not exists public.users
(
    id       serial
        primary key,
    name     varchar,
    email    varchar,
    password varchar,
    role     varchar
);

alter table public.users
    owner to postgres;

create database pis_projekt_movies
    with owner postgres;
