CREATE TABLE products
(
    id          serial primary key,
    material_id varchar(16) not null unique, 
    name        varchar(255) not null,
    price       decimal(10, 2) not null,
    currency    varchar(3) not null,
    category    varchar(255) not null check (category in ('PerformanceMaterials', 'Coatings', 'Plastics')),
    created_at  timestamp default now(),
    created_by  varchar(255) null,
    last_update timestamp default now(),
    last_update_by varchar(255) null
);

