package Classes;

import jakarta.persistence.*;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int shipId;

    @Column(nullable = false)
    private String type;

    private String name;

    @Lob
    private String skin; // JSON-Speicherung

    // Getter und Setter

    public String getName() {
        return name;
    }

    public int getShipId() {
        return shipId;
    }

    public String getSkin() {
        return skin;
    }

    public String getType() {
        return type;
    }
}
