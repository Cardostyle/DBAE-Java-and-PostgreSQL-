package Classes;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
public class Comment extends Message implements Serializable {

    @ManyToOne
    @JoinColumn(name = "replyon", referencedColumnName = "messageid", nullable = false)
    private Message replyOn;

    // Getter & Setter

    public Message getReplyOn() {
        return replyOn;
    }
}
