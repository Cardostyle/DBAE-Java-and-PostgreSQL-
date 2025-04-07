package AB4;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;


public class Main {
    public static void main(String[] args) {


        Drucker drucker = new Drucker(101,true,10);
        Notebook notebook = new Notebook(102,"Mary",100,12,true);
        Desktop desktop = new Desktop(103, "John",80,"21","4-31");
        Geraet geraet = new Geraet(104);

        //Beginnen
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = HibernateUtil.openSession();
        session.beginTransaction();

        session.save(drucker);
        session.save(notebook);
        session.save(desktop);
        session.save(geraet);
        session.getTransaction().commit();
        session.beginTransaction();


        List<Geraet> gList = session.createQuery(
                        "from Geraet ", Geraet.class)
                .getResultList();
        System.out.println("\nGereate:");
        for(Geraet g : gList){
            System.out.println(g);
        }

        session.close();
        HibernateUtil.getSessionFactory().close();

        //d als extra pdf in /Aufgabe 4


        //e
        //Table-per-concrete-class:
        //(Jede Klasse eine Tabelle, außer Basisklasse -> Redundanzen in den Tabellen)
        // Gut für kleine Hierarchien mit seltenen Änderungen in der Struktur, aber verurschat Redundanzen.
        //Table-per-subclass:
        //(Jede Klasse eine Tabelle, Joins auf Fremdschlüsselbezieheung )
        // Gut bei komplexen Hierarchien und häufiger Nutzung von JOINs, aber Joins brauchen Arbeitsspeicher.
        //Table-per-class-hierarchy:
        //(Eine Tabelle für alle Klassen, Hierachie wird durch extra Spalte festgehalten -> viele "Null" - Tupel)
        // Optimal für flache Hierarchien mit häufiger Abfrage aller Daten, aber wird bei komplexeren Hierachien oder Abfragen zu unübersichtlich.
    }
}
