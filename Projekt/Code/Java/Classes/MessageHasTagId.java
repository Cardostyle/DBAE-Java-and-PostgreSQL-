package Classes;

import java.io.Serializable;
import java.util.Objects;

public class MessageHasTagId implements Serializable {

    private Long message;
    private Integer tag;

    public MessageHasTagId() {}

    public MessageHasTagId(Long message, Integer tag) {
        this.message = message;
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageHasTagId)) return false;
        MessageHasTagId that = (MessageHasTagId) o;
        return Objects.equals(message, that.message) &&
                Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, tag);
    }
}
