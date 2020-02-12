CREATE TABLE  IF NOT EXISTS  "role" (
   "id"                      varchar(255) NOT NULL,
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