--Aufgabe 1

CREATE SCHEMA Ausleihe;
SET search_path TO Ausleihe;

CREATE TABLE Leser (
    LID INTEGER NOT NULL PRIMARY KEY,
    Lesername VARCHAR(40) NOT NULL,
    GebJahr INTEGER
);

CREATE TABLE BuchEx (
    ISBN CHAR(10) NOT NULL,
    EXPLNR INTEGER NOT NULL,
    Titel VARCHAR(90) NOT NULL,
    Autorname VARCHAR(60),
    PRIMARY KEY (ISBN, EXPLNR)
);

CREATE TABLE Ausleihe (
    ISBN CHAR(10) NOT NULL,
    EXPLNR INTEGER NOT NULL,
    Datum DATE NOT NULL,
    LeserID INTEGER NOT NULL REFERENCES Leser(LID) ON DELETE NO ACTION,
    PRIMARY KEY (ISBN, EXPLNR),
    FOREIGN KEY (ISBN, EXPLNR) REFERENCES BuchEx(ISBN, EXPLNR) ON DELETE CASCADE
);

-- Insert für die Tabelle Leser
INSERT INTO Leser (LID, Lesername, GebJahr) VALUES (1, 'Schmidt', 1950);
INSERT INTO Leser (LID, Lesername, GebJahr) VALUES (2, 'Mueller', 1954);

-- Insert für die Tabelle BuchEx
INSERT INTO BuchEx (ISBN, ExpLNr, Titel, Autorname) VALUES ('A', 1, 'Java', 'Krause');
INSERT INTO BuchEx (ISBN, ExpLNr, Titel, Autorname) VALUES ('A', 2, 'Java', 'Krause');
INSERT INTO BuchEx (ISBN, ExpLNr, Titel, Autorname) VALUES ('B', 1, 'XML', 'Mueller');

-- Insert für die Tabelle Ausleihe
INSERT INTO Ausleihe (ISBN, ExpLNr, Datum, LeserID) VALUES ('A', 1, '2022-10-13', 1);
INSERT INTO Ausleihe (ISBN, ExpLNr, Datum, LeserID) VALUES ('B', 1, '2022-10-16', 1);

--Aufgabe 2 

-- a) Löschen des Lesers mit LID = 1
DELETE FROM Leser WHERE LID = 1;
-- funktioniert nicht
-- Aktualisieren oder Löschen in Tabelle »leser« verletzt Fremdschlüssel-Constraint »ausleihe_leserid_fkey« von Tabelle »ausleihe«
-- Datensatz wird als Fremdschlüssel in Ausleihe benutzt

-- b) Löschen des Lesers mit Namen Mueller
DELETE FROM Leser WHERE Lesername = 'Mueller';
--funktioniert
--nur 1 Datensatz aus Leser wird gelöscht

-- c) Löschen des BuchEx-Tupels ('A', 1, 'Java', 'Krause')
DELETE FROM BuchEx WHERE ISBN = 'A' AND EXPLNR = 1;
--funktioniert
--Das Buch und die existierende Ausleihe von dem Buch wird gelöscht wegen on Delete Cascade

-- d) Löschen der Ausleihe des Buchexemplars ('B', 1)
DELETE FROM Ausleihe WHERE ISBN = 'B' AND EXPLNR = 1;
-- funktioniert
-- nur der eine Datensatz wird gelöscht


--Aufgabe 3

-- a)Lesername kann nicht mehrfach vergeben werden
ALTER TABLE Leser ADD CONSTRAINT unique_lesername UNIQUE (Lesername);

-- b)GebJahr muss zwischen 1900 und 2010 liegen
ALTER TABLE Leser ADD CONSTRAINT check_gebjahr CHECK (GebJahr > 1900 AND GebJahr < 2010);

-- c) Loggen der Rückgabe in AusleihHistorie
CREATE TABLE AusleihHistorie (
    ISBN CHAR(10),
    EXPLNR INTEGER,
    Datum DATE,
    LeserID INTEGER
);

CREATE FUNCTION log_rueckgabe() 
RETURNS TRIGGER 
LANGUAGE PLPGSQL
AS $$
	BEGIN
	    INSERT INTO Ausleihhistorie (ISBN, EXPLNR, Datum, LeserID)
	    VALUES (OLD.ISBN, OLD.EXPLNR, OLD.Datum, OLD.LeserID);
	    RETURN OLD;
	END;
$$;


CREATE TRIGGER rueckgabe_trigger
AFTER DELETE ON Ausleihe
FOR EACH ROW
EXECUTE FUNCTION log_rueckgabe();

-- d) Autoren dürfen keine eigenen Bücher ausleihen
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

-- e) Ein Leser darf maximal 3 Bücher gleichzeitig ausgeliehen haben
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

--Aufgabe 4

-- a) Ausgeliehen: ISBN, ExplNr und Titel aller ausgeliehenen Bücher
CREATE VIEW Ausgeliehen AS
SELECT A.ISBN, A.EXPLNR, B.Titel
FROM Ausleihe A
JOIN BuchEx B ON A.ISBN = B.ISBN AND A.EXPLNR = B.EXPLNR;

-- b) Unausgeliehen: ISBN, Autorname aller Bücher, von denen kein Exemplar ausgeliehen ist
CREATE VIEW Unausgeliehen AS
SELECT B.ISBN, B.Autorname
FROM BuchEx B
LEFT JOIN Ausleihe A ON B.ISBN = A.ISBN AND B.EXPLNR = A.EXPLNR
WHERE A.ISBN IS NULL;

-- c) AusleihAnzahl: ISBN aller Bücher mit der Anzahl der ausgeliehenen Exemplare
CREATE VIEW AusleihAnzahl AS
SELECT B.ISBN, COUNT(A.ISBN) AS Anzahl
FROM BuchEx B
LEFT JOIN Ausleihe A ON B.ISBN = A.ISBN AND B.EXPLNR = A.EXPLNR
GROUP BY B.ISBN;

-- d) Buch/ISBN mit den meisten ausgeliehenen Exemplaren
SELECT ISBN
FROM AusleihAnzahl
ORDER BY Anzahl DESC
LIMIT 1;

-- e) Buch/ISBN ohne ausgeliehene Exemplare
SELECT ISBN
FROM Unausgeliehen;



--Aufgabe 5
DROP SCHEMA filmDB CASCADE;

CREATE SCHEMA filmDB;
SET search_path TO filmDB;

DROP TABLE IF EXISTS bewertung CASCADE;
DROP TABLE IF EXISTS film CASCADE;
DROP TABLE IF EXISTS nutzer CASCADE;

CREATE TABLE film (
 film_id INT PRIMARY KEY,
 titel VARCHAR(255),
 jahr INT, 
 regisseur VARCHAR(255)
);
CREATE TABLE nutzer (
 nutzer_id INT PRIMARY KEY,
 name VARCHAR(255)
);
 CREATE TABLE bewertung (
 film_id INT,
 nutzer_id INT,
 punkte INT,
 datum DATE,
 CONSTRAINT pk PRIMARY KEY(film_id,nutzer_id),
 CONSTRAINT film_fk FOREIGN KEY (film_id) REFERENCES film(film_id) ON UPDATE CASCADE,
 CONSTRAINT nutzer_fk FOREIGN KEY (nutzer_id) REFERENCES nutzer(nutzer_id) ON UPDATE CASCADE
);

INSERT INTO film(film_id, titel, jahr, regisseur) VALUES  -- comment note -> current imdb score
(101,'Die Verurteilten',1994,'Frank Darabont'),--9,3
(102,'Pulp Fiction',1994,'Quentin Tarantino'),--8,9
(103,'Schindlers Liste',1993,'Steven Spielberg'),--9,0
(104,'Fight Club',1999,'David Fincher'),--8,8
(105,'Forrest Gump',1994,'Robert Zemeckis'),-- 8,8
(106,'Django Unchained',2012,'Quentin Tarantino'),--8,5
(108,'Nomadland',2020,'Chloé Zhao'),--7,3
(109,'Lost in Tranlation',2003,'Sofia Coppola'),--7,7
(110,'Wonder Woman',2001,'Patty Jenkins'),--7,3
(111,'The Favourite',2018,''), --7,5
(112,'The Hunger Games',2012,'Gary Ross'), -- 7,2 
(113,'Parasite',2019,'Bong Joon Ho'), --8,5
(114,'Dune',2021,'Denis Villeneuve'), --8,0
(115,'Spider-Man: No Way Home',2021,'Jon Watts'),--8,2
(116,'Deadpool',2016,'Tim Miller'),--8,2
(117,'Sharknado',2013,'Anthony C. Ferrante'),--3,3
(118,'Snakes on a Plane',2013,'David R. Ellis'),--3,3
(119,'The Lost World: Jurassic Park',2013,'Anthony C. Ferrante'),--6,9
(120,'Twister',1996,'Jan de Bont') --6,5
;

INSERT INTO nutzer(nutzer_id, name) VALUES
('201', 'Liam'),
('202', 'Noah'),
('203', 'Emma'),
('204', 'Olivia'),
('205', 'Ava'),
('206', 'Sophia'),
('207', 'Isabella'),
('208', 'Mia'),
('209', 'Elijah'),
('210', 'Lucas'),
('211', 'Aarav'),
('212', 'Zara'),
('213', 'Yuto'),
('214', 'Sofia'),
('215', 'Mateo'),
('216', 'Ethan'),
('217', 'Harper'),
('218', 'Charlotte'),
('219', 'Amelia'),
('220', 'Lea');

INSERT INTO bewertung(nutzer_id,film_id,punkte,datum) VALUES 
-- Die Verurteilten (ID: 101, IMDb: 9,3)
(201,101,9,TO_DATE('2022-12-01', 'YYYY-MM-DD')),
(202,101,10,TO_DATE('2023-07-11', 'YYYY-MM-DD')),
(209,101,9,NULL),
(207,101,8,TO_DATE('2022-09-10', 'YYYY-MM-DD')),

-- Schindlers Liste (ID: 103, IMDb: 9,0)
(208,103,9,TO_DATE('2022-09-10', 'YYYY-MM-DD')),
(212,103,9,TO_DATE('2023-07-12', 'YYYY-MM-DD')),

-- Fight Club (ID: 104, IMDb: 8,8)
(209,104,8,NULL),
(210,104,9,TO_DATE('2023-07-11', 'YYYY-MM-DD')),
(213,104,9,TO_DATE('2022-08-16', 'YYYY-MM-DD')),

-- Forrest Gump (ID: 105, IMDb: 8,8)
(211,105,9,TO_DATE('2022-05-06', 'YYYY-MM-DD')),
(212,105,8,TO_DATE('2023-01-15', 'YYYY-MM-DD')),
(214,105,9,TO_DATE('2023-02-08', 'YYYY-MM-DD')),

-- Django Unchained (ID: 106, IMDb: 8,5)
(213,106,8,TO_DATE('2023-07-06', 'YYYY-MM-DD')),
(214,106,9,TO_DATE('2023-07-16', 'YYYY-MM-DD')),
(215,106,8,TO_DATE('2022-07-11', 'YYYY-MM-DD')),

-- Nomadland (ID: 108, IMDb: 7,3)
(216,108,7,TO_DATE('2023-07-15', 'YYYY-MM-DD')),
(217,108,8,TO_DATE('2022-11-14', 'YYYY-MM-DD')),

-- Lost in Translation (ID: 109, IMDb: 7,7)
(218,109,7,TO_DATE('2022-12-03', 'YYYY-MM-DD')),
(219,109,8,TO_DATE('2023-07-04', 'YYYY-MM-DD')),
(220,109,8,TO_DATE('2021-07-04', 'YYYY-MM-DD')),

-- Wonder Woman (ID: 110, IMDb: 7,3)
(219,110,7,TO_DATE('2023-07-10', 'YYYY-MM-DD')),
(220,110,8,TO_DATE('2022-10-05', 'YYYY-MM-DD')),

-- The Favourite (ID: 111, IMDb: 7,5)
(201,111,8,TO_DATE('2022-06-01', 'YYYY-MM-DD')),
(202,111,7,TO_DATE('2023-07-02', 'YYYY-MM-DD')),

-- The Hunger Games (ID: 112, IMDb: 7,2)
(203,112,7,TO_DATE('2022-09-10', 'YYYY-MM-DD')),
(204,112,7,TO_DATE('2023-07-11', 'YYYY-MM-DD')),

-- Parasite (ID: 113, IMDb: 8,5)
(205,113,9,TO_DATE('2023-07-02', 'YYYY-MM-DD')),
(206,113,8,TO_DATE('2022-11-13', 'YYYY-MM-DD')),
(207,113,9,TO_DATE('2023-07-04', 'YYYY-MM-DD')),
(201,113,8,TO_DATE('2022-08-01', 'YYYY-MM-DD')),

-- Dune (ID: 114, IMDb: 8,0)
(208,114,8,TO_DATE('2023-07-10', 'YYYY-MM-DD')),
(209,114,8,TO_DATE('2022-12-22', 'YYYY-MM-DD')),
(210,114,8,TO_DATE('2023-07-12', 'YYYY-MM-DD')),

-- Spider-Man: No Way Home (ID: 115, IMDb: 8,2)
(211,115,8,TO_DATE('2023-07-15', 'YYYY-MM-DD')),
(212,115,8,TO_DATE('2022-07-16', 'YYYY-MM-DD')),

-- Deadpool (ID: 116, IMDb: 8,2)
(213,116,8,TO_DATE('2023-07-05', 'YYYY-MM-DD')),
(214,116,8,NULL),

-- Sharknado (ID: 117, IMDb: 3,3)
(215,117,5,TO_DATE('2023-07-08', 'YYYY-MM-DD')),
(216,117,3,NULL),
(217,117,3,TO_DATE('2023-07-14', 'YYYY-MM-DD')),

-- Snakes on a Plane (ID: 118, IMDb: 3,3)
(218,118,5,TO_DATE('2023-06-20', 'YYYY-MM-DD')),
(219,118,4,TO_DATE('2022-10-30', 'YYYY-MM-DD')),
(220,118,4,NULL),

-- The Lost World: Jurassic Park (ID: 119, IMDb: 6,9)
(201,119,6,TO_DATE('2023-05-25', 'YYYY-MM-DD')),
(202,119,7,TO_DATE('2022-08-12', 'YYYY-MM-DD')),
(203,119,6,NULL),

-- Twister (ID: 120, IMDb: 6,5)
(204,120,6,TO_DATE('2023-07-18', 'YYYY-MM-DD')),
(205,120,7,TO_DATE('2022-09-13', 'YYYY-MM-DD')),
(206,120,6,TO_DATE('2023-07-20', 'YYYY-MM-DD'))
;



--a)Alle Bewertungen seit dem 01.01.2023:
CREATE VIEW aktuelle_bewertung AS
SELECT b.film_id, f.titel, b.punkte, b.datum
FROM bewertung b
JOIN film f ON b.film_id = f.film_id
WHERE b.datum >= '2023-01-01';

--b)Alle Filme mit mindestens einer Bewertung von mehr als 7 Punkten (duplikatfrei):
CREATE VIEW gute_filme AS
SELECT DISTINCT b.film_id, f.titel
FROM bewertung b
JOIN film f ON b.film_id = f.film_id
WHERE b.punkte > 7;

--c) Alle Filme mit ihrer durchschnittlichen Bewertung, aufsteigend sortiert:
CREATE VIEW ranking AS
SELECT f.film_id, f.titel, AVG(b.punkte) AS durchschnitt
FROM bewertung b
JOIN film f ON b.film_id = f.film_id
GROUP BY f.film_id
ORDER BY durchschnitt ASC;


--d)Änderung des Titels in der Sicht aktuelle_bewertung:
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

--e)Änderung der film_id in der Sicht aktuelle_bewertung:
CREATE FUNCTION update_film_id() 
RETURNS TRIGGER 
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE bewertung SET film_id = NEW.film_id WHERE film_id = OLD.film_id AND datum = OLD.datum;
    RETURN NEW;
END;
$$;

CREATE TRIGGER update_film_id_trigger
INSTEAD OF UPDATE ON aktuelle_bewertung
FOR EACH ROW
EXECUTE FUNCTION update_film_id();


--f)Löschen eines Datensatzes in der Sicht gute_filme:
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
