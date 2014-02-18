create table EXT_JOURNALV(
ID uuid,
DATE_JV timestamp,
NUMBER_JV varchar(20),
SOMEONE varchar(100),
CONTENTV varchar(100),
CONTACT_JV varchar(50),
STATUSV varchar(100),
CREATE_TS timestamp,     --Когда создано (системное поле)
CREATED_BY varchar(50),  --Кем  создано (системное поле)
VERSION integer,         --Версия (системное поле)
UPDATE_TS timestamp,     --Когда было последнее изменение (системное поле)
UPDATED_BY varchar(50),  --Кто последний раз изменил сущность(системное поле)
DELETE_TS timestamp,     --Когда удалено (системное поле)
DELETED_BY varchar(50),  --Кем удалено (системное поле)
primary key (ID)
)^

create table EXT_JOURNALI(
ID uuid,
DATE_JI timestamp,
NUMBER_JI varchar(20),
WHOM varchar(100),
CONTENTI varchar(100),
CONTACT_JI varchar(50),
STATUSI varchar(100),
CREATE_TS timestamp,     --Когда создано (системное поле)
CREATED_BY varchar(50),  --Кем  создано (системное поле)
VERSION integer,         --Версия (системное поле)
UPDATE_TS timestamp,     --Когда было последнее изменение (системное поле)
UPDATED_BY varchar(50),  --Кто последний раз изменил сущность(системное поле)
DELETE_TS timestamp,     --Когда удалено (системное поле)
DELETED_BY varchar(50),  --Кем удалено (системное поле)
primary key (ID)
)^

create table EXT_CONTRACTPP(
CARD_ID uuid,
COMPANY_ID uuid,
CLIENT_ID uuid,
NUMBER varchar(20),
SITY varchar(100),
DATE_CONTR timestamp,
AMOUNT varchar(50),
HEAD_ID uuid,
primary key (CARD_ID)
)^
alter table EXT_CONTRACTPP add constraint FK_EXT_CONTRACTPP_DOC
    foreign key (CARD_ID) references DF_DOC(CARD_ID)^


insert into DF_DOC_TYPE (ID, CREATE_TS, CREATED_BY, NAME, DISCRIMINATOR)
values ('46ffadc4-3b6a-11e1-9e84-47fae3df0838', current_timestamp, 'admin', 'ext$ContractPP', 130)
^

insert into SYS_CATEGORY (ID, NAME, ENTITY_TYPE, IS_DEFAULT, CREATE_TS, CREATED_BY, VERSION, DISCRIMINATOR)
values ('51bb9e98-3b89-11e1-840f-b76bc93cc547', 'Договор ПП Парус', 'ext$ContractPP', false, now(), USER, 1, 1)
^

insert into DF_DOC_KIND (category_id, create_ts, created_by, version, doc_type_id, fields_xml, numerator_type, category_attrs_place)
values ('51bb9e98-3b89-11e1-840f-b76bc93cc547', '2011-01-10 00:00:00.00', 'admin', 1, '46ffadc4-3b6a-11e1-9e84-47fae3df0838',
'<?xml version="1.0" encoding="UTF-8"?>

<fields>
<field name="number" visible="true" required="false"/>
<field name="budgetItem" visible="true" required="false"/>
<field name="contractor" visible="true" required="false"/>
<field name="parentCard" visible="true" required="false"/>
<field name="paymentDate" visible="true" required="false"/>
<field name="amount" visible="true" required="false"/>
<field name="comment" visible="true" required="false"/>
</fields>'
, null, 1)^

create table EXT_HEAD(
ID uuid,
COMPANY_ID uuid,           -- Юридическое лицо
NAME varchar(100),              -- Отображаемое имя
SURNAME varchar(100),           -- Фамилия
NAMEH varchar(60),              -- Имя
PATRONYMIC varchar(100),        -- Отчество
AREAD_ID uuid,                  -- Район
POST varchar(100),              -- Должность  +
NUMBER_TEL varchar(20),         -- Номер телефона
FAXS varchar(20),               -- Факс +
DATE_BIRTH timestamp,           -- Дата рождения
EMAIL varchar(100),            -- Электронный адрес +
SURNAME_R varchar(100),         -- Фамилия в родительном падеже
NAME_R varchar(60),             -- Имя в родительном падеже
PATRONYMIC_R varchar(100),      -- Отчество в родительном падеже
SEX varchar(10),                -- Пол

CREATE_TS timestamp,     --Когда создано (системное поле)
CREATED_BY varchar(50),  --Кем  создано (системное поле)
VERSION integer,         --Версия (системное поле)
UPDATE_TS timestamp,     --Когда было последнее изменение (системное поле)
UPDATED_BY varchar(50),  --Кто последний раз изменил сущность(системное поле)
DELETE_TS timestamp,     --Когда удалено (системное поле)
DELETED_BY varchar(50),  --Кем удалено (системное поле)
primary key (ID))^

create table EXT_RESPONSIBLE(
ID uuid,
REPUBLIC_ID uuid,              -- Район
AREAD_ID uuid,                  -- Район
VILLA_ID uuid,                  -- Село
RESPONSIBLE varchar(100),       -- Ответственный
POST varchar(100),              -- Должность +
NUMBER_TEL varchar(20),         -- Номер телефона
FAXS varchar(20),               -- Факс       +
E_MAIL varchar(100),            -- Электронная почта +
POSTAL_ADRESS varchar(200),      -- Почтовый адрес +

CREATE_TS timestamp,     --Когда создано (системное поле)
CREATED_BY varchar(50),  --Кем  создано (системное поле)
VERSION integer,         --Версия (системное поле)
UPDATE_TS timestamp,     --Когда было последнее изменение (системное поле)
UPDATED_BY varchar(50),  --Кто последний раз изменил сущность(системное поле)
DELETE_TS timestamp,     --Когда удалено (системное поле)
DELETED_BY varchar(50),  --Кем удалено (системное поле)
primary key (ID))^

create table EXT_REPUBLIC (
 ID uuid,
 REGIONCODE varchar(5),
 REGIONNAME varchar(50),

 CREATE_TS timestamp,     --Когда создано (системное поле)
 CREATED_BY varchar(50),  --Кем  создано (системное поле)
 VERSION integer,         --Версия (системное поле)
 UPDATE_TS timestamp,     --Когда было последнее изменение (системное поле)
 UPDATED_BY varchar(50),  --Кто последний раз изменил сущность(системное поле)
 DELETE_TS timestamp,     --Когда удалено (системное поле)
 DELETED_BY varchar(50),  --Кем удалено (системное поле)
 primary key (ID))^

create table EXT_AREAD (
ID uuid,
AREADNAME varchar(100),
REPUBLIC uuid,

 CREATE_TS timestamp,     --Когда создано (системное поле)
   CREATED_BY varchar(50),  --Кем  создано (системное поле)
   VERSION integer,         --Версия (системное поле)
   UPDATE_TS timestamp,     --Когда было последнее изменение (системное поле)
   UPDATED_BY varchar(50),  --Кто последний раз изменил сущность(системное поле)
   DELETE_TS timestamp,     --Когда удалено (системное поле)
   DELETED_BY varchar(50),  --Кем удалено (системное поле)
   primary key (ID))^

create table EXT_VILLA (
ID uuid,
VILLANAME varchar(100),
REPUBLIC uuid,
AREAD uuid,
INDIVIDUAL_ID uuid,

CREATE_TS timestamp,     --Когда создано (системное поле)
  CREATED_BY varchar(50),  --Кем  создано (системное поле)
  VERSION integer,         --Версия (системное поле)
  UPDATE_TS timestamp,     --Когда было последнее изменение (системное поле)
  UPDATED_BY varchar(50),  --Кто последний раз изменил сущность(системное поле)
  DELETE_TS timestamp,     --Когда удалено (системное поле)
  DELETED_BY varchar(50),  --Кем удалено (системное поле)
  primary key (ID))^

alter table EXT_AREAD
add constraint FK_EXT_AREAD_REPUBLIC
foreign key (REPUBLIC) references EXT_REPUBLIC(ID)^

alter table EXT_VILLA
add constraint FK_EXT_VILLA_AREAD
foreign key (AREAD) references EXT_AREAD(ID)^

alter table EXT_VILLA
add constraint FK_EXT_VILLA_REPUBLIC
foreign key (REPUBLIC) references EXT_REPUBLIC(ID)^

alter table EXT_VILLA add constraint FK_EXT_VILLA_DF_INDIVIDUAL
       foreign key (INDIVIDUAL_ID) references DF_INDIVIDUAL(CONTRACTOR_ID)^

alter table EXT_RESPONSIBLE add constraint FK_EXT_RESPONSIBLE_EXT_AREAD
       foreign key (AREAD_ID) references EXT_AREAD(ID)^

alter table EXT_RESPONSIBLE add constraint FK_EXT_RESPONSIBLE_EXT_VILLA
       foreign key (VILLA_ID) references EXT_VILLA(ID)^

alter table EXT_RESPONSIBLE add constraint FK_EXT_RESPONSIBLE_EXT_REPUBLIC
       foreign key (REPUBLIC_ID) references EXT_REPUBLIC(ID)^

alter table EXT_HEAD add constraint FK_EXT_HEAD_EXT_AREAD
       foreign key (AREAD_ID) references EXT_AREAD(ID)^

create table EXT_CLIENT(
ID uuid,
FULLNAME varchar(200),            --  Полное наименование
COMPANY_ID uuid,                  -- Юридическое лицо
POST varchar(60),                 -- Должность
NUMBER_TW varchar(20),            -- Факс
NUMBER_TEL varchar(20),           -- Номер телефона
DATE_BIRTH timestamp,             -- Дата рождения
SURNAME_C varchar(100),           -- Фамилия
NAME_C varchar(60),               -- Имя
PATRONYMIC_C varchar(100),        -- Отчество
INSTITUTION varchar(100),         -- Наименование учреждения
PLACE_BIRTH varchar(200),         -- E-mail
ADDRESS varchar(200),             -- Адрес проживания
LEGAL_ADDRESS varchar(200),       -- Юридический адрес
COUNT_CHS varchar(20),            -- Индекс
NAME_CW varchar(60),              -- Сайт учреждения
BANK varchar(100),                -- Банк
INN varchar(100),                 -- ИНН
KPP varchar(100),                 -- КПП
LS varchar(100),                 -- Лицевой счет
RS varchar(100),                 -- Расчетный счет
BIK varchar(100),                 -- БИК
KS varchar(100),                 -- Корреспондентский счет
SURNAME_R varchar(100),         -- Фамилия в родительном падеже
NAME_R varchar(60),             -- Имя в родительном падеже
PATRONYMIC_R varchar(100),      -- Отчество в родительном падеже
SEX varchar(10),                -- Пол
NAME varchar(100),               -- Отображаемое имя
SUBDIVISION_ID uuid,

CREATE_TS timestamp,     --Когда создано (системное поле)
CREATED_BY varchar(50),  --Кем  создано (системное поле)
VERSION integer,         --Версия (системное поле)
UPDATE_TS timestamp,     --Когда было последнее изменение (системное поле)
UPDATED_BY varchar(50),  --Кто последний раз изменил сущность(системное поле)
DELETE_TS timestamp,     --Когда удалено (системное поле)
DELETED_BY varchar(50),  --Кем удалено (системное поле)
primary key (ID))^

alter table EXT_CLIENT add constraint FK_EXT_CLIENT_DF_COMPANY
       foreign key (COMPANY_ID) references DF_COMPANY(CONTRACTOR_ID)^

create table EXT_COMPANY(
COMPANY_ID uuid,
REPUBLIC_ID uuid,
primary key (COMPANY_ID))^

alter table EXT_COMPANY add constraint FK_EXT_COMPANY_DF_COMPANY
       foreign key (COMPANY_ID) references DF_COMPANY(CONTRACTOR_ID)^

alter table EXT_COMPANY add constraint FK_EXT_COMPANY_EXT_REPUBLIC
       foreign key (REPUBLIC_ID) references EXT_REPUBLIC(ID)^

alter table EXT_CONTRACTPP add constraint FK_EXT_CONTRACTPP_EXT_CLIENT
foreign key (CLIENT_ID) references EXT_CLIENT(ID)^

alter table EXT_CONTRACTPP add constraint FK_EXT_CONTRACTPP_EXT_HEAD
foreign key (HEAD_ID) references EXT_HEAD(ID)^

create table EXT_SUBDIVISION(
ID uuid,
NAME varchar(100),
ADDRESS_S varchar(100),
PARENT_SUBDIVISION_ID uuid,
CREATE_TS timestamp,     --Когда создано (системное поле)
CREATED_BY varchar(50),  --Кем  создано (системное поле)
VERSION integer,         --Версия (системное поле)
UPDATE_TS timestamp,     --Когда было последнее изменение (системное поле)
UPDATED_BY varchar(50),  --Кто последний раз изменил сущность(системное поле)
DELETE_TS timestamp,     --Когда удалено (системное поле)
DELETED_BY varchar(50),  --Кем удалено (системное поле)
primary key (ID)
)^


alter table EXT_SUBDIVISION add constraint FK_EXT_SUBDIVISION_EXT_SUBDIVISION
   foreign key (PARENT_SUBDIVISION_ID) references EXT_SUBDIVISION(ID)^

alter table EXT_CLIENT add constraint FK_EXT_CLIENT_EXT_SUBDIVISION
       foreign key (SUBDIVISION_ID) references EXT_SUBDIVISION(ID)^

create table EXT_CORRESPONDENT(
EXTCORRESPONDENT_ID uuid,
TYPECOMPANY_ID uuid,
FORM varchar(100),
primary key (EXTCORRESPONDENT_ID)
)^
alter table EXT_CORRESPONDENT add constraint FK_EXT_CORRESPONDENT_DF_CORRESPONDENT
     foreign key (EXTCORRESPONDENT_ID) references DF_CORRESPONDENT(ID)^


alter table EXT_SUBDIVISION add constraint FK_EXT_SUBDIVISION_EXT_CORRESPONDENT
    foreign key (ID) references EXT_CORRESPONDENT(EXTCORRESPONDENT_ID)^

create table EXT_TYPECOMPANY(
ID uuid,
TYPE varchar(100),
NAME varchar(100),
CODE varchar(50),
CREATE_TS timestamp,     --Когда создано (системное поле)
CREATED_BY varchar(50),  --Кем  создано (системное поле)
VERSION integer,         --Версия (системное поле)
UPDATE_TS timestamp,     --Когда было последнее изменение (системное поле)
UPDATED_BY varchar(50),  --Кто последний раз изменил сущность(системное поле)
DELETE_TS timestamp,     --Когда удалено (системное поле)
DELETED_BY varchar(50),  --Кем удалено (системное поле)

primary key (ID)
)^
alter table EXT_CORRESPONDENT add constraint FK_EXT_CORRESPONDENT_EXT_TYPECOMPANY
     foreign key (TYPECOMPANY_ID) references EXT_TYPECOMPANY(ID)^

alter table EXT_CONTRACTPP add constraint FK_EXT_CONTRACTPP_EXT_SUBDIVISION
     foreign key (COMPANY_ID) references EXT_SUBDIVISION(ID)^

create table EXT_TASKREQUEST (
TASK_ID uuid,
CLIENT_ID uuid,
NUMBER varchar(100),
CREATE_TS timestamp,     --Когда создано (системное поле)
CREATED_BY varchar(50),  --Кем  создано (системное поле)
VERSION integer,         --Версия (системное поле)
UPDATE_TS timestamp,     --Когда было последнее изменение (системное поле)
UPDATED_BY varchar(50),  --Кто последний раз изменил сущность(системное поле)
DELETE_TS timestamp,     --Когда удалено (системное поле)
DELETED_BY varchar(50),  --Кем удалено (системное поле)
primary key (TASK_ID)
)^

alter table EXT_TASKREQUEST
add constraint FK_EXT_TASKREQUEST_TASK
foreign key (TASK_ID) references TM_TASK(CARD_ID)^

alter table EXT_TASKREQUEST
add constraint FK_EXT_TASKREQUEST_CLIENT
foreign key (CLIENT_ID) references EXT_CLIENT(ID)^


create table EXT_CALL(
CARD_ID uuid,
CLIENT_ID uuid,
PRIORITY_ID uuid,
NAME varchar(50),
NUMBER varchar(100),
DESCRIPTION varchar(100),
REFUSE_ENABLED boolean,
REASSIGN_ENABLED boolean,
CONFIRM_REQUIRED boolean,
FINISH_DATE_PLAN timestamp,
primary key (CARD_ID)
)^

alter table EXT_CALL add constraint FK_EXT_CALL_DOC
    foreign key (CARD_ID) references DF_DOC(CARD_ID)^

insert into DF_DOC_TYPE (ID, CREATE_TS, CREATED_BY, NAME, DISCRIMINATOR)
values ('a0c6769d-f3fc-4be6-9721-05cb4f0be0ca', current_timestamp, 'admin', 'ext$Call', 150)
^

insert into SYS_CATEGORY (ID, NAME, ENTITY_TYPE, IS_DEFAULT, CREATE_TS, CREATED_BY, VERSION, DISCRIMINATOR)
values ('053ae7ee-d327-4a90-909a-f540e4357324', 'Звонок', 'ext$Call', false, now(), USER, 1, 1)
^

insert into DF_DOC_KIND (category_id, create_ts, created_by, version, doc_type_id, fields_xml, numerator_type, category_attrs_place)
values ('053ae7ee-d327-4a90-909a-f540e4357324', '2011-01-10 00:00:00.00', 'admin', 1, 'a0c6769d-f3fc-4be6-9721-05cb4f0be0ca',
'<?xml version="1.0" encoding="UTF-8"?>

<fields>
<field name="number" visible="true" required="false"/>
<field name="budgetItem" visible="true" required="false"/>
<field name="contractor" visible="true" required="false"/>
<field name="parentCard" visible="true" required="false"/>
<field name="paymentDate" visible="true" required="false"/>
<field name="amount" visible="true" required="false"/>
<field name="comment" visible="true" required="false"/>
</fields>'
, null, 1)^

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

ALTER TABLE EXT_CLIENT DROP CONSTRAINT FK_EXT_CLIENT_DF_COMPANY^
ALTER TABLE EXT_CLIENT DROP CONSTRAINT FK_EXT_CLIENT_EXT_SUBDIVISION^
ALTER TABLE EXT_CLIENT DROP COLUMN FULLNAME^
ALTER TABLE EXT_CLIENT DROP COLUMN SUBDIVISION_ID^
ALTER TABLE EXT_CLIENT ADD CONSTRAINT FK_EXT_CLIENT_EXT_COMPANY
       FOREIGN KEY (COMPANY_ID) REFERENCES EXT_COMPANY(COMPANY_ID)^

alter table EXT_COMPANY add column AREAD_ID uuid^
alter table EXT_COMPANY add column VILLA_ID uuid^
alter table EXT_COMPANY add column PARENT_COMPANY_ID uuid^
alter table EXT_COMPANY add constraint FK_EXT_COMPANY_EXT_AREAD
       foreign key (AREAD_ID) references EXT_AREAD(ID)^
alter table EXT_COMPANY add constraint FK_EXT_COMPANY_EXT_VILLA
       foreign key (VILLA_ID) references EXT_VILLA(ID)^
alter table EXT_COMPANY add constraint FK_EXT_COMPANY_EXT_COMPANY
       foreign key (PARENT_COMPANY_ID) references EXT_COMPANY(COMPANY_ID)^

ALTER TABLE EXT_VILLA ADD COLUMN TYPE_VILLA varchar(1)^
ALTER TABLE EXT_VILLA ADD COLUMN AREA_CENTER boolean^

delete from ext_contractpp where ext_contractpp.card_id in (select wf_card.id from wf_card where wf_card.category_id = '51bb9e98-3b89-11e1-840f-b76bc93cc547')^
delete from df_doc where df_doc.card_id in (select wf_card.id from wf_card where wf_card.category_id = '51bb9e98-3b89-11e1-840f-b76bc93cc547')^
delete from wf_card where wf_card.category_id = '51bb9e98-3b89-11e1-840f-b76bc93cc547'^
alter table EXT_CONTRACTPP drop constraint FK_EXT_CONTRACTPP_EXT_SUBDIVISION^
alter table EXT_CONTRACTPP drop column SITY;
alter table EXT_CONTRACTPP add column VILLA_ID uuid;
alter table EXT_CONTRACTPP add constraint FK_EXT_CONTRACTPP_EXT_VILLA
     foreign key (VILLA_ID) references EXT_VILLA(ID)^
alter table EXT_CONTRACTPP add constraint FK_EXT_CONTRACTPP_EXT_COMPANY
     foreign key (COMPANY_ID) references EXT_COMPANY(COMPANY_ID)^

alter table EXT_SUBDIVISION drop constraint FK_EXT_SUBDIVISION_EXT_SUBDIVISION^
alter table EXT_SUBDIVISION drop constraint FK_EXT_SUBDIVISION_EXT_CORRESPONDENT^
drop table EXT_SUBDIVISION^

alter table EXT_CORRESPONDENT drop constraint FK_EXT_CORRESPONDENT_DF_CORRESPONDENT^
alter table EXT_CORRESPONDENT drop constraint FK_EXT_CORRESPONDENT_EXT_TYPECOMPANY^
drop table EXT_CORRESPONDENT^

drop table EXT_TYPECOMPANY^

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

insert into DF_NUMERATOR (id, create_ts, created_by, version, name, script_enabled, numerator_format) values ('d9ea2b30-8a61-11e3-baa8-0800200c9a66', now(), USER, 1, 'Звонки', FALSE, 'Звонок-[number]');
insert into SYS_CONFIG (id, create_ts, created_by, version, name, value) values ('e2803280-8a61-11e3-baa8-0800200c9a66', now(), USER, 1, 'dept.call', 'Звонки');
^

DELETE FROM ext_taskrequest^
DELETE FROM tm_task WHERE tm_task.card_id in (select wf_card.id from wf_card where wf_card.created_by = 'oktell')^
DELETE FROM wf_assignment WHERE wf_assignment.card_id in (select wf_card.id from wf_card where wf_card.created_by = 'oktell')^
DELETE FROM wf_card_info WHERE wf_card_info.card_id in (select wf_card.id from wf_card where wf_card.created_by = 'oktell')^
DELETE FROM wf_card_role WHERE wf_card_role.card_id in (select wf_card.id from wf_card where wf_card.created_by = 'oktell')^
DELETE FROM wf_card WHERE wf_card.created_by = 'oktell'^

ALTER TABLE EXT_TASKREQUEST DROP CONSTRAINT FK_EXT_TASKREQUEST_CLIENT^
ALTER TABLE EXT_TASKREQUEST DROP CONSTRAINT FK_EXT_TASKREQUEST_TASK^

ALTER TABLE EXT_CONTRACTPP ADD COLUMN TEXTAMOUNT varchar(400);

ALTER TABLE EXT_CONTRACTPP ADD COLUMN NAMECONTRACT varchar(100);

ALTER TABLE EXT_MODULL ADD COLUMN NAMECONTRACT varchar(100);

ALTER TABLE EXT_CONTRACTPP ADD COLUMN DATECLOSE timestamp;