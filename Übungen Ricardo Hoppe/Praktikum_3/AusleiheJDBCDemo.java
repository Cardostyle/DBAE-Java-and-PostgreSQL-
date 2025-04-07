import java.sql.*;
import java.util.Scanner;

public class AusleiheJDBCDemo {

    static Connection con;


    public static void main(String[] args) {
        // Verbindungsdetails anpassen
        String user = "stud05"; // Benutzername für die Datenbank
        String database= "ausleihe"; //Name des Schemas
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the password: ");
        String pw = sc.nextLine().trim(); //Passwort für den Benutzernamen

        // Verbindung herstellen
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://pgsql.ins.hs-anhalt.de:5432/stud05_db?currentSchema=" +database, user, pw);
            System.out.println("Verbindung zur Datenbank erfolgreich hergestellt!");


            //Ausgaben:
            //2.a)
            System.out.println("Bitte LeserID eingeben: ");
            String lid=sc.nextLine().trim();
            getTitleByID(Integer.parseInt(lid));


            //2.b)
            System.out.println("Bitte LeserID eingeben: ");
            lid=sc.nextLine().trim();
            System.out.println("Bitte ISBN eingeben: ");
            String isbn=sc.nextLine().trim();
            setNewAusleihe(Integer.parseInt(lid),isbn);


            // Verbindung schließen
            con.close();
        } catch (Exception e) {
            System.out.println("Fehler bei der Verbindung zur Datenbank: " + e.getMessage());
            e.printStackTrace();
        }
    }
    //Buchex.titel
    public static void getTitleByID(int lid) throws SQLException {
        String sql =
                "SELECT Buchex.titel, Buchex.isbn, buchex.explnr FROM ausleihe " +
                        "JOIN leser ON ausleihe.leserid = leser.lid " +
                        "JOIN buchex ON buchex.isbn = ausleihe.isbn " +
                        "WHERE ausleihe.leserid = " + lid;                                 //SQL Befehl ohne Prepaired Statement = anfällig für SQL Injection
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql); //Ausführen des SQL statements/Query

        System.out.println("\nAusgeliehene Bücher von Leser mit ID " + lid + ": ");
        while(rs.next()){  //pointer
            String titel = rs.getString("titel");
            String isbn = rs.getString("isbn");
            String explnr = rs.getString("explnr");
            System.out.println("ISBN: " + isbn +" Exemplarnummer: "+ explnr+" Titel: "+titel);
        }
    }


    public static void setNewAusleihe(int leserId,String isbn) throws SQLException{
        // 1. Freie Exemplar-Nummer für das Buch mit der ISBN finden
        String findFreeExemplarSQL = "SELECT explnr FROM buchex WHERE isbn = '" + isbn +
                "' AND explnr NOT IN (SELECT explnr FROM ausleihe WHERE isbn = '" + isbn + "')";  //SQL Befehl ohne Prepaired Statement = anfällig für SQL Injection
        Statement findFreeExemplarStmt = con.createStatement();
        ResultSet rs = findFreeExemplarStmt.executeQuery(findFreeExemplarSQL);//Ausführen des SQL statements/Query

        if (rs.next()) {
            int explNr = rs.getInt("explnr");

            // 2. Eintrag in die Tabelle Ausleihe einfügen
            String insertAusleiheSQL = "INSERT INTO ausleihe (isbn, explnr, datum, leserid) " +
                    "VALUES ('" + isbn + "', " + explNr + ", current_date, " + leserId + ")"; //SQL Befehl ohne Prepaired Statement = anfällig für SQL Injection
            Statement insertStmt = con.createStatement();

            try {
                int rowsAffected = insertStmt.executeUpdate(insertAusleiheSQL); //Ausführen des SQL statements/Query
                if (rowsAffected > 0) {
                    System.out.println("Buch erfolgreich ausgeliehen.");
                }
            } catch (SQLException e) {
                // Fehlerbehandlung bei Integritätsverletzungen oder Trigger-Ausführung
                System.err.println("Fehler bei der Ausleihe: " + e.getSQLState() + " - " + e.getMessage());
            }
        } else {
            System.out.println("Kein verfügbares Exemplar für ISBN " + isbn + " gefunden.");
        }
    }




    //Das ist eine Funktion welche alle Bücher und alle Leser ausgibt.
    //Dies wurde nur zum Testen verwendet, ist jedoch jetzt nicht mehr nötig.
    public static void output() throws SQLException{
        // Abfrage für alle Daten aus der Tabelle 'buchex'
        String sqlBuchex = "SELECT * FROM buchex";
        Statement stmtBuchex = con.createStatement();
        ResultSet rsBuchex = stmtBuchex.executeQuery(sqlBuchex);
        System.out.println("Daten aus der Tabelle 'buchex':");
        while (rsBuchex.next()) {
            String isbn = rsBuchex.getString("isbn");
            int explNr = rsBuchex.getInt("explnr");
            String titel = rsBuchex.getString("titel");
            String autorname = rsBuchex.getString("autorname");
            System.out.println("ISBN: " + isbn + ", Exemplar-Nr: " + explNr + ", Titel: " + titel + ", Autor: " + autorname);
        }

        // Abfrage für alle Daten aus der Tabelle 'leser'
        String sqlLeser = "SELECT * FROM leser";
        Statement stmtLeser = con.createStatement();
        ResultSet rsLeser = stmtLeser.executeQuery(sqlLeser);
        System.out.println("\nDaten aus der Tabelle 'leser':");
        while (rsLeser.next()) {
            int lid = rsLeser.getInt("lid");
            String lesername = rsLeser.getString("lesername");
            int gebjahr = rsLeser.getInt("gebjahr");
            System.out.println("Leser-ID: " + lid + ", Name: " + lesername + ", Geburtsjahr: " + gebjahr);
        }
    }



}
