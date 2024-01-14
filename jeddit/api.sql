UNLOCK TABLES;

DROP TABLE IF EXISTS `commentary`;
DROP TABLE IF EXISTS `communities`;
DROP TABLE IF EXISTS `moderators`;
DROP TABLE IF EXISTS `posts`;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `users_communities`;
DROP TABLE IF EXISTS `votes`;

CREATE TABLE commentary
(
    id bigint NOT NULL DEFAULT nextval('commentary_id_seq'::regclass),
    date timestamp(6) without time zone NOT NULL,
    text character varying(40000) COLLATE pg_catalog."default" NOT NULL,
    user_id bigint NOT NULL,
    post_id bigint NOT NULL,
    CONSTRAINT commentary_pkey PRIMARY KEY (id),
    CONSTRAINT fkhndw0f497l4ae41j5yvstssr7 FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkkc7p77s3k947b4igt491wcq5w FOREIGN KEY (post_id)
        REFERENCES public.posts (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

CREATE TABLE communities
(
    id bigint NOT NULL DEFAULT nextval('communities_id_seq'::regclass),
    title character varying(50) COLLATE pg_catalog."default" NOT NULL,
    description character varying(200) COLLATE pg_catalog."default",
    owner bigint NOT NULL DEFAULT 1,
    CONSTRAINT communities_pkey PRIMARY KEY (id),
    CONSTRAINT communities_title_key UNIQUE (title),
    CONSTRAINT owner FOREIGN KEY (owner)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)

CREATE TABLE moderators
(
    user_id bigint NOT NULL,
    community_id bigint NOT NULL,
    CONSTRAINT fk88trnuotm3t0s0ew7cnpkup4a FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkcuow933dihv9ng2xhhjpsqyxx FOREIGN KEY (community_id)
        REFERENCES public.communities (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

CREATE TABLE posts
(
    id bigint NOT NULL DEFAULT nextval('posts_id_seq'::regclass),
    title character varying(300) COLLATE pg_catalog."default" NOT NULL,
    posttext character varying(40000) COLLATE pg_catalog."default",
    postdate timestamp without time zone NOT NULL,
    userid bigint NOT NULL,
    communityid bigint,
    CONSTRAINT posts_pkey PRIMARY KEY (id),
    CONSTRAINT fk4hp4tatdat9i0vc1jxxl1956s FOREIGN KEY (communityid)
        REFERENCES public.communities (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fktc10cvjiaj3p7ldl526coc36a FOREIGN KEY (userid)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

CREATE TABLE users
(
    id bigint NOT NULL DEFAULT nextval('users_id_seq'::regclass),
    login character varying(50) COLLATE pg_catalog."default" NOT NULL,
    password character varying(255) COLLATE pg_catalog."default" NOT NULL,
    email character varying(320) COLLATE pg_catalog."default" NOT NULL,
    role character varying(20) COLLATE pg_catalog."default" NOT NULL,
    carma integer NOT NULL DEFAULT 0,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT users_email_key UNIQUE (email),
    CONSTRAINT users_login_key UNIQUE (login)
)

CREATE TABLE users_communities
(
    user_id bigint NOT NULL,
    community_id bigint NOT NULL,
    CONSTRAINT fkngo675xkpopayl7x324c4y84h FOREIGN KEY (community_id)
        REFERENCES public.communities (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fko3nmtuinc3uan1l3xltglpp78 FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

CREATE TABLE votes
(
    id bigint NOT NULL DEFAULT nextval('votes_id_seq'::regclass),
    value integer NOT NULL,
    post_id bigint NOT NULL,
    user_id bigint NOT NULL,
    CONSTRAINT votes_pkey PRIMARY KEY (id),
    CONSTRAINT fk1m2jqtro85c13ya5kv0kvkc97 FOREIGN KEY (post_id)
        REFERENCES public.posts (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkli4uj3ic2vypf5pialchj925e FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)