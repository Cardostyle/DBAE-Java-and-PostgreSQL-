package Aufgabe2_3;

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
            //Abfragen Aller Bücher
            session = factory.openSession();  // Neue Session für die Abfrage
            Transaction transaction = session.beginTransaction();

            // Abfrage aller Bücher
            Query<Book> query = session.createQuery("from Book", Book.class);
            List<Book> books = query.getResultList();

            // Alle Geräte anzeigen
            System.out.println("Abgefragte Bücher:");
            for (Book book : books) {
                System.out.println(book);
            }

            // Commit der Abfrage-Transaktion
            transaction.commit();
            System.out.println("Daten wurden erfolgreich abgerufen und angezeigt!");

            //Aufgabe 3
            System.out.println("\n\nAusgabe aller Bücher, die 2014 veröffentlicht wurden");
            transaction = session.beginTransaction();

            // HQL-Abfrage: Alle Bücher, deren Jahr 2014 ist
            Query<Book> query2 = session.createQuery("from Book where year = :year", Book.class);
            query2.setParameter("year", 2014); // Parameter setzen
            List<Book> books2014 = query2.getResultList();

            // Ausgabe der Ergebnisse
            for (Book book : books2014) {
                System.out.println(book); // Ausgabe jedes Buchs
            }

            // Commit der Transaktion für die zweite Abfrage
            transaction.commit();
            System.out.println("Bücher aus 2014 wurden erfolgreich abgerufen und angezeigt!");



            System.out.println("\n\nAusgabe aller Verlage aus Berlin");
            transaction = session.beginTransaction();

            // HQL-Abfrage: Alle Verlage aus Berlin
            Query<Publisher> query3 = session.createQuery("from Publisher where city = :city",Publisher.class);
            query3.setParameter("city", "Berlin"); // Parameter setzen
            List<Publisher> publisherBerlin = query3.getResultList();

            // Ausgabe der Ergebnisse
            for (Publisher publisher : publisherBerlin) {
                System.out.println(publisher); // Ausgabe jedes Buchs
            }

            // Commit der Transaktion für die zweite Abfrage
            transaction.commit();
            System.out.println("Alle Verlage aus Berlin wurden erfolgreich abgerufen und angezeigt!");


            System.out.println("\n\nAusgabe aller Bücher von Autoren mit Vornamen Lara");
            transaction = session.beginTransaction();

            // HQL-Abfrage: Ausgabe aller Bücher von Autoren mit Vornamen Lara.
            Query<Book> query4 = session.createQuery("select b from Book b " +
                    "join b.bookAuthors ba " +
                    "join ba.author a " +
                    "where a.firstName = :name", Book.class);
            query4.setParameter("name", "Lara"); // Parameter setzen
            List<Book> bookLara = query4.getResultList();

            // Ausgabe der Ergebnisse
            for (Book book : bookLara) {
                System.out.println(book); // Ausgabe jedes Buchs
            }

            // Commit der Transaktion für die zweite Abfrage
            transaction.commit();
            System.out.println("Alle Bücher von Autoren mit Vornamen Lara wurden erfolgreich abgerufen und angezeigt!");

        } finally {
            session.close();  // Session schließen
            factory.close();  // SessionFactory schließen
        }
    }
}
