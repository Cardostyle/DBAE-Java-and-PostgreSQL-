--Löschen wenn vorhanden
DROP SCHEMA IF EXISTS "socialnetwork" CASCADE;
COMMIT;
-- Schema erstellen
CREATE SCHEMA "socialnetwork";
Commit;
-- Schema setzen
SET search_path TO "socialnetwork";

-- Tabellen erstellen
CREATE TABLE "person" (
    "personid" BIGINT PRIMARY KEY,
    "firstname" VARCHAR(255) NOT NULL,
    "lastname" VARCHAR(255) NOT NULL,
    "gender" VARCHAR(10),
    "birthday" DATE NOT NULL CHECK ("birthday" <= CURRENT_DATE),
    "creationdate" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "locationip" VARCHAR(255) NOT NULL,
    "browserused" VARCHAR(255),
    "city" INT,
    "language" VARCHAR[] 
);

CREATE TABLE "hasinterest" (
    "personid" BIGINT,
    "tagid" INT,
    PRIMARY KEY ("personid", "tagid")
);


CREATE TABLE "email" (
    "personid" BIGINT,
    "email" VARCHAR(255) UNIQUE CHECK ("email" ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    PRIMARY KEY ("personid", "email")
);

CREATE TABLE "knows" (
    "person1id" BIGINT,
    "person2id" BIGINT,
    "creationdate" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY ("person1id", "person2id")
);

CREATE TABLE "forum" (
    "forumid" BIGINT PRIMARY KEY,
    "title" VARCHAR(255) NOT NULL,
    "creationdate" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "moderator" BIGINT
);

CREATE TABLE "forumhastag" (
    "forumid" BIGINT,
    "tagid" INT,
    PRIMARY KEY ("forumid", "tagid")
);

CREATE TABLE "messagehastag" (
    "messageid" BIGINT,
    "tagid" INT,
    PRIMARY KEY ("messageid", "tagid")
);

CREATE TABLE "hasmember" (
    "forumid" BIGINT,
    "personid" BIGINT,
    "joindate" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY ("forumid", "personid")
);

CREATE TABLE "tag" (
    "tagid" SERIAL PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL,
    "url" VARCHAR(255)
);

CREATE TABLE "hastype" (
    "tagid" INT,
    "tagclassid" INT,
    PRIMARY KEY ("tagclassid", "tagid")
);

CREATE TABLE "tagclass" (
    "tagclassid" SERIAL PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL,
    "url" VARCHAR(255)
);

CREATE TABLE "tagsubclasses" (
    "parentclass" INT,
    "childclass" INT,
    PRIMARY KEY ("parentclass", "childclass")
);

CREATE TABLE "message" (
    "messageid" BIGINT PRIMARY KEY,
    "creationdate" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "locationip" VARCHAR(255),
	"browserused" VARCHAR(255),
    "content" TEXT,
    "length" INT,
    "creator" BIGINT,
    "country" INT
);

CREATE TABLE "comment" (
    "messageid" BIGINT PRIMARY KEY,
    "replyon" BIGINT NOT NULL
);

CREATE TABLE "post" (
    "messageid" BIGINT PRIMARY KEY,
    "imagefile" VARCHAR(255),
	"language" VARCHAR(255),
    "forumid" BIGINT
);

CREATE TABLE "likes" (
    "personid" BIGINT,
    "messageid" BIGINT,
    "creationdate" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY ("personid", "messageid")
);

CREATE TABLE "organisation" (
    "organisationid" SERIAL PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL
);

CREATE TABLE "university" (
    "organisationid" INT PRIMARY KEY,
    "city" INT
);

CREATE TABLE "company" (
    "organisationid" INT PRIMARY KEY,
    "country" INT
);

CREATE TABLE "place" (
    "placeid" SERIAL PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL
);

CREATE TABLE "country" (
    "placeid" INT PRIMARY KEY,
    "continent" INT
);

CREATE TABLE "city" (
    "placeid" INT PRIMARY KEY,
    "country" INT
);

CREATE TABLE "continent" (
    "placeid" INT PRIMARY KEY
);

CREATE TABLE "studyat" (
    "personid" BIGINT,
    "universityid" INT,
    "classyear" INT,
    PRIMARY KEY ("personid", "universityid")
);

CREATE TABLE "workat" (
    "personid" BIGINT,
    "companyid" INT,
    "worksince" INT,
    PRIMARY KEY ("personid", "companyid")
);

CREATE TABLE "workhistory" (
    "workhistoryid" SERIAL PRIMARY KEY,
    "personid" BIGINT,
    "companyid" INT,
    "worksince" INT,
    "enddate" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "pirate" (
    "gamertag" VARCHAR(255) PRIMARY KEY,
    "personid" BIGINT,
    "gold" INT CHECK ("gold" >= 0),
    "doubloons" INT CHECK ("doubloons" >= 0),
    "bodytype" VARCHAR(255)
);

CREATE TABLE "fleet" (
    "captain" VARCHAR(255),
    "shipid" INT,
    PRIMARY KEY ("captain", "shipid")
);

CREATE TABLE "ship" (
    "shipid" SERIAL PRIMARY KEY,
    "type" VARCHAR(255) NOT NULL,
    "name" VARCHAR(255),
    "skin" JSON
);

CREATE TABLE "factionrelation" (
    "gamertag" VARCHAR(255),
    "faction" VARCHAR(255),
    "level" FLOAT,
    PRIMARY KEY ("gamertag", "faction")
);

CREATE TABLE "faction" (
    "name" VARCHAR(255) PRIMARY KEY,
    "description" TEXT,
    "maxlvl" INT NOT NULL
);

