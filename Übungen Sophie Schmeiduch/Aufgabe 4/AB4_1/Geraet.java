package AB4;

import jakarta.persistence.*;

@Entity
@Table(name="Geraet")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) //t-p-c-c
//@Inheritance(strategy = InheritanceType.JOINED) //t-p-s
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //t-p-c-h
@DiscriminatorColumn(name = "gereat_typ", discriminatorType = DiscriminatorType.STRING) //t-p-c-h
public class Geraet {
    @Id //t-p-c-c
    @GeneratedValue(strategy = GenerationType.AUTO)
     int inventarnummer;

    public Geraet(int inventarnummer) {
        this.inventarnummer = inventarnummer;
    }

    public Geraet() {
        
    }
}
