CREATE TABLE PET (
  ID BIGSERIAL PRIMARY KEY,
  NAME VARCHAR NOT NULL,
  STATUS VARCHAR NOT NULL,
  TAGS VARCHAR NOT NULL
);

CREATE TABLE ORDERS (
  ID BIGSERIAL PRIMARY KEY,
  PET_ID INT8 NOT NULL REFERENCES PET (ID) ON DELETE CASCADE,
  USER_ID INT8 NOT NULL REFERENCES USERS (ID) ON DELETE CASCADE,
  SHIP_DATE TIMESTAMP NULL,
  STATUS VARCHAR NOT NULL
);

CREATE TABLE USERS (
  ID BIGSERIAL PRIMARY KEY,
  USER_NAME VARCHAR NOT NULL UNIQUE,
  FIRST_NAME VARCHAR NOT NULL,
  LAST_NAME VARCHAR NOT NULL,
  EMAIL VARCHAR NOT NULL,
  PASSWORD VARCHAR NOT NULL
);
