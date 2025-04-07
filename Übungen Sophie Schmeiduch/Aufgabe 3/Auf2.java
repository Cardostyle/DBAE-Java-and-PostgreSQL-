import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class Auf2 {

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
            // 2
            con = DriverManager.getConnection("jdbc:postgresql://" + host + "/" + db + "?currentSchema=ausleihe", user, pw);
            getBuchZuLeser();
            getBuchVerleihen();


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
    private static void getBuchZuLeser() throws Exception {
        System.out.print("Leser-ID eingeben: ");
        int lid = Integer.parseInt(new Scanner(System.in).nextLine());
        String query = "SELECT b.Titel FROM ausleihe.ausleihe a " +
                "JOIN ausleihe.buchex b ON a.isbn = b.isbn AND a.explnr = b.explnr WHERE a.leserid = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, lid);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println(rs.getString("Titel"));
                }
            }
        }
    }

    private static void getBuchVerleihen() throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Leser-ID eingeben: ");
        int lid = Integer.parseInt(scanner.nextLine());

        System.out.print("ISBN eingeben: ");
        String isbn = scanner.nextLine();

        String queryFreiesExemplar = "SELECT explnr FROM ausleihe.buchex " +
                "WHERE isbn = ? AND explnr NOT IN " +
                "(SELECT explnr FROM ausleihe.ausleihe WHERE isbn = ?) LIMIT 1";

        try (PreparedStatement pstmtFreiesExemplar = con.prepareStatement(queryFreiesExemplar)) {
            pstmtFreiesExemplar.setString(1, isbn);
            pstmtFreiesExemplar.setString(2, isbn);

            try (ResultSet rs = pstmtFreiesExemplar.executeQuery()) {
                if (rs.next()) {
                    int explnr = rs.getInt("explnr");

                    String queryInsertAusleihe = "INSERT INTO ausleihe.ausleihe (isbn, explnr, datum, leserid) " +
                            "VALUES (?, ?, current_date, ?)";
                    try (PreparedStatement pstmtInsert = con.prepareStatement(queryInsertAusleihe)) {
                        pstmtInsert.setString(1, isbn);
                        pstmtInsert.setInt(2, explnr);
                        pstmtInsert.setInt(3, lid);

                        pstmtInsert.executeUpdate();
                        System.out.println("Ausleihe erfolgreich hinzugefügt: Leser-ID " + lid +
                                ", ISBN " + isbn + ", Exemplar-Nr. " + explnr);
                    }
                } else {
                    System.out.println("Kein freies Exemplar mit der ISBN: " + isbn + " verfügbar.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Einfügen der Ausleihe:");
            System.err.println("SQL-Status: " + e.getSQLState());
            System.err.println("Fehlermeldung: " + e.getMessage());

        }
    }
}