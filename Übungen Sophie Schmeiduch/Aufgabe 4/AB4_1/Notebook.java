package AB4;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Notebook")
@DiscriminatorValue("Notebook") //t-p-c-h
public class Notebook extends Rechner {
     int groesse;
     boolean istTablet;

    public Notebook(int inventarnummer, String besitzer, int festplatte, int groesse, boolean istTablet) {
        super(inventarnummer, besitzer, festplatte);
        this.groesse = groesse;
        this.istTablet = istTablet;
    }


}
