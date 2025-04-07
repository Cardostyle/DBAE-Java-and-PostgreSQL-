package A2;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.management.Query;
import java.util.List;


public class Main {
    public static void main(String[] args) {

        //Beginnen
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = HibernateUtil.openSession();
        session.beginTransaction();


        session.getTransaction().commit();
        session.beginTransaction();


        List<Book> bookList = session.createQuery(
                        "from Book ", Book.class)
                .getResultList();
        System.out.println("\nBücher:");
        for(Book b : bookList){
            System.out.println(b);
        }

        //3
        //a
        List<Book> aList = session.createQuery(
                        "from Book where year = :year ", Book.class).setParameter("year", 2014)
                .getResultList();
        System.out.println("\nBücher:");
        for(Book b : aList){
            System.out.println(b);
        }

        //b
        List<Publisher> bList = session.createQuery(
                        "from Publisher where city = :city ", Publisher.class).setParameter("city", "Berlin")
                .getResultList();
        System.out.println("\nBücher:");
        for(Publisher b : bList){
            System.out.println(b);
        }

        //c
        List<Book> cList = session.createQuery(
                        "Select b from Book b " +
                                "join b.bookAuthors ba " +
                                "join ba.author p " +
                                "where p.authorName = :name ", Book.class).setParameter("name", "Lara")
                .getResultList();
        System.out.println("\nBücher:");
        for(Book b : cList){
            System.out.println(b);
        }



        session.close();
        HibernateUtil.getSessionFactory().close();



    }
}
