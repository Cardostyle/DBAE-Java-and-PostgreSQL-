package AB4;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Desktop")
@DiscriminatorValue("Desktop")  //t-p-c-h
public class Desktop extends Rechner {
     String monitor;
     String raum;

    public Desktop(int inventarnummer, String besitzer, int festplatte, String monitor, String raum) {
        super(inventarnummer, besitzer, festplatte);
        this.monitor = monitor;
        this.raum = raum;
    }


}