package Classes;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String gender;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date birthday;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date creationDate;

    @Column(nullable = false)
    private String locationIp;

    private String browserUsed;

    @ManyToOne
    @JoinColumn(name = "city", referencedColumnName = "placeid", nullable = true)
    private City city;

    @ElementCollection
    private List<String> language;

    // Getter und Setter

    public City getCity() {
        return city;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getGender() {
        return gender;
    }

    public String getLastName() {
        return lastName;
    }

    public List<String> getLanguage() {
        return language;
    }

    public Long getPersonId() {
        return personId;
    }

    public String getBrowserUsed() {
        return browserUsed;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getLocationIp() {
        return locationIp;
    }

}
