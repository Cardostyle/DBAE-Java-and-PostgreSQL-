package Classes;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Organisation implements Serializable {

    @Id
    private int organisationId;

    @Column(nullable = false)
    private String name;

    // Getter und Setter
    public String getName() {
        return name;
    }

    public int getOrganisationId() {
        return organisationId;
    }
}
