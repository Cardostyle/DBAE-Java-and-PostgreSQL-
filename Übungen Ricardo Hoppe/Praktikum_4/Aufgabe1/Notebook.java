package Aufgabe1;

import Aufgabe1.Rechner;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
//@Table(name = "notebook")
@DiscriminatorValue("Notebook")
public class Notebook extends Rechner {
    int groesse;
    boolean istTablet;

    public Notebook(int inventarnummer,String besitzer, int festplatte, int groesse, boolean istTablet) {
        super(inventarnummer,besitzer,festplatte);
        this.istTablet = istTablet;
        this.groesse = groesse;

    }

    public Notebook() {

    }
}
