package Classes;

import jakarta.persistence.*;

@Entity
@IdClass(WorkAtId.class)
public class WorkAt {

    @Id
    @ManyToOne
    @JoinColumn(name = "personid")
    private Person person;

    @Id
    @ManyToOne
    @JoinColumn(name = "companyid")
    private Company company;

    private int workSince;

    // Getter und Setter
    public Person getPerson() {
        return person;
    }

    public Company getCompany() {
        return company;
    }

    public int getWorkSince() {
        return workSince;
    }
}
