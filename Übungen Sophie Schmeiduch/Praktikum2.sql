--1)
CREATE SCHEMA Ausleihe;
SET search_path TO Ausleihe;

CREATE TABLE Leser (
    LID INTEGER NOT NULL PRIMARY KEY,
    Lesername VARCHAR(40) NOT NULL,
    GebJahr INTEGER
);
INSERT INTO Leser (LID, Lesername, GebJahr) VALUES (1, 'Schmidt', 1950);
INSERT INTO Leser (LID, Lesername, GebJahr) VALUES (2, 'Mueller', 1954);


CREATE TABLE BuchEx (
    ISBN CHAR(10) NOT NULL,
    EXPLNR INTEGER NOT NULL,
    Titel VARCHAR(90) NOT NULL,
    Autorname VARCHAR(60),
    PRIMARY KEY (ISBN, EXPLNR)
);
INSERT INTO BuchEx (ISBN, ExpLNr, Titel, Autorname) VALUES ('A', 1, 'Java', 'Krause');
INSERT INTO BuchEx (ISBN, ExpLNr, Titel, Autorname) VALUES ('A', 2, 'Java', 'Krause');
INSERT INTO BuchEx (ISBN, ExpLNr, Titel, Autorname) VALUES ('B', 1, 'XML', 'Mueller');


CREATE TABLE Ausleihe (
    ISBN CHAR(10) NOT NULL,
    EXPLNR INTEGER NOT NULL,
    Datum DATE NOT NULL,
    LeserID INTEGER NOT NULL REFERENCES Leser(LID) ON DELETE NO ACTION,
    PRIMARY KEY (ISBN, EXPLNR),
    FOREIGN KEY (ISBN, EXPLNR) REFERENCES BuchEx(ISBN, EXPLNR) ON DELETE CASCADE
);
INSERT INTO Ausleihe (ISBN, ExpLNr, Datum, LeserID) VALUES ('A', 1, '2022-10-13', 1);
INSERT INTO Ausleihe (ISBN, ExpLNr, Datum, LeserID) VALUES ('B', 1, '2022-10-16', 1);


--2) 
--a)
DELETE FROM Leser WHERE LID = 1;
Select * From Leser;
-- Error: Auf Schlüssel (lid)=(1) wird noch aus Tabelle »ausleihe« verwiesen
-- löschen mit der aktuellen Löschregel nicht möglich, da Fremdschlüsselbeziehung mit Ausleihe exsistiert.
-- 2 Tupel noch vorhanden

-- b) 
DELETE FROM Leser WHERE Lesername = 'Mueller';
Select * From Leser;
--funktioniert, da LID = 2 keine beziehung zu Ausleihe hat
--1 Tupel noch vorhanden

-- c)
DELETE FROM BuchEx WHERE ISBN = 'A' AND EXPLNR = 1;
Select * From BuchEx;
--funktioniert, da on Delete Cascade
--2 Tupel noch vorhanden

-- d)
DELETE FROM Ausleihe WHERE ISBN = 'B' AND EXPLNR = 1;
Select * From Ausleihe
-- funktioniert
-- jetzt sind keine Tupel mehr vorhanden


--3) 

-- a)
ALTER TABLE Leser ADD CONSTRAINT unique_lesername UNIQUE (Lesername);

-- b)
ALTER TABLE Leser ADD CONSTRAINT check_gebjahr CHECK (GebJahr > 1900 AND GebJahr < 2010);

-- c) 
CREATE TABLE AusleihHistorie (
    ISBN CHAR(10),
    EXPLNR INTEGER,
    Datum DATE,
    LeserID INTEGER
);

CREATE FUNCTION log_rueckgabe() 
RETURNS TRIGGER 
LANGUAGE PLPGSQL
AS
$$
BEGIN
    INSERT INTO AusleihHistorie (ISBN, EXPLNR, Datum, LeserID)
    VALUES (OLD.ISBN, OLD.EXPLNR, OLD.Datum, OLD.LeserID);
    RETURN OLD;
END;
$$;

CREATE TRIGGER rueckgabe_trigger
AFTER DELETE ON Ausleihe
FOR EACH ROW
EXECUTE FUNCTION log_rueckgabe();

-- d)
CREATE FUNCTION autor_check() 
RETURNS TRIGGER 
LANGUAGE PLPGSQL
AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM BuchEx WHERE BuchEx.Autorname = (SELECT Lesername FROM Leser WHERE Leser.LID = NEW.LeserID) AND BuchEx.ISBN = NEW.ISBN) THEN
        RAISE EXCEPTION 'Autoren dürfen keine eigenen Bücher ausleihen';
    END IF;
    RETURN NEW;
END;
$$ ;

CREATE TRIGGER autor_ausleihe_check
BEFORE INSERT ON Ausleihe
FOR EACH ROW
EXECUTE FUNCTION autor_check();

-- e)
CREATE FUNCTION max_ausleihe_check() 
RETURNS TRIGGER 
LANGUAGE PLPGSQL
AS $$
BEGIN
    IF (SELECT COUNT(*) FROM Ausleihe WHERE LeserID = NEW.LeserID) >= 3 THEN
        RAISE EXCEPTION 'Ein Leser darf maximal 3 Bücher gleichzeitig ausleihen';
    END IF;
    RETURN NEW;
END;
$$;

CREATE TRIGGER max_ausleihe
BEFORE INSERT ON Ausleihe
FOR EACH ROW
EXECUTE FUNCTION max_ausleihe_check();


--4)
--a) 
CREATE VIEW Ausgeliehen AS
SELECT A.ISBN, A.EXPLNR, B.Titel
FROM Ausleihe A
JOIN BuchEx B ON A.ISBN = B.ISBN AND A.EXPLNR = B.EXPLNR;

--b) 
CREATE VIEW Unausgeliehen AS
SELECT B.ISBN, B.Autorname
FROM BuchEx B
LEFT JOIN Ausleihe A ON B.ISBN = A.ISBN AND B.EXPLNR = A.EXPLNR
WHERE A.ISBN IS NULL;

--c) 
CREATE VIEW AusleihAnzahl AS
SELECT B.ISBN, COUNT(A.ISBN) AS Anzahl
FROM BuchEx B
LEFT JOIN Ausleihe A ON B.ISBN = A.ISBN AND B.EXPLNR = A.EXPLNR
GROUP BY B.ISBN;

--d)
SELECT ISBN
FROM AusleihAnzahl
ORDER BY Anzahl DESC
LIMIT 1;

-- e)
SELECT ISBN
FROM Unausgeliehen;

--5)
SET search_path TO filmDB;
--a)
CREATE VIEW aktuelle_bewertung AS
SELECT b.film_id, f.titel, b.punkte, b.datum
FROM bewertung b
JOIN film f ON b.film_id = f.film_id
WHERE b.datum >= '2023-01-01';

--b)
CREATE VIEW gute_filme AS
SELECT DISTINCT b.film_id, f.titel
FROM bewertung b
JOIN film f ON b.film_id = f.film_id
WHERE b.punkte > 7;

--c)
CREATE VIEW ranking AS
SELECT f.film_id, f.titel, AVG(b.punkte) AS durchschnitt
FROM bewertung b
JOIN film f ON b.film_id = f.film_id
GROUP BY f.film_id
ORDER BY durchschnitt ASC;

--d)
CREATE FUNCTION update_film_titel() 
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE film SET titel = NEW.titel WHERE film_id = OLD.film_id;
    RETURN NEW;
END;
$$;

CREATE TRIGGER update_titel_trigger
INSTEAD OF UPDATE ON aktuelle_bewertung
FOR EACH ROW
EXECUTE FUNCTION update_film_titel();

--e)
CREATE FUNCTION update_film_id() 
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE bewertung SET film_id = NEW.film_id WHERE film_id = OLD.film_id AND datum = OLD.datum;
    RETURN NEW;
END;
$$ ;

CREATE TRIGGER update_film_id_trigger
INSTEAD OF UPDATE ON aktuelle_bewertung
FOR EACH ROW
EXECUTE FUNCTION update_film_id();


--f)
CREATE FUNCTION delete_gute_filme() 
RETURNS TRIGGER 
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM bewertung WHERE film_id = OLD.film_id;
    RETURN OLD;
END;
$$;

CREATE TRIGGER delete_gute_filme_trigger
INSTEAD OF DELETE ON gute_filme
FOR EACH ROW
EXECUTE FUNCTION delete_gute_filme();
