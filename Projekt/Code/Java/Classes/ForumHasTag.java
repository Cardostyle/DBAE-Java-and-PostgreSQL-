package Classes;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(ForumHasTagId.class)
public class ForumHasTag implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "forumid", referencedColumnName = "forumid", nullable = false)
    private Forum forumId;

    @Id
    @ManyToOne
    @JoinColumn(name = "tagid", referencedColumnName = "tagid", nullable = false)
    private Tag tagId;

    // Getter & Setter
    public Tag getTagId() {
        return tagId;
    }

    public Forum getForumId() {
        return forumId;
    }
}
