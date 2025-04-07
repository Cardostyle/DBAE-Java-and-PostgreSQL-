package Classes;

import java.io.Serializable;
import java.util.Objects;

public class StudyAtId implements Serializable {

    private Long person;
    private Integer university;

    public StudyAtId() {}

    public StudyAtId(Long person, Integer university) {
        this.person = person;
        this.university = university;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudyAtId)) return false;
        StudyAtId that = (StudyAtId) o;
        return Objects.equals(person, that.person) &&
                Objects.equals(university, that.university);
    }

    @Override
    public int hashCode() {
        return Objects.hash(person, university);
    }
}
