package Aufgabe1;

import jakarta.persistence.*;

@Entity
//@Table(name = "rechner")
@DiscriminatorValue("Rechner")
public abstract class  Rechner extends Gerät {
    String besitzer;
    int festplatte;

    public Rechner(int inventarnummer,String besitzer, int festplatte) {
            super(inventarnummer);
            this.besitzer = besitzer;
            this.festplatte = festplatte;
    }

    public Rechner() {

    }
}
