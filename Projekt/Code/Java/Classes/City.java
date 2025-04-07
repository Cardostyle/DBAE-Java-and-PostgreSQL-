package Classes;

import jakarta.persistence.*;

@Entity
public class City extends Place {

    @ManyToOne
    @JoinColumn(name = "country", referencedColumnName = "placeid", nullable = true)
    private Country country;

    // Getter und Setter
    public Country getCountry() {
        return country;
    }
}
