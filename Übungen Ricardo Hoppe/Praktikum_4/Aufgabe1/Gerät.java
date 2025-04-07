package Aufgabe1;

import jakarta.persistence.*;

@Entity
@Table(name = "gerät")

//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)  // Table-per-Concrete-Class Strategy

//@Inheritance(strategy = InheritanceType.JOINED)  // Joined Table Strategy

@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Single Table Strategy
@DiscriminatorColumn(name = "device_type", discriminatorType = DiscriminatorType.STRING) // Gibt den Typ der Entität an
public class Gerät {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int inventarnummer;

    public Gerät (int inventarnummer){
        this.inventarnummer = inventarnummer;
    }

    public Gerät() {

    }
}
