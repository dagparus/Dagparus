ALTER TABLE EXT_CONTRACTPP DROP COLUMN NAMEORGANISATION^
ALTER TABLE EXT_CONTRACTPP ADD COLUMN ORGANIZATION_ID uuid^

ALTER TABLE EXT_CONTRACTPP ADD CONSTRAINT FK_EXT_CONTACTPP_ORGANIZATION
    FOREIGN KEY (ORGANIZATION_ID) REFERENCES DF_ORGANIZATION(ID)^