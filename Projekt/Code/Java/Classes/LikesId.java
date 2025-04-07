package Classes;

import java.io.Serializable;
import java.util.Objects;

public class LikesId implements Serializable {
    private Long personId;
    private Long messageId;

    public LikesId() {}

    public LikesId(Long personId, Long messageId) {
        this.personId = personId;
        this.messageId = messageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LikesId that = (LikesId) o;
        return Objects.equals(personId, that.personId) &&
                Objects.equals(messageId, that.messageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId, messageId);
    }
}
