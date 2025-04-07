package Classes;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
public class Faction implements Serializable {

    @Id
    private String name;

    private String description;

    @Column(nullable = false)
    private int maxLvl;

    // Getter & Setter
    public String getName() {
        return name;
    }

    public int getMaxLvl() {
        return maxLvl;
    }

    public String getDescription() {
        return description;
    }
}
