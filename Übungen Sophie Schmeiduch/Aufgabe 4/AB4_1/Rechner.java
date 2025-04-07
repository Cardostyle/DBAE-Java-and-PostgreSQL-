package AB4;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="Rechner")
@DiscriminatorValue("Rechner")  //t-p-c-h
public abstract class Rechner extends Geraet {
     String besitzer;
     int festplatte;

    public Rechner(int inventarnummer, String besitzer, int festplatte) {
        super(inventarnummer);
        this.besitzer = besitzer;
        this.festplatte = festplatte;
    }

}