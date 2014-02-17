alter table EXT_COMPANY add column AREAD_ID uuid^
alter table EXT_COMPANY add column VILLA_ID uuid^
alter table EXT_COMPANY add column PARENT_COMPANY_ID uuid^

alter table EXT_COMPANY add constraint FK_EXT_COMPANY_EXT_AREAD
       foreign key (AREAD_ID) references EXT_AREAD(ID)^

alter table EXT_COMPANY add constraint FK_EXT_COMPANY_EXT_VILLA
       foreign key (VILLA_ID) references EXT_VILLA(ID)^

alter table EXT_COMPANY add constraint FK_EXT_COMPANY_EXT_COMPANY
       foreign key (PARENT_COMPANY_ID) references EXT_COMPANY(COMPANY_ID)^