create table if not exists file_index
(
  name      varchar(256)  null,
  pinyin    varchar(1024) null,
  file_type varchar(64)   null,
  path      varchar(1024) null,
  depth     int           null
);