create table EXT_MODULL(
ID uuid,
TOTAL varchar(50),          -- Итого
NAME varchar(200),          -- Продукт
COUNT varchar(50),          -- Количество
PRICE varchar(50),          -- Цена
TOTAL_COST varchar(50),     -- Общая стоимость

CREATE_TS timestamp,     --Когда создано (системное поле)
CREATED_BY varchar(50),  --Кем  создано (системное поле)
VERSION integer,         --Версия (системное поле)
UPDATE_TS timestamp,     --Когда было последнее изменение (системное поле)
UPDATED_BY varchar(50),  --Кто последний раз изменил сущность(системное поле)
DELETE_TS timestamp,     --Когда удалено (системное поле)
DELETED_BY varchar(50),  --Кем удалено (системное поле)
primary key (ID)
)^
