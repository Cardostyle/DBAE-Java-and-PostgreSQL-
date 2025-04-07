package Classes;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@IdClass(KnowsId.class)
public class Knows {

    @Id
    @ManyToOne
    @JoinColumn(name = "person1id")
    private Person person1Id;

    @Id
    @ManyToOne
    @JoinColumn(name = "person2id")
    private Person person2Id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date creationDate;

    // Getter und Setter
    public Date getCreationDate() {
        return creationDate;
    }

    public Person getPerson1Id() {
        return person1Id;
    }

    public Person getPerson2Id() {
        return person2Id;
    }
}
