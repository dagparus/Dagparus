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