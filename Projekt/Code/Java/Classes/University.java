package Classes;

import jakarta.persistence.*;

@Entity
public class University extends Organisation {

    @ManyToOne
    @JoinColumn(name = "city", referencedColumnName = "placeid", nullable = true)
    private City city;

    // Getter und Setter
    public City getCity() {
        return city;
    }

}
