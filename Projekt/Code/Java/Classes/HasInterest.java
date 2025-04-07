package Classes;

import jakarta.persistence.*;

@Entity
@IdClass(HasInterestId.class)
public class HasInterest {

    @Id
    @ManyToOne
    @JoinColumn(name = "personid")
    private Person personId;

    @Id
    @ManyToOne
    @JoinColumn(name = "tagid")
    private Tag tagId;

    // Getter und Setter
    public Tag getTagId() {
        return tagId;
    }

    public Person getPersonId() {
        return personId;
    }
}
