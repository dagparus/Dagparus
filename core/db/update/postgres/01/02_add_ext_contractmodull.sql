create table EXT_CONTRACTMODULL(
ID uuid,
CONTRACTPP_ID uuid,
MODULL_ID uuid,

CREATE_TS timestamp,     --Когда создано (системное поле)
CREATED_BY varchar(50),  --Кем  создано (системное поле)
VERSION integer,         --Версия (системное поле)
UPDATE_TS timestamp,     --Когда было последнее изменение (системное поле)
UPDATED_BY varchar(50),  --Кто последний раз изменил сущность(системное поле)
DELETE_TS timestamp,     --Когда удалено (системное поле)
DELETED_BY varchar(50),  --Кем удалено (системное поле)
primary key (ID))^

alter table EXT_CONTRACTMODULL add constraint FK_EXT_CONTRACTMODULL_COMTRACTPP
    foreign key (CONTRACTPP_ID) references EXT_CONTRACTPP(CARD_ID)^

alter table EXT_CONTRACTMODULL add constraint FK_EXT_CONTRACTMODULL_EXT_MODULL
    foreign key (MODULL_ID) references EXT_MODULL(ID)^