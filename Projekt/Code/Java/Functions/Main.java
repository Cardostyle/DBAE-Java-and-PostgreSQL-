package Functions;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        PersonRelatedAPI personAPI = new PersonRelatedImpl(session);
        StatisticAPI statisticAPI = new StatisticImpl(session);

        Scanner scanner = new Scanner(System.in);

        try {
            boolean running = true;
            while (running) {
                System.out.println("\n*** Social Network Console ***");
                System.out.println("1) Profil anzeigen");
                System.out.println("2) Gemeinsame Interessen von Freunden");
                System.out.println("3) Gemeinsame Freunde");
                System.out.println("4) Ähnlichste Person mit gleichen Interessen");
                System.out.println("5) Job-Empfehlung");
                System.out.println("6) Kürzester Freundschaftspfad");
                System.out.println("7) Fraktionslevel anzeigen");
                System.out.println("8) Schiffe anzeigen");
                System.out.println();
                System.out.println("10) Tag-Klassen-Hierarchie");
                System.out.println("11) Beliebteste Kommentare");
                System.out.println("12) Land mit den meisten Posts und Kommentaren");
                System.out.println("13) Piraten mit dem Höchstlevel anzeigen");
                System.out.println("0) Beenden");
                System.out.print("Wähle eine Option: ");

                int choice = scanner.nextInt();
                scanner.nextLine();  // Buffer leeren

                Transaction transaction = session.beginTransaction();
                try {
                    System.out.println();
                    switch (choice) {
                        case 1:
                            System.out.print("Gib die Person-ID ein: ");
                            Long personId = scanner.nextLong();
                            System.out.println(personAPI.getProfile(personId));
                            break;
                        case 2:
                            System.out.print("Gib die Person-ID ein: ");
                            Long id = scanner.nextLong();
                            List<String> interests = personAPI.getCommonInterestsOfMyFriends(id);
                            System.out.println("Gemeinsame Interessen: " + interests);
                            break;
                        case 3:
                            System.out.print("Gib die erste Person-ID ein: ");
                            Long idA = scanner.nextLong();
                            System.out.print("Gib die zweite Person-ID ein: ");
                            Long idB = scanner.nextLong();
                            List<String> commonFriends = personAPI.getCommonFriends(idA, idB);
                            System.out.println("Gemeinsame Freunde: " + commonFriends);
                            break;
                        case 4:
                            System.out.print("Gib die Person-ID ein: ");
                            Long idPerson = scanner.nextLong();
                            System.out.println(personAPI.getPersonsWithMostCommonInterests(idPerson));
                            break;
                        case 5:
                            System.out.print("Gib die Person-ID ein: ");
                            Long personForJob = scanner.nextLong();
                            System.out.println(personAPI.getJobRecommendation(personForJob));
                            break;
                        case 6:
                            System.out.print("Gib die Start-Person-ID ein: ");
                            Long startId = scanner.nextLong();
                            System.out.print("Gib die Ziel-Person-ID ein: ");
                            Long endId = scanner.nextLong();
                            System.out.println("Kürzester Pfad: " + personAPI.getShortestFriendshipPath(startId, endId));
                            break;
                        case 7:
                            System.out.print("Gib den Gamertag ein: ");
                            String gamertag = scanner.next();
                            System.out.print("Gib den Fraktionsnamen ein: ");
                            String factionName = scanner.next();
                            Integer level = personAPI.getFractionLvl(gamertag, factionName);
                            System.out.println(level != null ? "Fraktionslevel: " + level : "Spieler nicht in Fraktion gefunden.");
                            break;
                        case 8:
                            System.out.print("Gib den Gamertag ein: ");
                            String gamer = scanner.next();
                            List<String> ships = personAPI.getShips(gamer);
                            System.out.println(ships.isEmpty() ? "Keine Schiffe gefunden." : "Schiffe: " + ships);
                            break;
                        case 10:
                            List<String> hierarchy = statisticAPI.getTagClassHierarchy();
                            System.out.println("Tag-Klassen-Hierarchie:");
                            hierarchy.forEach(System.out::println);
                            break;
                        case 11:
                            System.out.print("Gib die Mindestanzahl an Likes für beliebte Kommentare ein: ");
                            int minLikes = scanner.nextInt();
                            List<String> comments = statisticAPI.getPopularComments(minLikes);
                            System.out.println("Beliebteste Kommentare: " + comments);
                            break;
                        case 12:
                            System.out.println(statisticAPI.getFrequentPostCommentCountry());
                            break;
                        case 13:
                            System.out.print("Gib den Fraktionsnamen ein: ");
                            String faction = scanner.next();
                            List<String> maxedPlayers = statisticAPI.getMaxedPlayers(faction);
                            System.out.println(maxedPlayers.isEmpty() ? "Keine Spieler mit Max-Level gefunden." : "Spieler mit Max-Level: " + maxedPlayers);
                            break;
                        case 0:
                            running = false;
                            System.out.println("Programm beendet.");
                            break;
                        default:
                            System.out.println("Ungültige Eingabe, bitte versuche es erneut.");
                            break;
                    }
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                    System.err.println("Fehler: " + e.getMessage());
                }
            }
        } finally {
            session.close();
            factory.close();
            scanner.close();
        }
    }
}
