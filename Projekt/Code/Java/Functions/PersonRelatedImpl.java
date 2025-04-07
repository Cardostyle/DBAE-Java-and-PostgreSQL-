package Functions;

import Classes.*;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.*;

public class PersonRelatedImpl implements PersonRelatedAPI {

    private final Session session;

    public PersonRelatedImpl(Session session) {
        this.session = session;
    }

    @Override
    public String getProfile(Long personId) {
        Person person = session.get(Person.class, personId);
        if (person == null) {
            return "Person mit ID " + personId + " nicht gefunden.";
        }
        return String.format("Profil von %s %s (ID=%d, gender=%s, city=%s)",
                person.getFirstName(),
                person.getLastName(),
                person.getPersonId(),
                person.getGender(),
                person.getCity() != null ? person.getCity().getName() : "unbekannt");
    }

    @Override
    public List<String> getCommonInterestsOfMyFriends(Long personId) {
        String hql = "SELECT DISTINCT t.name " +
                "FROM Tag t " +
                "JOIN HasInterest hsa ON t.tagId = hsa.tagId.tagId " +
                "JOIN Person pa ON pa.personId = hsa.personId.personId " +
                "JOIN pkp_symmetric pkp ON pkp.personA = pa " +
                "JOIN Person pb ON pb = pkp.personB " +
                "WHERE  pkp.personA.personId = :personId AND hsa.tagId.tagId IN ( " +
                "    SELECT hs2.tagId.tagId FROM HasInterest hs2 WHERE hs2.personId.personId = pb.personId " +
                ")";

        Query<String> query = session.createQuery(hql, String.class);
        query.setParameter("personId", personId);
        return query.getResultList();
    }


    @Override
    public List<String> getCommonFriends(Long personIdA, Long personIdB) {
        String hql = "SELECT DISTINCT p.firstName, p.lastName " +
                "FROM Person p " +
                "JOIN pkp_symmetric pkp1 ON p.personId = pkp1.personB.personId " +
                "JOIN pkp_symmetric pkp2 ON p.personId = pkp2.personB.personId " +
                "WHERE pkp1.personA.personId = :personA " +
                "AND pkp2.personA.personId = :personB";

        Query<Object[]> query = session.createQuery(hql, Object[].class);
        query.setParameter("personA", personIdA);
        query.setParameter("personB", personIdB);

        List<String> commonFriends = new ArrayList<>();
        for (Object[] result : query.getResultList()) {
            commonFriends.add(result[0] + " " + result[1]);
        }
        return commonFriends;
    }



    @Override
    public String getPersonsWithMostCommonInterests(Long personId) {
        String hql = "SELECT p, COUNT(h.tagId.id) as commonInterestCount FROM HasInterest h " +
                "JOIN h.personId p " +
                "WHERE h.tagId.id IN (SELECT hi.tagId.id FROM HasInterest hi WHERE hi.personId.id = :personId) " +
                "AND p.id <> :personId " +
                "GROUP BY p " +
                "ORDER BY commonInterestCount DESC";
        Query<Object[]> query = session.createQuery(hql, Object[].class);
        query.setParameter("personId", personId);
        query.setMaxResults(1);

        List<Object[]> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return "Keine Person mit gemeinsamen Interessen gefunden.";
        }

        Person similarPerson = (Person) resultList.get(0)[0];
        return "Person mit den meisten gemeinsamen Interessen: " +
                similarPerson.getFirstName() + " " + similarPerson.getLastName();
    }

    @Override
    public List<Organisation> getJobRecommendation(Long personId) {
        String hql = "SELECT DISTINCT org.name " +
                "FROM Organisation org " +
                "JOIN University u ON u.organisationId = org.organisationId " +
                "JOIN City c ON u.city.placeId = c.placeId " +
                "JOIN Country co ON c.country.placeId = co.placeId " +
                "JOIN Company com ON com.country.placeId = co.placeId " +
                "JOIN WorkAt w ON w.company.organisationId = com.organisationId " +
                "JOIN StudyAt s ON s.university.organisationId = u.organisationId " +
                "JOIN pkp_symmetric pkp ON pkp.personA.personId = :personId " +
                "WHERE pkp.personA.personId=:personId " +
                "AND (u.city.placeId = c.placeId AND com.country.placeId = c.country.placeId) " +
                "AND EXISTS ( " +
                "    SELECT 1 FROM pkp_symmetric pkp2 " +
                "    LEFT JOIN WorkAt w2 ON w2.person = pkp2.personB AND w2.company.organisationId = com.organisationId " +
                "    LEFT JOIN StudyAt s2 ON s2.person = pkp2.personB AND s2.university.organisationId = u.organisationId " +
                "    WHERE pkp2.personA = pkp.personA " +
                "    AND (w2.person.personId IS NOT NULL OR s2.person.personId IS NOT NULL) " +
                ")";

        Query<Organisation> query = session.createQuery(hql, Organisation.class);
        query.setParameter("personId", personId);
        //query.setMaxResults(2);  // Maximal zwei Empfehlungen

        return query.getResultList();
    }


    @Override
    public List<String> getShortestFriendshipPath(Long personId, Long otherPersonId) {
        StoredProcedureQuery query = session.createStoredProcedureQuery("getShortestFriendshipPath");

        // Parameter registrieren
        query.registerStoredProcedureParameter("p_startId", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_endId", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("result_cursor", void.class, ParameterMode.REF_CURSOR);

        // Parameter setzen
        query.setParameter("p_startId", personId);
        query.setParameter("p_endId", otherPersonId);

        // Prozedur ausführen
        query.execute();

        // Ergebnis abrufen (Liste von Namen)
        List<String> resultList = query.getResultList();

        return resultList;
    }


    @Override
    public Integer getFractionLvl(String Gamertag, String factionName) {
        String hql = "SELECT fr.level FROM FactionRelation fr " +
                "WHERE fr.gamertag.gamertag = :gamertag AND fr.faction.name = :factionName";
        Query<Float> query = session.createQuery(hql, Float.class);
        query.setParameter("gamertag", Gamertag);
        query.setParameter("factionName", factionName);

        List<Float> result = query.getResultList();
        return result.isEmpty() ? null : Math.round(result.getFirst()); // Float runden und zurückgeben
    }


    @Override
    public List<String> getShips(String Gamertag) {
        String hql = "SELECT s.name FROM Ship s " +
                "WHERE s.shipId = (select ship.shipId from Fleet where captain.gamertag=:gamertag)";
        Query<String> query = session.createQuery(hql, String.class);
        query.setParameter("gamertag", Gamertag);

        return query.getResultList();
    }

}
