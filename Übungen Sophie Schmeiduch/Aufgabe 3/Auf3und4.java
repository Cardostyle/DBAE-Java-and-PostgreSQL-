import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class Auf3und4 {

    static Connection con;

    public static void main(String[] args) {
        String[] loginArray = new String[4];

        // Login über Datei
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/java/Login.txt"))) {
            for (int i = 0; i < 4; i++) {
                loginArray[i] = bufferedReader.readLine();
                if (loginArray[i] == null) {
                    throw new IOException("Die Login-Datei ist unvollständig.");
                }
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Lesen der Login-Datei: " + e.getMessage());
            return;
        }

        // Login-Parameter auslesen
        String host = loginArray[0];
        String db = loginArray[1];
        String user = loginArray[2];
        String pw = loginArray[3];

        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://" + host + "/" + db + "?currentSchema=ausleihe", user, pw);
            //3
            //BuchAnlegen(); //20 mal
            //BuchgruppeAnlegen();

            //4
            System.out.println("Lesername: ");
            String lesername = new Scanner(System.in).nextLine();
            System.out.println("Statement zu '" + lesername +"': ");
            ausgelieheneBuecherStatement(lesername);
            System.out.println("PrepStatement zu '" + lesername +"': ");
            ausgelieheneBuecherPrepStatement(lesername);
            // Schmidt: bei beiden gleich, da normal in der Datenbank vorhanden
            // A : bei beiden nichts, da es keinen Leser des namens "A" gibt
            // A' or 'a'='a : Beim ersten Java, beim zweiten nichts. Durch Sql-Injection wurden hier alle Titel in der Ausleihe (a) ausgegeben.
            // A' UNION (Select lesername From leser)-- : Beim ersten Schmidt und Mueller, beim zweiten nichts. Durch Sql-Injection wurden hier
            //                                            alle Lesernamen in der Tabelle Leser ausgegeben.

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static int getMaxExplnr(String isbn) throws Exception {
        String queryMaxExplnr = "Select Max(explnr) from ausleihe.buchex where isbn = ?";
        int explnr;
        try (PreparedStatement pstmt = con.prepareStatement(queryMaxExplnr)) {
            pstmt.setString(1, isbn);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    explnr = rs.getInt(1) + 1;
                } else {
                    explnr = 1;
                }
            }
        }
        return explnr;
    }

    private static void BuchAnlegen() throws Exception {
        //explnr
        System.out.print("ISBN eingeben: ");
        String isbn = new Scanner(System.in).nextLine();
        int explnr = getMaxExplnr(isbn);
        //Buch anlegen
        System.out.print("Titel eingeben: ");
        String titel = new Scanner(System.in).nextLine();
        System.out.print("Autor eingeben: ");
        String autor = new Scanner(System.in).nextLine();
        String queryBuchInsert = "Insert into ausleihe.buchex values(?,?,?,?)";
        try (PreparedStatement pstmt = con.prepareStatement(queryBuchInsert)) {
            pstmt.setString(1, isbn);
            pstmt.setInt(2, explnr);
            pstmt.setString(3, titel);
            pstmt.setString(4, autor);

            pstmt.executeUpdate();
            System.out.println("Das Buch " + titel + " von " + autor + " wurde erfolgreich mit der ISBN " + isbn + " als Exemplar " + explnr + " angelegt.");
        }
    }

    private static void BuchgruppeAnlegen() throws Exception {
        int size = 0;
        boolean einfuegen = true;

        while (einfuegen) {
            boolean weiteresBuch = true;
            //explnr
            System.out.print("ISBN eingeben: ");
            String isbn = new Scanner(System.in).nextLine();
            int explnr = getMaxExplnr(isbn);
            //Buecher anlegen
            System.out.print("Titel eingeben: ");
            String titel = new Scanner(System.in).nextLine();
            System.out.print("Autor eingeben: ");
            String autor = new Scanner(System.in).nextLine();
            String queryBuchInsert = "Insert into ausleihe.buchex values(?,?,?,?)";
            try (PreparedStatement pstmt = con.prepareStatement(queryBuchInsert)) {
                pstmt.setString(1, isbn);
                pstmt.setInt(2, explnr);
                pstmt.setString(3, titel);
                pstmt.setString(4, autor);
                pstmt.addBatch();
                size++;

                System.out.println("Weiteres Buch anlegen? (j/n): ");
                String antwort = new Scanner(System.in).nextLine();
                if (antwort.equals("n")) {
                    weiteresBuch = false;
                } else if (antwort.equals("j")) {
                    weiteresBuch = true;
                }


                if (size == 5 || !weiteresBuch) {
                    pstmt.executeBatch();
                    if (size == 1) {
                        System.out.println("1 Buch wurde angelegt");
                    } else if (size > 1) {
                        System.out.println(size + " Bücher wurden angelegt");
                    } else {
                        System.out.println("Es wurde kein Buch angelegt.");
                    }
                    if (!weiteresBuch) {
                        einfuegen = false;
                    }

                }
            }

        }
    }

    private static void ausgelieheneBuecherStatement (String lersername) throws Exception {
        String queryBuecher = "SELECT b.Titel FROM ausleihe.ausleihe a " +
                "JOIN ausleihe.buchex b ON a.isbn = b.isbn AND a.explnr = b.explnr " +
                "JOIN ausleihe.leser l ON a.leserid = l.lid WHERE l.lesername = '" + lersername + "'";
        try (PreparedStatement pstmt = con.prepareStatement(queryBuecher)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println(rs.getString("Titel"));
                }
            }
        }
    }

    private static void ausgelieheneBuecherPrepStatement (String lersername) throws Exception {
        String queryBuecher = "SELECT b.Titel FROM ausleihe.ausleihe a " +
                "JOIN ausleihe.buchex b ON a.isbn = b.isbn AND a.explnr = b.explnr " +
                "JOIN ausleihe.leser l ON a.leserid = l.lid WHERE l.lesername = ? ";
        try (PreparedStatement pstmt = con.prepareStatement(queryBuecher)) {
            pstmt.setString(1, lersername);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println(rs.getString("Titel"));
                }
            }
        }
    }
}
