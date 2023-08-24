CREATE TABLE IF NOT EXISTS files
(
    id           INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    file_name    NVARCHAR(255)   NOT NULL,
    content_type NVARCHAR(255),
    data         BLOB
);
