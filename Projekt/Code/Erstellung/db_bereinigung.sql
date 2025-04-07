set search_path to "socialnetwork";
---------------------------------------------------------
--Comment in Message einfuegen
INSERT INTO "socialnetwork"."message"(
	"messageid", "creationdate", "locationip", "browserused", content, length, creator, country)
	SELECT id, TO_TIMESTAMP("creationdate",'YYYY-MM-DD"t"HH24:MI:SS.MS+0000'), "locationip", "browserused", content, length, creator, place
	FROM "socialnetwork".comment_0_0_cleaned;

--Post in Message einfuegen
INSERT INTO "socialnetwork"."message"(
	"messageid", "creationdate", "locationip", "browserused", content, length, creator, country)
	SELECT id, TO_TIMESTAMP("creationdate",'YYYY-MM-DD"t"HH24:MI:SS.MS+0000'), "locationip", "browserused", content, length, creator, place
	FROM "socialnetwork".post_0_0;
----------------------------------------------------------
--MessageHasTag mit postHasTag befuellen
INSERT INTO "socialnetwork"."messagehastag"(
	"messageid", "tagid")
	SELECT "post.id", "tag.id"
	FROM "socialnetwork"."post_hastag_tag_0_0";

--MessageHasTag mit commentHasTag befuellen
INSERT INTO "socialnetwork"."messagehastag"(
	"messageid", "tagid")
	SELECT "comment.id", "tag.id"
	FROM "socialnetwork"."comment_hastag_tag_0_0";
----------------------------------------------------------	
--Comment befuellen
INSERT INTO "socialnetwork"."comment"(
	"messageid", "replyon")
Select id, COALESCE("replyofpost", 0) from "socialnetwork".comment_0_0_cleaned;

UPDATE "socialnetwork"."comment" AS c
SET "replyon" = "replyofcomment"
FROM "socialnetwork".comment_0_0_cleaned AS co
WHERE c."replyon" = 0
  AND c."messageid" = co."id";
---------------------------------------------------------
--Post befuellen 
INSERT INTO "socialnetwork"."post"(
	"messageid", "imagefile", language, "forumid")
	SELECT id, "imagefile", language, "forum.id"
	FROM "socialnetwork".post_0_0;
---------------------------------------------------------
--PersonLikesPost zu Likes hinzufuegen 
INSERT INTO "socialnetwork"."likes"(
	"personid", "messageid", "creationdate")
	SELECT "person.id", "post.id", TO_TIMESTAMP("creationdate",'YYYY-MM-DD"t"HH24:MI:SS.MS+0000')
	FROM "socialnetwork".person_likes_post_0_0;
--------------------------------------------------------
--Person mit Sprache befüllen
UPDATE "socialnetwork"."person"
SET "language" = subquery.languages
FROM (
    SELECT 
        "person.id", 
        array_agg(language) AS languages
    FROM 
        "socialnetwork".person_speaks_language_0_0 s
    GROUP BY 
        "person.id"
) AS subquery
WHERE "person"."personid" = subquery."person.id";

--------------------------------------------------------
--URL weggelassen
--Organisation mit organisation_0_0 befuellen 
INSERT INTO "socialnetwork"."organisation"(
	"organisationid", name)
	SELECT id, name
	FROM "socialnetwork".organisation_0_0;
--------------------------------------------------------
--University mit organisation_0_0 befuellen	
INSERT INTO "socialnetwork"."university"(
	"organisationid", city)
	SELECT id, place
	FROM "socialnetwork".organisation_0_0
	WHERE type = 'university';

--Company mit organisation_0_0 befuellen	
INSERT INTO "socialnetwork"."company"(
	"organisationid", country)
	SELECT id, place
	FROM "socialnetwork".organisation_0_0
	WHERE type = 'company';
--------------------------------------------------------
--URL weggelassen
--Einfuegen von place_0_0 in place 
INSERT INTO "socialnetwork"."place"(
	"placeid", name)
	SELECT id, name
	FROM "socialnetwork".place_0_0;
--------------------------------------------------------
--Einfuegen von place_0_0 in city 
INSERT INTO "socialnetwork"."city"(
	"placeid", country)
	SELECT id, "ispartof"
	FROM "socialnetwork".place_0_0
	WHERE type = 'city';
	
--Einfuegen von place_0_0 in country 
INSERT INTO "socialnetwork"."country"(
	"placeid", continent)
	SELECT id, "ispartof"
	FROM "socialnetwork".place_0_0
	WHERE type = 'country';

--Einfuegen von place_0_0 in continent 
INSERT INTO "socialnetwork"."continent"(
	"placeid")
	SELECT id
	FROM "socialnetwork".place_0_0
	WHERE type = 'continent';
--------------------------------------------------------
--Loeschskripte
Drop TABLE IF EXISTS 
"socialnetwork".comment_0_0_cleaned,
"socialnetwork"."comment_hastag_tag_0_0",
"socialnetwork".organisation_0_0,
"socialnetwork".person_likes_post_0_0,
"socialnetwork".place_0_0,
"socialnetwork".post_0_0,
"socialnetwork"."post_hastag_tag_0_0",
"socialnetwork".person_speaks_language_0_0
;
