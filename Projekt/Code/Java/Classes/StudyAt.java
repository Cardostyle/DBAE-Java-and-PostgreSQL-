package Classes;

import jakarta.persistence.*;

@Entity
@IdClass(StudyAtId.class)
public class StudyAt {

    @Id
    @ManyToOne
    @JoinColumn(name = "personid")
    private Person person;

    @Id
    @ManyToOne
    @JoinColumn(name = "universityid")
    private University university;

    private int classYear;

    // Getter und Setter
    public Person getPerson() {
        return person;
    }

    public int getClassYear() {
        return classYear;
    }

    public University getUniversity() {
        return university;
    }
}
