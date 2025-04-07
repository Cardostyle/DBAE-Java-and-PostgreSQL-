set search_path to Bibliothek;

SELECT buch.titel FROM buch;

SELECT DISTINCT buch.titel FROM buch;

SELECT DISTINCT buch.titel FROM buch order by buch.titel desc;

SELECT buch.titel FROM buch WHERE buch.jahr = 1980;

SELECT buch.titel FROM buch WHERE buch.jahr < 1980;

SELECT buch.titel , Verlag.name FROM buch inner join Verlag on Buch.Verlagsid=Verlag.Verlagsid order by Verlag.name asc , Buch.titel asc;

SELECT distinct( buch.titel , Autor.Nachname )FROM buch 
inner join Buch_Aut on Buch.buchId=buch_aut.buchid 
inner Join Autor on Buch_aut.autorid = Autor.autorid;

SELECT distinct Autor.Nachname , Autor.Vornamen
FROM buch 
inner join Buch_Aut on Buch.buchId=buch_aut.buchid 
inner Join Autor on Buch_aut.autorid = Autor.autorid
inner join Verlag on Buch.Verlagsid=Verlag.Verlagsid
where Verlag.ort like 'Berlin' and buch_aut.rolle like 'h'
order by Autor.nachname asc , autor.vornamen asc;

SELECT distinct Buch.titel, (select date_part('year',(select current_timestamp))-Buch.jahr) as age
FROM buch 
where buch.jahr > 1997
order by age asc, buch.titel asc;


SELECT distinct Autor.Nachname , Autor.Vornamen, autor.autorid
FROM Buch 
inner join Buch_Aut on Buch.buchId=buch_aut.buchid 
inner Join Autor on Buch_aut.autorid = Autor.autorid
inner join Buch_sw on Buch.buchid = buch_sw.buchid
inner join schlagwort on buch_sw.swid = schlagwort.swid
Where schlagwort.schlagwort like 'Datenbank' and buch_aut.rolle like 'v':

SELECT buch.buchid, buch.titel
FROM Buch
JOIN Verlag on buch.verlagsid = verlag.verlagsid
WHERE verlag.name like 'Springer' and buch.jahr > 1989
order by  buch.jahr asc, buch.titel asc;