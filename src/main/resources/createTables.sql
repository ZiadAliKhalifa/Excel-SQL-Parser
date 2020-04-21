CREATE TABLE  IF NOT EXISTS  "role" (
   "id"                      uuid  NOT NULL,
   "data"                    jsonb        NOT NULL,
   "active"                  boolean NOT NULL DEFAULT TRUE,
   "creation_date"           timestamptz  NOT NULL DEFAULT now(),
   "created_by"              varchar(255)  NOT NULL,
   "last_modification_date"  timestamptz  NOT NULL DEFAULT now(),
   "last_modified_by"        varchar(255)  NOT NULL,
   CONSTRAINT PK_ROLE_ID PRIMARY KEY ("id")
);
CREATE TABLE IF NOT EXISTS "user" (
   "id"                      uuid  NOT NULL,
   "data"                    jsonb        NOT NULL,
   "active"                  boolean NOT NULL DEFAULT TRUE,
   "creation_date"           timestamptz  NOT NULL DEFAULT now(),
   "created_by"              varchar(255)  NOT NULL,
   "last_modification_date"  timestamptz  NOT NULL DEFAULT now(),
   "last_modified_by"        varchar(255)  NOT NULL,
   CONSTRAINT PK_USER_ID PRIMARY KEY ("id")
);
CREATE TABLE IF NOT EXISTS "reference_data_type"  (
   "key"            varchar(255) NOT NULL,
   "description"    varchar(1000) NOT NULL,
   CONSTRAINT PK_REFERENCE_DATA_TYPE_KEY PRIMARY KEY ("key")
);

-- lookup data
INSERT into reference_data_type ("key","description")
VALUES('CATEGORY','Category reference data')
ON CONFLICT DO NOTHING;


CREATE TABLE IF NOT EXISTS "reference_data" (
   "id"                      uuid  NOT NULL,
   "type"                    varchar(255) NOT NULL,
   "data"                    jsonb        NOT NULL,
   "active"                  boolean NOT NULL DEFAULT TRUE,
   "creation_date"           timestamptz  NOT NULL DEFAULT now(),
   "created_by"              varchar(255)  NOT NULL,
   "last_modification_date"  timestamptz  NOT NULL DEFAULT now(),
   "last_modified_by"        varchar(255)  NOT NULL,
   CONSTRAINT PK_REFERENCE_DATA_ID PRIMARY KEY (id),
   CONSTRAINT FK_REFERENCE_DATA_TYPE FOREIGN KEY ("type")
      REFERENCES REFERENCE_DATA_TYPE ("key") MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE TABLE IF NOT EXISTS "shortcut" (
   "id"                      uuid  NOT NULL,
   "data"                    jsonb        NOT NULL,
   "active"                  boolean NOT NULL DEFAULT TRUE,
   "creation_date"           timestamptz  NOT NULL DEFAULT now(),
   "created_by"              varchar(255)  NOT NULL,
   "last_modification_date"  timestamptz  NOT NULL DEFAULT now(),
   "last_modified_by"        varchar(255)  NOT NULL,
   CONSTRAINT pk_shortcut_id PRIMARY KEY ("id")
);
CREATE TABLE IF NOT EXISTS "workspace" (
   "id"                      uuid  NOT NULL,
   "data"                    jsonb        NOT NULL,
   "active"                  boolean NOT NULL DEFAULT TRUE,
   "creation_date"           timestamptz  NOT NULL DEFAULT now(),
   "created_by"              varchar(255)  NOT NULL,
   "last_modification_date"  timestamptz  NOT NULL DEFAULT now(),
   "last_modified_by"        varchar(255)  NOT NULL,
   CONSTRAINT PK_WORKSPACE_ID PRIMARY KEY ("id")
);
CREATE TABLE IF NOT EXISTS "long_operation_status"  (
   "key"            varchar(255) NOT NULL,
   "description"    varchar(1000) NOT NULL,
   CONSTRAINT PK_LONG_OPERATION_STATUS_KEY PRIMARY KEY ("key")
);

-- lookup data
INSERT into long_operation_status ("key","description")
VALUES('IN_PROGRESS','In Progress')
ON CONFLICT DO NOTHING;

INSERT into long_operation_status ("key","description")
VALUES('FAILED','Failed')
ON CONFLICT DO NOTHING;

INSERT into long_operation_status ("key","description")
VALUES('SUCCEED','Succeed')
ON CONFLICT DO NOTHING;


CREATE TABLE IF NOT EXISTS "long_operation_type"  (
   "key"            varchar(255) NOT NULL,
   "description"    varchar(1000) NOT NULL,
   CONSTRAINT PK_LONG_OPERATION_TYPE_KEY PRIMARY KEY ("key")
);

-- lookup data
INSERT into long_operation_type ("key","description")
VALUES('ROLE','operation related to roles')
ON CONFLICT DO NOTHING;

INSERT into long_operation_type ("key","description")
VALUES('MODULE','operation related to modules')
ON CONFLICT DO NOTHING;


CREATE TABLE IF NOT EXISTS "long_operation" (
   "id"                      uuid  NOT NULL,
   "type"                    varchar(250) NOT NULL,
   "type_details"            varchar(250) NOT NULL,
   "status"                  varchar(250) NOT NULL,
   "entity_id"               uuid  NOT NULL,
   "creation_date"           timestamptz  NOT NULL DEFAULT now(),
   "created_by"              varchar(255)  NOT NULL,
   "last_modification_date"  timestamptz  NOT NULL DEFAULT now(),
   "last_modified_by"        varchar(255)  NOT NULL,
   CONSTRAINT PK_LONG_OPERATION_ID PRIMARY KEY ("id"),
   CONSTRAINT FK_LONG_OPERATION_TYPE FOREIGN KEY ("type")
      REFERENCES long_operation_type ("key") MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT FK_LONG_OPERATION_STATUS FOREIGN KEY ("status")
      REFERENCES long_operation_status ("key") MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);