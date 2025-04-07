package Classes;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@IdClass(LikesId.class)
public class Likes {

    @Id
    @ManyToOne
    @JoinColumn(name = "personid")
    private Person personId;

    @Id
    @ManyToOne
    @JoinColumn(name = "messageid")
    private Message messageId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date creationDate;

    // Getter und Setter

    public Person getPersonId() {
        return personId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Message getMessageId() {
        return messageId;
    }
}
