package A2;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.SessionFactoryObserver;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HibernateUtil {

    /* configuration of Hibernate is possible
     * 1) via hibernate.cfg.xml or
     * 2) via Configuration object
     * this is a small demo using Configuration object with setProperty and addAnnotatedClass etc.
     */

    /*
     * IMPORTANT STEPS
     * 1) modify the file "config.properties" in the already existing directory "resources"
     * 2) replace the db properties:
                 db.user=YOURDBUSER
                db.password=YOURDBPASSWORD
                db.url=YOURDBURL
     */
    private static final SessionFactory sessionFactory;
    private static final StandardServiceRegistry standardServiceRegistry;
    private static String dburl;
    private static String dbuser;
    private static String dbpassword;

    static {
        try {

            // load db properties from resources/config.properties
            setDBProperties();

            Configuration cfg = getConfiguration();
            StandardServiceRegistryBuilder sb = new StandardServiceRegistryBuilder();
            sb.applySettings(cfg.getProperties());
            standardServiceRegistry = sb.build();

            cfg.setSessionFactoryObserver(new SessionFactoryObserver() {
                private static final long  serialVersionUID = 1L;

                @Override
                public void sessionFactoryClosed(SessionFactory factory) {
                    StandardServiceRegistryBuilder.destroy(standardServiceRegistry);
                }
            });

            sessionFactory = cfg.buildSessionFactory(standardServiceRegistry);
        } catch (Throwable th) {
            System.err.println("Initial SessionFactory creation failed" + th);
            throw new ExceptionInInitializerError(th);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session openSession() {
        return sessionFactory.openSession();
    }

    private static Configuration getConfiguration() {

        Configuration cfg = new Configuration();
        cfg.addAnnotatedClass(Author.class);
        cfg.addAnnotatedClass(Book.class);
        cfg.addAnnotatedClass(BookAuthor.class);
        cfg.addAnnotatedClass(Publisher.class);
        cfg.addAnnotatedClass(BookAuthorId.class);
        //cfg.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        cfg.setProperty("hibernate.connection.url",dburl);
        cfg.setProperty("hibernate.connection.username", dbuser);
        cfg.setProperty("hibernate.connection.password", dbpassword);
        cfg.setProperty("hibernate.show_sql", "false");

        /*
         * hibernate.hbm2ddl.auto configuration property is used to customize the Hibernate database schema generation process
         * none – This option disables the hbm2ddl.auto tool, so Hibernate is not going to take any action for managing the underlying database schema.
         * create – This option instructs Hibernate to drop the database schema and recreate it afterward using the entity model as a reference.
         */

        cfg.setProperty("hibernate.hbm2ddl.auto", "none"); // e.g. none | validate | update | create | create-drop
        //cfg.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
        //cfg.setProperty("hibernate.current_session_context_class", "thread");
        return cfg;

    }

    private static void setDBProperties(){
        try (InputStream input = new FileInputStream("./src/main/resources/config.properties")) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            dburl = "jdbc:postgresql://pgsql.ins.hs-anhalt.de:5432/stud02_db?currentSchema=kleinebibliothek";
            dbuser = prop.getProperty("db.user");
            dbpassword = prop.getProperty("db.password");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
