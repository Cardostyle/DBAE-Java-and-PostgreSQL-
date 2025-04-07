package Classes;

import java.io.Serializable;
import java.util.Objects;

public class KnowsId implements Serializable {
    private Long person1Id;
    private Long person2Id;

    public KnowsId() {}

    public KnowsId(Long person1Id, Long person2Id) {
        this.person1Id = person1Id;
        this.person2Id = person2Id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KnowsId that = (KnowsId) o;
        return Objects.equals(person1Id, that.person1Id) &&
                Objects.equals(person2Id, that.person2Id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(person1Id, person2Id);
    }
}
