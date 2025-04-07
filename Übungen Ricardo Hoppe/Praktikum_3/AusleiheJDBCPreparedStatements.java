import java.sql.*;
import java.util.Scanner;

public class AusleiheJDBCPreparedStatements {

    static Connection con;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // Verbindungsdetails anpassen
        String user = "stud05"; // Benutzername für die Datenbank
        String database= "ausleihe";

        System.out.println("Enter the password: ");
        String pw = sc.nextLine().trim();


        // Verbindung herstellen
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://pgsql.ins.hs-anhalt.de:5432/stud05_db?currentSchema=" +database, user, pw);
            System.out.println("Verbindung zur Datenbank erfolgreich hergestellt!");


            //Ausgaben:
            //3.a)
            addNewBook();
            //b)
            addMultipleBooks();


            //4.c)
            System.out.println("NORMALE STATEMENTS:");
            ausgelieheneBuecherStatement("Schmidt");
            ausgelieheneBuecherStatement("A");
            ausgelieheneBuecherStatement("A ' or 'a'='a");
            ausgelieheneBuecherStatement("A ' UNION (SELECT lesername FROM leser)--");

            System.out.println("PREPAIRED STATEMENTS:");
            ausgelieheneBuecherPrepStatement("Schmidt");
            ausgelieheneBuecherPrepStatement("A");
            ausgelieheneBuecherPrepStatement("A ' or 'a'='a");
            ausgelieheneBuecherPrepStatement("A ' UNION (SELECT lesername FROM leser)--");

            //4.d)
            /*
            Lesername = Schmidt:Normales Verhalten, es wird alles korrekt angezeigt
            Lesername = A: Normales Verhalten, keine Bücher sind ausgeliehen und es wird nix angezeigt
            Lesername = A ' or 'a'='a:
                                SQL-Injection bei normalen Statements - alle Bücher werden angezeigt
                                Prepaired Statements schützen vor SQL Injektion und es werden keine Bücher angezeigt weil es keine Nutzer gibt mit dem Leseramen
            Lesername = A ' UNION (SELECT NULL AS isbn, NULL AS explnr, lesername FROM leser)--
                                SQL-Injection bei normalen Statements - Alle Lesernamen welche Bücher ausgeliehen haben werden ausgegeben.
                                Prepaired Statements schützen vor SQL Injektion und es werden keine Bücher angezeigt weil es keine Nutzer gibt mit dem Leseramen
             */



            // Verbindung schließen
            con.close();
        } catch (Exception e) {
            System.out.println("Fehler bei der Verbindung zur Datenbank: " + e.getMessage());
            e.printStackTrace();
        }
    }



    public static void addNewBook() throws SQLException {
        String getMaxExplNrSql = "SELECT COALESCE(MAX(explnr), 0) FROM buchex WHERE isbn = ?";
        String insertSql = "INSERT INTO buchex (isbn, explnr, titel, autorname) VALUES (?, ?, ?, ?)";

        PreparedStatement getMaxExplNrStmt = con.prepareStatement(getMaxExplNrSql);
        PreparedStatement insertStmt = con.prepareStatement(insertSql);

        System.out.println("Geben Sie die ISBN ein:");
        String isbn = sc.nextLine().trim();

        // Abfrage der maximalen Exemplar-Nummer
        getMaxExplNrStmt.setString(1, isbn);
        ResultSet rs = getMaxExplNrStmt.executeQuery();
        int newExplNr = 1; // Standardwert, falls keine Exemplar-Nummer existiert
        if (rs.next()) {
            newExplNr = rs.getInt(1) + 1; // Nächste Exemplar-Nummer
        }

        System.out.println("Geben Sie den Titel ein:");
        String titel = sc.nextLine().trim();

        System.out.println("Geben Sie den Autorennamen ein:");
        String autorname = sc.nextLine().trim();

        // Einfügen des neuen Buches
        insertStmt.setString(1, isbn);
        insertStmt.setInt(2, newExplNr);
        insertStmt.setString(3, titel);
        insertStmt.setString(4, autorname);

        insertStmt.executeUpdate();
        System.out.println("Das Buch wurde erfolgreich hinzugefügt: ISBN = " + isbn + ", Exemplar-Nr = " + newExplNr);
    }


    public static void addMultipleBooks() throws SQLException {
        String getMaxExplNrSql = "SELECT COALESCE(MAX(explnr), 0) FROM buchex WHERE isbn = ?";
        String insertSql = "INSERT INTO buchex (isbn, explnr, titel, autorname) VALUES (?, ?, ?, ?)";

        PreparedStatement getMaxExplNrStmt = con.prepareStatement(getMaxExplNrSql);
        PreparedStatement insertStmt = con.prepareStatement(insertSql);

        boolean inserting = true;
        int batchSize = 0;

        while (inserting) {
            boolean question = true;
            System.out.println("Geben Sie die ISBN ein:");
            String isbn = sc.nextLine().trim();

            // Abfrage der maximalen Exemplar-Nummer für die gegebene ISBN
            getMaxExplNrStmt.setString(1, isbn);
            ResultSet rs = getMaxExplNrStmt.executeQuery();
            int newExplNr = 1; // Standardwert, falls keine Exemplar-Nummer existiert
            if (rs.next()) {
                newExplNr = rs.getInt(1) + 1; // Nächste Exemplar-Nummer
            }

            System.out.println("Geben Sie den Titel ein:");
            String titel = sc.nextLine().trim();

            System.out.println("Geben Sie den Autorennamen ein:");
            String autorname = sc.nextLine().trim();

            // Einfügen in den Batch
            insertStmt.setString(1, isbn);
            insertStmt.setInt(2, newExplNr);
            insertStmt.setString(3, titel);
            insertStmt.setString(4, autorname);

            insertStmt.addBatch(); // Zur Batch hinzufügen
            batchSize++;

            if (batchSize == 5) {
                insertStmt.executeBatch();
                System.out.println(batchSize + " Bücher wurden hinzugefügt");
                batchSize = 0;
            }

            while (question) {
                System.out.println("Möchten Sie ein neues Buch hinzufügen? (ja/nein)");
                String antwort = sc.nextLine().trim().toLowerCase();
                if (antwort.equals("ja")) {
                    question = false;
                } else if (antwort.equals("nein")) {
                    inserting = false;
                    question = false;
                }
            }
        }

        // Restlichen Batch ausführen
        if (batchSize > 0) {
            insertStmt.executeBatch();
            System.out.println(batchSize + " Bücher wurden hinzugefügt");
        }
    }



    public static void ausgelieheneBuecherStatement(String lesername) throws SQLException {
        String sql = "SELECT b.titel " +
                "FROM ausleihe AS a " +
                "JOIN buchex AS b ON a.isbn = b.isbn AND a.explnr = b.explnr " +
                "JOIN leser AS l ON a.leserid = l.lid " +
                "WHERE l.lesername = '" + lesername + "';";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Ausgeliehene Bücher von Leser: " + lesername);
            while (rs.next()) {

                String titel = rs.getString("titel");
                System.out.println("Titel: " + titel);
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim Abrufen der ausgeliehenen Bücher: " + e.getMessage());
        }
    }

    public static void ausgelieheneBuecherPrepStatement(String lesername) throws SQLException {
        String sql = "SELECT  b.titel " +
                "FROM ausleihe AS a " +
                "JOIN buchex AS b ON a.isbn = b.isbn AND a.explnr = b.explnr " +
                "JOIN leser AS l ON a.leserid = l.lid " +
                "WHERE l.lesername = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, lesername);
            try (ResultSet rs = pstmt.executeQuery()) {

                System.out.println("Ausgeliehene Bücher von Leser: " + lesername);
                while (rs.next()) {
                    String titel = rs.getString("titel");
                    System.out.println("Titel: " + titel);
                }
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim Abrufen der ausgeliehenen Bücher: " + e.getMessage());
        }
    }






//Das ist eine Funktion welche alle Bücher und alle Leser ausgibt.
//Dies wurde nur zum Testen verwendet, ist jedoch jetzt nicht mehr nötig.
    public static void output() throws SQLException {
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
