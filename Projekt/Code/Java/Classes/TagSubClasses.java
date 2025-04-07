package Classes;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(TagSubClassesId.class)
public class TagSubClasses implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "parentclass", referencedColumnName = "tagclassid", nullable = false)
    private TagClass parentClass;

    @Id
    @ManyToOne
    @JoinColumn(name = "childclass", referencedColumnName = "tagclassid", nullable = false)
    private TagClass childClass;

    // Getter & Setter

    public TagClass getChildClass() {
        return childClass;
    }

    public TagClass getParentClass() {
        return parentClass;
    }
}
