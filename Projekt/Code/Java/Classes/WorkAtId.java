package Classes;

import java.io.Serializable;
import java.util.Objects;

public class WorkAtId implements Serializable {

    private Long person;
    private Integer company;

    public WorkAtId() {}

    public WorkAtId(Long person, Integer company) {
        this.person = person;
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkAtId)) return false;
        WorkAtId that = (WorkAtId) o;
        return Objects.equals(person, that.person) &&
                Objects.equals(company, that.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(person, company);
    }
}
