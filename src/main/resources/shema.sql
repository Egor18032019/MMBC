create table files
(
    processing         boolean,
    id                 uuid not null,
    old_name           varchar(255),
    processing_success varchar(255),
    primary key (id)
)
