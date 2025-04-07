package Classes;

import java.io.Serializable;
import java.util.Objects;

public class HasMemberId implements Serializable {
    private Long forumId;
    private Long personId;

    public HasMemberId() {}

    public HasMemberId(Long forumId, Long personId) {
        this.forumId = forumId;
        this.personId = personId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HasMemberId that = (HasMemberId) o;
        return Objects.equals(forumId, that.forumId) &&
                Objects.equals(personId, that.personId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(forumId, personId);
    }
}
