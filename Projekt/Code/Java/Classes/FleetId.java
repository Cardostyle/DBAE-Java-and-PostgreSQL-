package Classes;

import java.io.Serializable;
import java.util.Objects;

public class FleetId implements Serializable {

    private String captain; // Pirate.gamertag
    private Integer ship;   // Ship.shipId

    public FleetId() {}

    public FleetId(String captain, Integer ship) {
        this.captain = captain;
        this.ship = ship;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FleetId)) return false;
        FleetId that = (FleetId) o;
        return Objects.equals(captain, that.captain) &&
                Objects.equals(ship, that.ship);
    }

    @Override
    public int hashCode() {
        return Objects.hash(captain, ship);
    }
}
