package A2;

import jakarta.persistence.*;

@Entity
@Table(name="autor")
public class Author {
    @Id //Primär
    @Column(name="autorId")
    private int authorId;
    @Column(name="vorname")
    private String authorName;
    @Column(name="nachname")
    private String authorSurname;

    public Author(int authorId, String authorName, String authorSurname) {

        this.authorId = authorId;
        this.authorName = authorName;
        this.authorSurname = authorSurname;
    }

    public Author() {

    }

    public int getAuthorId() {
        return authorId;
    }
    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }
    public String getAuthorName() {
        return authorName;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    public String getAuthorSurname() {
        return authorSurname;
    }
    public void setAuthorSurname(String authorSurname) {
        this.authorSurname = authorSurname;
    }
}
