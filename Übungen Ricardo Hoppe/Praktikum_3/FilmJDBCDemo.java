import java.sql.*;
import java.util.Scanner;

public class FilmJDBCDemo {

    static Connection con;

    public static void main(String[] args) {
        // Verbindungsdetails anpassen
        String user = "stud05"; // Benutzername für die Datenbank
        String database= "filmdb";
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the password: ");
        String pw = sc.nextLine().trim();

        // Verbindung herstellen
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://pgsql.ins.hs-anhalt.de:5432/stud05_db?currentSchema="+database, user, pw);
            System.out.println("Verbindung zur Datenbank erfolgreich hergestellt!");

            // Abfragen
            //a)
            getFilmCount();
            //b)
            getAverageReview();
            //c)
            getNewestFilms();
            //d)
            getGoodFilms();



            // Verbindung schließen
            con.close();
        } catch (Exception e) {
            System.out.println("Fehler bei der Verbindung zur Datenbank: " + e.getMessage());
            e.printStackTrace();
        }
    }


    //a) Anzahl aller Filme
    private static int getFilmCount() throws SQLException{
        String sql = "SELECT count(*) from film";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        //Ausgabe
        System.out.print("\nAnzahl aller Filme: ");
        while (rs.next()) {
            int output = rs.getInt(1);
            System.out.println(output);
            return output;
        }

    return 0;
    }

    //b)Durchschnittliche Bewertung pro Film
    private static void getAverageReview() throws SQLException{
        String sql = "SELECT titel,  avg(bewertung.punkte) as average from film join bewertung on Film.Film_id = bewertung.film_id group by titel";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("\nBewertungen: ");
        while(rs.next()){
            String titel = rs.getString("titel");
            double bewertung = rs.getDouble("average");
            System.out.println(titel + ": " + bewertung);
        }
    }


    //c)3 aktuellste Filmtitel und Jahr ausgeben - "<Film> erschien im Jahr <jahr>"
    public static void getNewestFilms() throws SQLException{
        String sql = "SELECT titel,  jahr  from film order by jahr desc";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("\nDie drei neuesten Filme:" );
        int counter=0;
        while(rs.next() && counter<3) {
            String titel = rs.getString("titel");
            int jahr = rs.getInt("jahr");
            System.out.println(titel + " erschien im Jahr " + jahr);
            counter++;
        }
    }

    //d)gute Filme aus View (P2 - Aufgabe: 5b)
    public static void getGoodFilms() throws SQLException{
        String sql = "SELECT * from gute_filme";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("\nGute Filme: ");
        while(rs.next()){
            String titel = rs.getString("titel");
            int filmId = rs.getInt("film_id");;
            System.out.println(titel + " : " + filmId);
        }

    }
}
