package Classes;

import java.io.Serializable;
import java.util.Objects;

public class TagSubClassesId implements Serializable {

    private Integer parentClass;
    private Integer childClass;

    public TagSubClassesId() {}

    public TagSubClassesId(Integer parentClass, Integer childClass) {
        this.parentClass = parentClass;
        this.childClass = childClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagSubClassesId)) return false;
        TagSubClassesId that = (TagSubClassesId) o;
        return Objects.equals(parentClass, that.parentClass) &&
                Objects.equals(childClass, that.childClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parentClass, childClass);
    }
}
