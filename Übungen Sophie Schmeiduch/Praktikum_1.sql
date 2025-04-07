SET search_path TO Bibliothek;

--a)
Select titel from buch;

--b)
Select distinct titel from buch;

--c)
Select distinct titel from buch order by titel DESC;

--d)
Select distinct titel from buch where jahr = 1980;

--e)
Select titel from buch where jahr < 1980;

--f)
Select b.titel,v.name from buch b Join verlag v on v.verlagsid=b.verlagsid order by v.name, b.titel

--g)
Select Distinct b.titel,a.nachname from buch b 
Join buch_aut ba on b.buchid = ba.buchid
join autor a on a.autorid=ba.autorid 

--h)
Select Distinct a.vornamen,a.nachname from autor a 
Join buch_aut ba on a.autorid = ba.autorid
join buch b on b.buchid=ba.buchid
Join verlag ve on ve.verlagsid = b.verlagsid
where ba.rolle = 'h' and ve.ort = 'Berlin' order by a.nachname 

--i)
Select b.titel, (select date_part('year',(select current_timestamp))-b.jahr) as age from buch b where b.jahr >1997
order by age ASC, b.titel ASC 

--j)
Select a.nachname, a.vornamen, a.autorid from autor a 
Join buch_aut ba on a.autorid = ba.autorid
join buch b on b.buchid=ba.buchid
Join buch_sw sw on sw.buchid = b.buchid
join schlagwort sch on sch.swid = sw.swid
where ba.rolle = 'v' and sch.schlagwort = 'Datenbank' 

--k)
Select b.buchid, b.titel from buch b 
Join verlag ve on ve.verlagsid = b.verlagsid
where b.jahr >= 1990 and ve.name = 'Springer' 
order by b.jahr ASC, b.titel ASC
