package Classes;

import jakarta.persistence.*;

@Entity
@IdClass(FactionRelationId.class)
public class FactionRelation {

    @Id
    @ManyToOne
    @JoinColumn(name = "gamertag")
    private Pirate gamertag;

    @Id
    @ManyToOne
    @JoinColumn(name = "faction")
    private Faction faction;

    private float level;

    // Getter & Setter

    public Faction getFaction() {
        return faction;
    }

    public Pirate getGamertag() {
        return gamertag;
    }

    public float getLevel() {
        return level;
    }
}
