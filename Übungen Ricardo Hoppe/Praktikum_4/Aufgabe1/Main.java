package Aufgabe1;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Hole die SessionFactory aus der HibernateUtil-Klasse
        SessionFactory factory = HibernateUtil.getSessionFactory();

        // Öffne eine Session
        Session session = factory.openSession();

        try {
            // 1. Speichern der Objekte in der Datenbank
            Transaction transaction = session.beginTransaction();

            // Erstelle Objekte
            Drucker dr1 = new Drucker(101, true, 10);
            Notebook n1 = new Notebook(102, "Mary", 100, 12, true);
            Desktop de1 = new Desktop(103, "John", 80, "21", "4-31");
            Gerät g1 = new Gerät(104);

            // Speichern der Objekte in der Datenbank
            session.save(dr1);
            session.save(n1);
            session.save(de1);
            session.save(g1);

            // Commit der Transaktion
            transaction.commit();

            System.out.println("Objekte wurden erfolgreich gespeichert!");

            // 2. Abfragen der gespeicherten Objekte
            session = factory.openSession();  // Neue Session für die Abfrage
            transaction = session.beginTransaction();

            // Abfrage aller Geräte
            Query<Gerät> query = session.createQuery("from Gerät", Gerät.class);
            List<Gerät> geräte = query.getResultList();

            // Alle Geräte anzeigen
            System.out.println("Abgefragte Geräte:");
            for (Gerät gerät : geräte) {
                System.out.println(gerät);
            }

            // Commit der Abfrage-Transaktion
            transaction.commit();
            System.out.println("Daten wurden erfolgreich abgerufen und angezeigt!");

        } finally {
            session.close();  // Session schließen
            factory.close();  // SessionFactory schließen
        }
    }
}
