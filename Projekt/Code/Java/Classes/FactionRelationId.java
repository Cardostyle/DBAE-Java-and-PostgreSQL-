package Classes;

import java.io.Serializable;
import java.util.Objects;

public class FactionRelationId implements Serializable {
    private String gamertag;
    private String faction;

    public FactionRelationId() {}

    public FactionRelationId(String gamertag, String faction) {
        this.gamertag = gamertag;
        this.faction = faction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FactionRelationId that = (FactionRelationId) o;
        return Objects.equals(gamertag, that.gamertag) &&
                Objects.equals(faction, that.faction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gamertag, faction);
    }
}
