package Classes;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Message implements Serializable {

    @Id
    private Long messageId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date creationDate;

    private String locationIp;
    private String browserUsed;

    @Lob
    private String content;

    private Integer length;

    @ManyToOne
    @JoinColumn(name = "creator", referencedColumnName = "personid", nullable = true)
    private Person creator;

    @ManyToOne
    @JoinColumn(name = "country", referencedColumnName = "placeid", nullable = true)
    private Country country;

    // Getter & Setter
    public Date getCreationDate() {
        return creationDate;
    }

    public Country getCountry() {
        return country;
    }

    public Integer getLength() {
        return length;
    }

    public Long getMessageId() {
        return messageId;
    }

    public Person getCreator() {
        return creator;
    }

    public String getBrowserUsed() {
        return browserUsed;
    }

    public String getContent() {
        return content;
    }

    public String getLocationIp() {
        return locationIp;
    }

}
