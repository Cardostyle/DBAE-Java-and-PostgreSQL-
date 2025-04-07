package Classes;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
public class Post extends Message implements Serializable {

    private String language;
    private String imageFile;

    @ManyToOne
    @JoinColumn(name = "forumid", referencedColumnName = "forumid", nullable = true)
    private Forum forum;

    // Getter & Setter
    public Forum getForum() {
        return forum;
    }

    public String getLanguage() {
        return language;
    }

    public String getImageFile() {
        return imageFile;
    }
}
