set search_path to "socialnetwork";

-- Fremdschlüssel hinzufügen
ALTER TABLE "person" ADD CONSTRAINT fk_person_city FOREIGN KEY ("city") REFERENCES "city" ("placeid") ON UPDATE CASCADE ON DELETE SET NULL;
ALTER TABLE "hasinterest" ADD CONSTRAINT fk_interest_person FOREIGN KEY ("personid") REFERENCES "person" ("personid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "hasinterest" ADD CONSTRAINT fk_interest_tag FOREIGN KEY ("tagid") REFERENCES "tag" ("tagid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "email" ADD CONSTRAINT fk_Email_person FOREIGN KEY ("personid") REFERENCES "person" ("personid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "knows" ADD CONSTRAINT fk_knows_person1 FOREIGN KEY ("person1id") REFERENCES "person" ("personid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "knows" ADD CONSTRAINT fk_knows_person2 FOREIGN KEY ("person2id") REFERENCES "person" ("personid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "forum" ADD CONSTRAINT fk_forum_moderator FOREIGN KEY ("moderator") REFERENCES "person" ("personid") ON UPDATE CASCADE ON DELETE SET NULL;
ALTER TABLE "forumhastag" ADD CONSTRAINT fk_hastag_forum FOREIGN KEY ("forumid") REFERENCES "forum" ("forumid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "forumhastag" ADD CONSTRAINT fk_hastag_tag FOREIGN KEY ("tagid") REFERENCES "tag" ("tagid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "hasmember" ADD CONSTRAINT fk_hasmember_forum FOREIGN KEY ("forumid") REFERENCES "forum" ("forumid") ON UPDATE CASCADE ON DELETE SET NULL;
ALTER TABLE "hasmember" ADD CONSTRAINT fk_hasmember_person FOREIGN KEY ("personid") REFERENCES "person" ("personid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "hastype" ADD CONSTRAINT fk_hastype_tag FOREIGN KEY ("tagid") REFERENCES "tag" ("tagid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "hastype" ADD CONSTRAINT fk_hastype_tagclass FOREIGN KEY ("tagclassid") REFERENCES "tagclass" ("tagclassid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "tagsubclasses" ADD CONSTRAINT fk_tagsubclasses_parentclass FOREIGN KEY ("parentclass") REFERENCES "tagclass" ("tagclassid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "tagsubclasses" ADD CONSTRAINT fk_tagsubclasses_childclass FOREIGN KEY ("childclass") REFERENCES "tagclass" ("tagclassid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "message" ADD CONSTRAINT fk_message_creator FOREIGN KEY ("creator") REFERENCES "person" ("personid") ON UPDATE CASCADE ON DELETE SET NULL;
ALTER TABLE "message" ADD CONSTRAINT fk_message_country FOREIGN KEY ("country") REFERENCES "country" ("placeid") ON UPDATE CASCADE ON DELETE SET NULL;
ALTER TABLE "messagehastag" ADD CONSTRAINT fk_hastag_message FOREIGN KEY ("messageid") REFERENCES "message" ("messageid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "messagehastag" ADD CONSTRAINT fk_hastag_tag FOREIGN KEY ("tagid") REFERENCES "tag" ("tagid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "comment" ADD CONSTRAINT fk_comment_replyon FOREIGN KEY ("replyon") REFERENCES "message" ("messageid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "comment" ADD CONSTRAINT fk_comment_message FOREIGN KEY ("messageid") REFERENCES "message" ("messageid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "post" ADD CONSTRAINT fk_post_forum FOREIGN KEY ("forumid") REFERENCES "forum" ("forumid") ON UPDATE CASCADE ON DELETE SET NULL;
ALTER TABLE "post" ADD CONSTRAINT fk_post_message FOREIGN KEY ("messageid") REFERENCES "message" ("messageid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "likes" ADD CONSTRAINT fk_likes_person FOREIGN KEY ("personid") REFERENCES "person" ("personid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "likes" ADD CONSTRAINT fk_likes_message FOREIGN KEY ("messageid") REFERENCES "message" ("messageid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "university" ADD CONSTRAINT fk_university_organisation FOREIGN KEY ("organisationid") REFERENCES "organisation" ("organisationid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "university" ADD CONSTRAINT fk_university_city FOREIGN KEY ("city") REFERENCES "city" ("placeid") ON UPDATE CASCADE ON DELETE SET NULL;
ALTER TABLE "company" ADD CONSTRAINT fk_Company_organisation FOREIGN KEY ("organisationid") REFERENCES "organisation" ("organisationid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "company" ADD CONSTRAINT fk_Company_city FOREIGN KEY ("country") REFERENCES "country" ("placeid") ON UPDATE CASCADE ON DELETE SET NULL;
ALTER TABLE "city" ADD CONSTRAINT fk_city_place FOREIGN KEY ("placeid") REFERENCES "place" ("placeid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "city" ADD CONSTRAINT fk_city_country FOREIGN KEY ("country") REFERENCES "country" ("placeid") ON UPDATE CASCADE ON DELETE NO ACTION;
ALTER TABLE "country" ADD CONSTRAINT fk_coutry_place FOREIGN KEY ("placeid") REFERENCES "place" ("placeid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "country" ADD CONSTRAINT fk_country_continet FOREIGN KEY ("continent") REFERENCES "continent" ("placeid") ON UPDATE CASCADE ON DELETE NO ACTION;
ALTER TABLE "continent" ADD CONSTRAINT fk_continent_place FOREIGN KEY ("placeid") REFERENCES "place" ("placeid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "studyat" ADD CONSTRAINT fk_studyat_person FOREIGN KEY ("personid") REFERENCES "person" ("personid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "studyat" ADD CONSTRAINT fk_studyat_university FOREIGN KEY ("universityid") REFERENCES "university" ("organisationid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "workat" ADD CONSTRAINT fk_workat_person FOREIGN KEY ("personid") REFERENCES "person" ("personid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "workat" ADD CONSTRAINT fk_workat_company FOREIGN KEY ("companyid") REFERENCES "company" ("organisationid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "workhistory" ADD CONSTRAINT fk_workhistory_person FOREIGN KEY ("personid") REFERENCES "person" ("personid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "workhistory" ADD CONSTRAINT fk_workhistory_company FOREIGN KEY ("companyid") REFERENCES "company" ("organisationid") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "pirate" ADD CONSTRAINT fk_pirate_person FOREIGN KEY ("personid") REFERENCES "person" ("personid") ON UPDATE CASCADE ON DELETE SET NULL;
ALTER TABLE "factionrelation" ADD CONSTRAINT fk_factionrelation_gamertag FOREIGN KEY ("gamertag") REFERENCES "pirate" ("gamertag") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "factionrelation" ADD CONSTRAINT fk_factionrelation_faction FOREIGN KEY ("faction") REFERENCES "faction" ("name") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "fleet" ADD CONSTRAINT fk_fleet_captain FOREIGN KEY ("captain") REFERENCES "pirate" ("gamertag") ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE "fleet" ADD CONSTRAINT fk_fleet_ship FOREIGN KEY ("shipid") REFERENCES "ship" ("shipid") ON UPDATE CASCADE ON DELETE CASCADE;

-- Trigger und Funktionen
-- Trigger für WorkHistory
CREATE OR REPLACE FUNCTION log_work_history()
RETURNS TRIGGER 
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO "workhistory" ("personid", "companyid", "worksince", "enddate")
    VALUES (OLD."personid", OLD."companyid", OLD."worksince", CURRENT_TIMESTAMP);
    RETURN OLD;
END;
$$;

CREATE TRIGGER after_delete_workAt
AFTER DELETE ON "workat"
FOR EACH ROW
EXECUTE FUNCTION log_work_history();

-- Create the trigger function to enforce the level constraint
CREATE OR REPLACE FUNCTION enforce_level_constraint()
RETURNS TRIGGER 
LANGUAGE plpgsql
AS $$
BEGIN
    -- Check if the level is within the allowed range for the faction
    IF NEW."level" < 0 OR NEW."level" > (SELECT "maxlvl" FROM "faction" WHERE "name" = NEW."faction") THEN
        RAISE EXCEPTION 'Level % is out of bounds for faction %', NEW."level", NEW."faction";
    END IF;
    RETURN NEW;
END;
$$;

-- Create the trigger to call the function before inserting or updating
CREATE TRIGGER check_level_before_insert_update
BEFORE UPDATE ON "factionrelation"
FOR EACH ROW
EXECUTE FUNCTION enforce_level_constraint();