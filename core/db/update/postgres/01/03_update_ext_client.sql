ALTER TABLE EXT_CLIENT DROP CONSTRAINT FK_EXT_CLIENT_DF_COMPANY^
ALTER TABLE EXT_CLIENT DROP CONSTRAINT FK_EXT_CLIENT_EXT_SUBDIVISION^
ALTER TABLE EXT_CLIENT DROP COLUMN FULLNAME^
ALTER TABLE EXT_CLIENT DROP COLUMN SUBDIVISION_ID^
ALTER TABLE EXT_CLIENT ADD CONSTRAINT FK_EXT_CLIENT_EXT_COMPANY
       FOREIGN KEY (COMPANY_ID) REFERENCES EXT_COMPANY(COMPANY_ID)^
