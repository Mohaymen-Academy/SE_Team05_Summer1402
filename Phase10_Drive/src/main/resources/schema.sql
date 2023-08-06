create table if not exists photos
(
    id           INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    file_name    nvarchar(255)   not null,
    content_type nvarchar(255),
    data         binary large object
);
