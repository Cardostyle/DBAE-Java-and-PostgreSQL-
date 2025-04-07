package Classes;

import jakarta.persistence.*;

@Entity
@IdClass(FleetId.class)
public class Fleet {

    @Id
    @ManyToOne
    @JoinColumn(name = "captain")
    private Pirate captain;

    @Id
    @ManyToOne
    @JoinColumn(name = "shipid")
    private Ship ship;

    // Getter und Setter
    public Pirate getCaptain() {
        return captain;
    }

    public Ship getShip() {
        return ship;
    }
}
