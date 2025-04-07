package Classes;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
public class TagClass implements Serializable {

    @Id
    private Integer tagClassId;

    @Column(nullable = false)
    private String name;

    private String url;

    // Getter & Setter
    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Integer getTagClassId() {
        return tagClassId;
    }
}
