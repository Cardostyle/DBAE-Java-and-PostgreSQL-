package Functions;

import Classes.*;

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
        cfg.addAnnotatedClass(City.class);
        cfg.addAnnotatedClass(Comment.class);
        cfg.addAnnotatedClass(Company.class);
        cfg.addAnnotatedClass(Continent.class);
        cfg.addAnnotatedClass(Country.class);
        cfg.addAnnotatedClass(Email.class);
        cfg.addAnnotatedClass(Faction.class);
        cfg.addAnnotatedClass(FactionRelation.class);
        cfg.addAnnotatedClass(Fleet.class);
        cfg.addAnnotatedClass(Forum.class);
        cfg.addAnnotatedClass(ForumHasTag.class);
        cfg.addAnnotatedClass(HasInterest.class);
        cfg.addAnnotatedClass(HasMember.class);
        cfg.addAnnotatedClass(HasType.class);
        cfg.addAnnotatedClass(Knows.class);
        cfg.addAnnotatedClass(Likes.class);
        cfg.addAnnotatedClass(Message.class);
        cfg.addAnnotatedClass(MessageHasTag.class);
        cfg.addAnnotatedClass(Organisation.class);
        cfg.addAnnotatedClass(Person.class);
        cfg.addAnnotatedClass(Pirate.class);
        cfg.addAnnotatedClass(pkp_symmetric.class);
        cfg.addAnnotatedClass(Place.class);
        cfg.addAnnotatedClass(Post.class);
        cfg.addAnnotatedClass(Ship.class);
        cfg.addAnnotatedClass(StudyAt.class);
        cfg.addAnnotatedClass(Tag.class);
        cfg.addAnnotatedClass(TagClass.class);
        cfg.addAnnotatedClass(TagSubClasses.class);
        cfg.addAnnotatedClass(University.class);
        cfg.addAnnotatedClass(WorkAt.class);
        cfg.addAnnotatedClass(WorkHistory.class);




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
            dburl = prop.getProperty("db.url");
            dbuser = prop.getProperty("db.user");
            dbpassword = prop.getProperty("db.password");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
