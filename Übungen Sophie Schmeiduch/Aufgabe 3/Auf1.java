import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class Auf1 {

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
            con = DriverManager.getConnection("jdbc:postgresql://" + host + "/" + db + "?currentSchema=filmdb", user, pw);
            // 1
            getFilm();
            getBewertung();
            getNeusteFilme();
            getGuteFilme();

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

    private static void getFilm() throws Exception {
        String query = "SELECT COUNT(*) FROM filmdb.film";
        try (PreparedStatement pstmt = con.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                System.out.println(rs.getInt(1));
            }
        }
    }

    private static void getBewertung() throws Exception {
        String query = "SELECT b.titel, AVG(a.punkte) AS avg_rating FROM filmdb.bewertung a " +
                "JOIN filmdb.film b ON a.film_id = b.film_id GROUP BY b.titel";
        try (PreparedStatement pstmt = con.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                System.out.println(rs.getString("titel") + " : " + rs.getDouble("avg_rating"));
            }
        }
    }

    private static void getNeusteFilme() throws Exception {
        String query = "SELECT titel, jahr FROM filmdb.film ORDER BY jahr DESC LIMIT 3";
        try (PreparedStatement pstmt = con.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                System.out.println(rs.getString("titel") + " erschien im Jahr " + rs.getString("jahr") + ".");
            }
        }
    }

    private static void getGuteFilme() throws Exception {
        String query = "SELECT film_id, titel FROM filmdb.gute_filme";
        try (PreparedStatement pstmt = con.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                System.out.println(rs.getString("film_id") + " : " + rs.getString("titel"));
            }
        }
    }
}
