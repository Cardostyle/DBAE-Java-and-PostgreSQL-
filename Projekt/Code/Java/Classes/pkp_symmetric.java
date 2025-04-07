package Classes;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "pkp_symmetric")
@IdClass(pkp_symmetricId.class) // Zusammengesetzter Primärschlüssel
public class pkp_symmetric implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "person_a", referencedColumnName = "personid") // Fremdschlüssel-Verknüpfung
    private Person personA;

    @Id
    @ManyToOne
    @JoinColumn(name = "person_b", referencedColumnName = "personid") // Fremdschlüssel-Verknüpfung
    private Person personB;

    // Getter & Setter
    public Person getPersonA() { return personA; }
    public void setPersonA(Person personA) { this.personA = personA; }

    public Person getPersonB() { return personB; }
    public void setPersonB(Person personB) { this.personB = personB; }
}
