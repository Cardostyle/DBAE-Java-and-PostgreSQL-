package Classes;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class WorkHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer workHistoryId;

    @ManyToOne
    @JoinColumn(name = "personid", referencedColumnName = "personid", nullable = false)
    private Person person;

    @ManyToOne
    @JoinColumn(name = "companyid", referencedColumnName = "organisationid", nullable = false)
    private Company company;

    private Integer workSince;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    // Getter & Setter
    public Person getPerson() {
        return person;
    }

    public Company getCompany() {
        return company;
    }

    public Integer getWorkHistoryId() {
        return workHistoryId;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Integer getWorkSince() {
        return workSince;
    }
}
