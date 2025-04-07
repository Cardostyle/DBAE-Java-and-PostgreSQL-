package Classes;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Place implements Serializable {

    @Id
    private int placeId;

    @Column(nullable = false)
    private String name;

    // Getter und Setter
    public String getName() {
        return name;
    }

    public int getPlaceId() {
        return placeId;
    }
}
