package Classes;

import java.io.Serializable;
import java.util.Objects;

public class HasInterestId implements Serializable {
    private Long personId;
    private Integer tagId;

    public HasInterestId() {}

    public HasInterestId(Long personId, Integer tagId) {
        this.personId = personId;
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HasInterestId that = (HasInterestId) o;
        return Objects.equals(personId, that.personId) &&
                Objects.equals(tagId, that.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId, tagId);
    }
}
