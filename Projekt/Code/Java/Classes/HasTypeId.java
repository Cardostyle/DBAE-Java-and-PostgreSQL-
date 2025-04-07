package Classes;

import java.io.Serializable;
import java.util.Objects;

public class HasTypeId implements Serializable {

    private Integer tag;
    private Integer tagClass;

    public HasTypeId() {}

    public HasTypeId(Integer tag, Integer tagClass) {
        this.tag = tag;
        this.tagClass = tagClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HasTypeId)) return false;
        HasTypeId that = (HasTypeId) o;
        return Objects.equals(tag, that.tag) &&
                Objects.equals(tagClass, that.tagClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, tagClass);
    }
}
