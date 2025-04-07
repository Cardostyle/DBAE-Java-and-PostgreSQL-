package Aufgabe1;

import jakarta.persistence.*;

@Entity
@Table(name = "desktop")
@DiscriminatorValue("Desktop")
public class Desktop extends Rechner {
    String monitor;
    String raum;

    public Desktop(int inventarnummer,String besitzer, int festplatte,String monitor, String raum){
        super(inventarnummer,besitzer,festplatte);
        this.monitor = monitor;
        this.raum = raum;
    }

    public Desktop() {

    }
}
