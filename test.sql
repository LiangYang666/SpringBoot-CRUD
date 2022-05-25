
create table allowed_ip(
    ip vchar(15) PRIMARY KEY, -- 允许的ip
    address vchar(20) ,      -- 允许的ip所属地址
    create_time date not null -- 创建时间
);
insert into allowed_ip values('192.168.0.1', null, '2022-5-1');
select * from allowed_ip;