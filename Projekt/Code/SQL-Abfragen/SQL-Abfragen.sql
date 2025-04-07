Set search_path to "socialnetwork";

-- Aufgabe 5: Sichtenerstellung
CREATE VIEW "pkp_symmetric" AS
SELECT "person1id" AS "person_a", "person2id" AS "person_b" FROM "knows"
UNION
SELECT "person2id" AS "person_a", "person1id" AS "person_b" FROM "knows";

-- Aufgabe 6: SQL-Anfragen

-- a) In wie vielen verschiedenen europäischen Städten gibt es eine Universität?
SELECT COUNT(DISTINCT "city"."placeid") AS num_cities
FROM "university"
JOIN "city" ON "university"."city" = "city"."placeid"
JOIN "country" ON "city"."country" = "country"."placeid"
WHERE "country"."continent" = (SELECT "placeid" FROM "place" WHERE "name" = 'Europe');

-- b) Wie viele Forenbeiträge (Posts) hat die jüngste Person verfasst?
SELECT CONCAT("firstname", ' ', "lastname") AS name, COUNT("post"."messageid") AS num_posts
FROM "person"
INNER JOIN "message" ON "message"."creator" = "person"."personid"
LEFT JOIN "post" ON "message"."messageid" = "post"."messageid"
WHERE "birthday" = (SELECT MAX("birthday") FROM "person")
GROUP BY "person"."personid";

-- c) Wie viele Kommentare zu Posts gibt es aus jedem Land?
SELECT COUNT(c."messageid") as comment_cout,pl."name" as comment_country
FROM "country" cnt
LEFT JOIN "place" pl ON cnt."placeid" = pl."placeid"
LEFT JOIN "message" m ON pl."placeid" = m."country"
LEFT JOIN "comment" c ON m."messageid" = c."messageid"
LEFT JOIN "post" p ON p."messageid" = c."replyon"
GROUP BY pl."name"
ORDER BY comment_cout DESC;

-- d) Aus welchen Städten stammen die meisten Nutzer?
SELECT pl."name" AS "city_name", COUNT(p."personid") AS "user_count"
FROM "city" c
JOIN "place" pl ON pl."placeid" = c."placeid"
JOIN "person" p ON c."placeid" = p."city"
GROUP BY pl."name"
ORDER BY "user_count" DESC
LIMIT 5;

-- e) Mit wem ist 'Hans Johansson' befreundet?
SELECT p_b."firstname",p_b."lastname"
FROM "socialnetwork".pkp_symmetric pkp
JOIN "person" p_a on p_a."personid" = pkp."person_a"
JOIN"person" p_b on p_b."personid" = pkp."person_b"
where p_a."firstname" = 'Hans' AND p_a."lastname" = 'Johansson';

-- f) Wer sind die "echten" Freundesfreunde von 'Hans Johansson'?
SELECT DISTINCT p_c."firstname", p_c."lastname"
FROM "socialnetwork".pkp_symmetric pkp1
JOIN "person" p_a ON p_a."personid" = pkp1."person_a"
JOIN "socialnetwork".pkp_symmetric pkp2 ON pkp1."person_b" = pkp2."person_a"
JOIN "person" p_c ON p_c."personid" = pkp2."person_b"
WHERE p_a."firstname" = 'Hans' 
  AND p_a."lastname" = 'Johansson'
  AND p_c."personid" != p_a."personid" 
  AND p_c."personid" NOT IN (
      SELECT pkp1."person_b"
      FROM "socialnetwork".pkp_symmetric pkp1
      WHERE pkp1."person_a" = p_a."personid"
  );
  
-- g) Welche Nutzer sind Mitglied in allen Foren, in denen auch 'Mehmet Koksal' Mitglied ist?
WITH MehmetForums AS (
    SELECT hm."forumid"
    FROM "hasmember" hm
    JOIN "person" p ON hm."personid" = p."personid"
    WHERE p."firstname" = 'Mehmet' AND p."lastname" = 'Koksal'
),
AllMembers AS (
    SELECT hm."personid"
    FROM "hasmember" hm
    JOIN MehmetForums mf ON hm."forumid" = mf."forumid"
    GROUP BY hm."personid"
    HAVING COUNT(DISTINCT hm."forumid") = (SELECT COUNT(*) FROM MehmetForums)
)
SELECT CONCAT(p."firstname", ' ', p."lastname") AS name
FROM "person" p
JOIN AllMembers am ON p."personid" = am."personid";

-- h) Prozentuale Verteilung der Nutzer bezüglich ihrer Herkunft aus verschiedenen Kontinenten
SELECT "place"."name" AS "continent_name", 
       ROUND(COUNT("person"."personid") * 100.0 / (SELECT COUNT(*) FROM "person"), 2) AS "percentage"
FROM "person"
JOIN "city" ON "person"."city" = "city"."placeid"
JOIN "country" ON "city"."country" = "country"."placeid"
JOIN "continent" ON "country"."continent" = "continent"."placeid"
JOIN "place" ON "continent"."placeid" = "place"."placeid"
GROUP BY "place"."name";

-- i) Foren mit mehr Posts als der durchschnittlichen Anzahl
SELECT "forum"."title"
FROM "forum"
JOIN "post" ON "forum"."forumid" = "post"."forumid"
GROUP BY "forum"."title"
HAVING COUNT("post"."messageid") > (
    SELECT AVG("post_count")
    FROM (
        SELECT COUNT("messageid") AS "post_count"
        FROM "post"
        GROUP BY "forumid"
    ) AS "subquery"
)
ORDER BY "forum"."title";

-- j) Wer ist mit der Person befreundet, die die meisten Likes auf einen Post erhalten hat?
WITH MostLikedPost AS (
    SELECT "creator"
    FROM "post" p
	Inner JOIN "message" ON p."messageid" = "message"."messageid"
    JOIN "likes" ON "message"."messageid" = "likes"."messageid"
    GROUP BY "message"."creator"
    ORDER BY COUNT("likes"."personid") DESC
    LIMIT 1
)
SELECT CONCAT("person"."firstname", ' ', "person"."lastname") AS name
FROM pkp_symmetric
JOIN "person" ON pkp_symmetric."person_b" = "person"."personid"
WHERE "pkp_symmetric"."person_a" = (SELECT * FROM MostLikedPost)
ORDER BY "person"."lastname";


-- k) Personen, die mit 'Jun Hu' (id 94) direkt oder indirekt verbunden sind
--rekursive CTE, um die Distanz zu berechnen.
WITH RECURSIVE FriendshipPath AS (
    SELECT "person_a", "person_b", 1 AS distance
    FROM "pkp_symmetric"
    WHERE "person_a" = 94
    UNION ALL
    SELECT fp."person_b", ps."person_b", fp.distance + 1
    FROM "pkp_symmetric" ps
    JOIN FriendshipPath fp ON ps."person_a" = fp."person_b"
    WHERE fp.distance < 5
)
SELECT DISTINCT 
    "person"."firstname", 
    "person"."lastname", 
    MIN(fp.distance) AS min_distance
FROM FriendshipPath fp
JOIN "person" ON fp."person_b" = "person"."personid"
GROUP BY "person"."personid"
ORDER BY min_distance;




-- l) Erweiterung von k) mit minimalen Pfaden
WITH RECURSIVE Paths AS (
    SELECT 
        "person_a", 
        "person_b", 
        ARRAY[(SELECT CONCAT("firstname", ' ', "lastname") FROM "person" WHERE "personid" = "person_a"), 
              (SELECT CONCAT("firstname", ' ', "lastname") FROM "person" WHERE "personid" = "person_b")] AS path, 
        1 AS distance
    FROM "pkp_symmetric"
    WHERE "person_a" = 94
    UNION ALL
    SELECT 
        p."person_b", 
        ps."person_b", 
        path || (SELECT CONCAT("firstname", ' ', "lastname") FROM "person" WHERE "personid" = ps."person_b"), 
        distance + 1
    FROM "pkp_symmetric" ps
    JOIN Paths p ON ps."person_a" = p."person_b"
    WHERE NOT (SELECT CONCAT("firstname", ' ', "lastname") FROM "person" WHERE "personid" = ps."person_b") = ANY(path) 
      AND distance < 5
),
MinDistancePaths AS (
    SELECT 
        "person_b", 
        MIN(distance) AS min_distance
    FROM Paths
    GROUP BY "person_b"
),
FilteredPaths AS (
    SELECT DISTINCT
        Paths."person_b", 
        Paths.path, 
        Paths.distance
    FROM Paths
    JOIN MinDistancePaths ON Paths."person_b" = MinDistancePaths."person_b" AND Paths.distance = MinDistancePaths.min_distance
)
SELECT 
    "person"."firstname", 
    "person"."lastname", 
    FilteredPaths.path, 
    FilteredPaths.distance AS min_distance
FROM FilteredPaths
JOIN "person" ON FilteredPaths."person_b" = "person"."personid"
ORDER BY min_distance;


-- m) Mindestens 2 Anfragen, die neue Tabellen und Daten verwenden:
-- 1.Frage: Welche Piraten haben mindestens ein Schiff welches die Farbe weiß hat
SELECT DISTINCT "pirate"."gamertag"
FROM "pirate"
JOIN "fleet" ON "pirate"."gamertag" = "fleet"."captain"
JOIN "ship" ON "fleet"."shipid" = "ship"."shipid"
WHERE "ship"."skin"::JSONB @> '{"color": "white"}';

-- 2.Frage: Welche Personen sprechen mehr als 2 Sprachen? (Verwendung von Arrays)
SELECT CONCAT("firstname", ' ', "lastname") AS name, "language"
FROM "person"
WHERE array_length("language", 1) > 2;

