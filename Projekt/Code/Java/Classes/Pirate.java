package Classes;

import jakarta.persistence.*;

@Entity
public class Pirate {

    @Id
    private String gamertag;

    @ManyToOne
    @JoinColumn(name = "personid", nullable = true)
    private Person person;

    @Column(nullable = false)
    private int gold;

    @Column(nullable = false)
    private int doubloons;

    private String bodyType;

    // Getter und Setter
    public Person getPerson() {
        return person;
    }

    public int getDoubloons() {
        return doubloons;
    }

    public int getGold() {
        return gold;
    }

    public String getBodyType() {
        return bodyType;
    }

    public String getGamertag() {
        return gamertag;
    }
}
