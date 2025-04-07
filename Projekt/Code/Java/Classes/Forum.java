package Classes;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Forum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long forumId;

    @Column(nullable = false)
    private String title;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date creationDate;

    @ManyToOne
    @JoinColumn(name = "moderator", referencedColumnName = "personid", nullable = true)
    private Person moderator;

    // Getter und Setter
    public Date getCreationDate() {
        return creationDate;
    }

    public Person getModerator() {
        return moderator;
    }

    public Long getForumId() {
        return forumId;
    }

    public String getTitle() {
        return title;
    }
}
