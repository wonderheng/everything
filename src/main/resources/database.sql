-- 创建数据库
create database if not exists everything;

-- 重建索引
drop table if exists file_index;

-- 创建数据表
create table if not exists file_index
(
  name      varchar(256)  not null comment '文件名',
  pinyin    varchar(1024) not null,
  file_type varchar(64)   null,
  path      varchar(1024) not null comment '文件路径',
  depth     int           not null comment '文件深度'
);