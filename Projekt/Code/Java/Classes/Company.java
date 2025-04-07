package Classes;

import jakarta.persistence.*;

@Entity
public class Company extends Organisation {

    @ManyToOne
    @JoinColumn(name = "country", referencedColumnName = "placeid", nullable = true)
    private Country country;

    // Getter und Setter
    public Country getCountry() {
        return country;
    }
}
