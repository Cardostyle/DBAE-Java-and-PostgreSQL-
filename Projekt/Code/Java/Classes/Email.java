package Classes;

import jakarta.persistence.*;

@Entity
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "personid")
    private Person person;

    @Column(unique = true, nullable = false)
    private String email;

    // Getter und Setter
    public Long getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public String getEmail() {
        return email;
    }
}
