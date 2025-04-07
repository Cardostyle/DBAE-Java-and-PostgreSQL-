package Classes;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
public class Tag implements Serializable {

    @Id
    private Integer tagId;

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

    public Integer getTagId() {
        return tagId;
    }
}
