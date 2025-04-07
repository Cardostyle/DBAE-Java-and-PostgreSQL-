SET search_path TO socialnetwork;

CREATE OR REPLACE PROCEDURE getShortestFriendshipPath(
    IN p_startId BIGINT,
    IN p_endId BIGINT,
    OUT result_cursor REFCURSOR
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Cursor öffnen, um die Ergebnistabelle als ResultSet zurückzugeben
    OPEN result_cursor FOR
    WITH RECURSIVE Paths AS (
        SELECT 
            ps.person_a AS current_person,
            ps.person_b AS next_person,
            ARRAY[ps.person_a, ps.person_b] AS path,
            1 AS depth
        FROM socialnetwork.pkp_symmetric ps
        WHERE ps.person_a = p_startId

        UNION ALL

        SELECT 
            p.next_person AS current_person,
            ps.person_b AS next_person,
            p.path || ps.person_b AS path,
            p.depth + 1 AS depth
        FROM socialnetwork.pkp_symmetric ps
        JOIN Paths p ON ps.person_a = p.next_person
        WHERE NOT ps.person_b = ANY(p.path) 
        AND p.depth < 5
    )
    SELECT CONCAT(p.firstname, ' ', p.lastname) AS fullname
    FROM Paths
    JOIN socialnetwork.person p ON p.personid = ANY(path)  -- IDs in Namen umwandeln
    WHERE next_person = p_endId
    ORDER BY array_length(path, 1)
    LIMIT 1;
END;
$$;

-- Aufgabe 5: Sichtenerstellung
CREATE VIEW "pkp_symmetric" AS
SELECT "person1id" AS "person_a", "person2id" AS "person_b" FROM "knows"
UNION
SELECT "person2id" AS "person_a", "person1id" AS "person_b" FROM "knows";
