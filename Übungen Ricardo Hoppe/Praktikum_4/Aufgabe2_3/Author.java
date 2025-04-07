package Aufgabe2_3;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Autor", schema = "kleinebibliothek")
public class Author {
    @Id
    @Column(name = "autorId")
    private int autorId;
    @Column(name = "nachname")
    private String lastName;
    @Column(name = "vorname")
    private String firstName;

    public Author(int autorId, String lastName, String firstName) {
        this.autorId = autorId;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public Author() {

    }

    public int getAutorId() {
        return autorId;
    }

    public void setAutorId(Integer autorId) {
        this.autorId = autorId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String toString() {
        return "Author{" +
                "autorId=" + autorId +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                '}';
    }
}
