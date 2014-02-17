alter table EXT_CALL drop constraint FK_EXT_CALL_DOC^
drop table EXT_CALL^

create table EXT_CALL (
CARD_ID uuid,
CLIENT_ID uuid,
PRIORITY_ID uuid,
NAME varchar(100),
TELEPHONE_NUMBER varchar(50),
DESCRIPTION varchar(500),
FINISH_DATE_PLAN timestamp,
REFUSE_ENABLED boolean,
REASSIGN_ENABLED boolean,
CONFIRM_REQUIRED boolean,

CREATE_TS timestamp,     --Когда создано (системное поле)
CREATED_BY varchar(50),  --Кем  создано (системное поле)
VERSION integer,         --Версия (системное поле)
UPDATE_TS timestamp,     --Когда было последнее изменение (системное поле)
UPDATED_BY varchar(50),  --Кто последний раз изменил сущность(системное поле)
DELETE_TS timestamp,     --Когда удалено (системное поле)
DELETED_BY varchar(50),  --Кем удалено (системное поле)
primary key (CARD_ID)
)^

alter table EXT_CALL add constraint FK_EXT_CALL_DOC
    foreign key (CARD_ID) references DF_DOC(CARD_ID)^
alter table EXT_CALL add constraint FK_EXT_CALL_EXT_CLIENT
    foreign key (CLIENT_ID) references EXT_CLIENT(ID)^