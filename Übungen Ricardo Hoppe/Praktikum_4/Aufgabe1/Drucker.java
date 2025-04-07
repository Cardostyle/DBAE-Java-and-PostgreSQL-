package Aufgabe1;

import jakarta.persistence.*;

@Entity
//@Table(name = "drucker")
@DiscriminatorValue("Drucker")
public class Drucker extends Gerät{
    boolean istDuplex;
    int seitenProMin;

    public Drucker(int inventarnummer, boolean istDuplex, int seitenProMin) {
        super(inventarnummer);
        this.istDuplex = istDuplex;
        this.seitenProMin = seitenProMin;
    }

    public Drucker() {

    }
}
