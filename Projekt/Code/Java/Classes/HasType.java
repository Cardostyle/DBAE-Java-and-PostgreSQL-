package Classes;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(HasTypeId.class)
public class HasType implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "tagId", referencedColumnName = "tagid", nullable = false)
    private Tag tag;

    @Id
    @ManyToOne
    @JoinColumn(name = "tagclassid", referencedColumnName = "tagclassid", nullable = false)
    private TagClass tagClass;

    // Getter & Setter

    public Tag getTag() {
        return tag;
    }

    public TagClass getTagClass() {
        return tagClass;
    }
}
