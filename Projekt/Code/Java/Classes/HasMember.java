package Classes;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@IdClass(HasMemberId.class)
public class HasMember {

    @Id
    @ManyToOne
    @JoinColumn(name = "forumid")
    private Forum forumId;

    @Id
    @ManyToOne
    @JoinColumn(name = "personid")
    private Person personId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date joinDate;

    // Getter und Setter

    public Person getPersonId() {
        return personId;
    }

    public Forum getForumId() {
        return forumId;
    }

    public Date getJoinDate() {
        return joinDate;
    }
}
