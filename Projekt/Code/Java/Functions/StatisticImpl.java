package Functions;

import Classes.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.*;

public class StatisticImpl implements StatisticAPI {

    private final Session session;

    public StatisticImpl(Session session) {
        this.session = session;
    }

    @Override
    public List<String> getTagClassHierarchy() {
        // Alle Blätter (TagClasses, die keine Kinder haben) finden
        String leafQuery = "SELECT t.name FROM TagClass t WHERE t.tagClassId NOT IN " +
                "(SELECT DISTINCT ts.parentClass.tagClassId FROM TagSubClasses ts)";
        List<String> leafTagClasses = session.createQuery(leafQuery, String.class).getResultList();

        // Alle Beziehungen zwischen TagClasses laden (Kind → Eltern)
        String hql = "SELECT t1.name, t2.name FROM TagSubClasses ts " +
                "JOIN ts.parentClass t1 " +
                "JOIN ts.childClass t2";
        Query<Object[]> query = session.createQuery(hql, Object[].class);

        // Reverse-Hierarchie als Map speichern (Kind → Eltern)
        Map<String, List<String>> reverseHierarchy = new HashMap<>();
        for (Object[] row : query.getResultList()) {
            String parent = (String) row[0];
            String child = (String) row[1];
            reverseHierarchy.computeIfAbsent(child, k -> new ArrayList<>()).add(parent);
        }

        // Hierarchie von den Blättern aus aufbauen
        List<String> output = new ArrayList<>();
        int leafCounter = 0;
        for (String leaf : leafTagClasses) {
            buildReverseTagHierarchy(leaf, reverseHierarchy, String.valueOf(leafCounter), output);
            leafCounter++;
        }

        return output;
    }

    private void buildReverseTagHierarchy(String child, Map<String, List<String>> reverseHierarchy, String depth, List<String> output) {
        output.add(depth + " " + child); // Speichere die aktuelle Ebene

        if (!reverseHierarchy.containsKey(child)) {
            return; // Keine Eltern vorhanden
        }

        List<String> parents = reverseHierarchy.get(child);
        int counter = 1;
        for (String parent : parents) {
            buildReverseTagHierarchy(parent, reverseHierarchy, depth + "." + counter, output);
            counter++;
        }
    }




    @Override
    public List<String> getPopularComments(int minLikes) {
        String hql = "SELECT c.messageId, c.creator.firstName, c.creator.lastName, COUNT(l.personId) as likeCount " +
                "FROM Likes l " +
                "JOIN l.messageId c " +
                "WHERE TYPE(c) = Comment " +
                "GROUP BY c.messageId, c.creator.firstName, c.creator.lastName " +
                "HAVING COUNT(l.personId) > :minLikes " +
                "ORDER BY likeCount DESC";
        Query<Object[]> query = session.createQuery(hql, Object[].class);
        query.setParameter("minLikes", minLikes);

        List<String> results = new ArrayList<>();
        for (Object[] row : query.getResultList()) {
            results.add("Kommentar " + row[0] + " von " + row[1] + " " + row[2]);
        }
        return results;
    }

    @Override
    public String getFrequentPostCommentCountry() {
        String hql = "SELECT c.country.name " +
                "FROM Message c " +
                "GROUP BY c.country.name, c.country.placeId " +
                "ORDER BY COUNT(c.country.placeId) DESC";

        Query<String> query = session.createQuery(hql, String.class);
        query.setMaxResults(1);

        List<String> result = query.getResultList();
        return result.isEmpty() ? "Keine Daten vorhanden" : "Aktivstes Land: " + result.get(0);
    }

    @Override
    public List<String> getMaxedPlayers(String factionName) {
        String hql = "SELECT fr.gamertag.gamertag FROM FactionRelation fr " +
                "WHERE fr.faction.name = :factionName " +
                "AND fr.level = (SELECT f.maxLvl FROM Faction f WHERE f.name = :factionName)";

        Query<String> query = session.createQuery(hql, String.class);
        query.setParameter("factionName", factionName);

        return query.getResultList();
    }

}
