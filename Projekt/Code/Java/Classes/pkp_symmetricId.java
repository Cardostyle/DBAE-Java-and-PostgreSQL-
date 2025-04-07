package Classes;

import java.io.Serializable;
import java.util.Objects;

public class pkp_symmetricId implements Serializable {

    private Long personA;
    private Long personB;

    public pkp_symmetricId() {}

    public pkp_symmetricId(Long personA, Long personB) {
        this.personA = personA;
        this.personB = personB;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        pkp_symmetricId that = (pkp_symmetricId) o;
        return Objects.equals(personA, that.personA) &&
                Objects.equals(personB, that.personB);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personA, personB);
    }
}
