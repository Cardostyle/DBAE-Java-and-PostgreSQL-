package Classes;

import jakarta.persistence.*;

@Entity
public class Country extends Place {

    @ManyToOne
    @JoinColumn(name = "continent", referencedColumnName = "placeid", nullable = true)
    private Continent continent;

    // Getter und Setter
    public Continent getContinent() {
        return continent;
    }
}
