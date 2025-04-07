package AB4;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Drucker")
@DiscriminatorValue("Drucker")  //t-p-c-h
public class Drucker extends Geraet {
     boolean istDuplex;
     int seitenProMin;

    public Drucker(int inventarnummer, boolean istDuplex, int seitenProMin) {
        super(inventarnummer);
        this.istDuplex = istDuplex;
        this.seitenProMin = seitenProMin;
    }

}