package Classes;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(MessageHasTagId.class)
public class MessageHasTag implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "messageid", referencedColumnName = "messageid", nullable = false)
    private Message message;

    @Id
    @ManyToOne
    @JoinColumn(name = "tagid", referencedColumnName = "tagid", nullable = false)
    private Tag tag;

    // Getter & Setter

    public Message getMessage() {
        return message;
    }

    public Tag getTag() {
        return tag;
    }
}
