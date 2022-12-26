create table if not exists USERS
(
    USER_ID  INTEGER auto_increment,
    EMAIL CHARACTER VARYING(50) not null,
    LOGIN      CHARACTER VARYING(25) not null,
    NAME  CHARACTER VARYING(25) not null,
    BIRTHDAY   DATE                  not null,
    constraint USERS_PK
        primary key (USER_ID)
);

create table if not exists GENRE
(
    GENRE_ID   INTEGER auto_increment,
    GENRE_NAME CHARACTER VARYING(20) not null,
    constraint GENRE_PK
        primary key (GENRE_ID)
);

create table if not exists MPA
(
    MPA_ID   INTEGER auto_increment,
    MPA_NAME CHARACTER VARYING(10),
    constraint MPA_PK
        primary key (MPA_ID)
);

create table if not exists FILMS
(
    FILM_ID      INTEGER auto_increment,
    NAME         CHARACTER VARYING(50)  not null,
    DESCRIPTION  CHARACTER VARYING(200) not null,
    RELEASE_DATE DATE                   not null,
    DURATION     INTEGER                not null,
    RATE             INTEGER,
    MPA_ID           INTEGER,
    constraint FILMS_PK
        primary key (FILM_ID),
    constraint FILMS_MPA_ID_FK
        foreign key (MPA_ID) references MPA
);

create table if not exists FILM_GENRE
(
    FILM  INTEGER,
    GENRE INTEGER,
    constraint FILM_GENRE_FILMS_FK
        foreign key (FILM) references FILMS,
    constraint FILM_GENRE_GENRE_FK
        foreign key (GENRE) references GENRE
);

create table if not exists FILM_LIKES
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint LIKES_FILM_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint LIKES_USER_ID_FK
        foreign key (USER_ID) references USERS
);

create table if not exists FRIENDS
(
    USER_ID   INTEGER not null,
    FRIEND_ID INTEGER,
    constraint FRIENDS_FRIEND_ID_FK
        foreign key (USER_ID) references USERS,
    constraint FRIENDS_USER_ID_FK
        foreign key (FRIEND_ID) references USERS
);