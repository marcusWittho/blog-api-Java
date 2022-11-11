create table if not exists tb_user (
    id int(11) auto_increment,
    username varchar(255),
    email varchar(255),
    password varchar(255),
    primary key (id)
);

create table if not exists tb_posts (
    id int(11) auto_increment,
    title varchar(255),
    comments varchar(600),
    post_date varchar(50),
    user_id int(11),
    primary key (id),
    foreign key (user_id) references tb_user(id)
);
