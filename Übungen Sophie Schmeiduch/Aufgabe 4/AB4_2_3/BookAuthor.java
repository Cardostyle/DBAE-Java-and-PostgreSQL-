package A2;

import jakarta.persistence.*;

@Entity
@Table(name = "buch_autor")
@IdClass(BookAuthorId.class)
public class BookAuthor {
    @Id //Primärschlüssel
    @ManyToOne
    @JoinColumn(name="buecher_buchid")
    Book book;

    @Id //2.Primärschlüssel
    @ManyToOne
    @JoinColumn(name = "autoren_autorid")
    Author author;

    public BookAuthor(Book bookID, Author authorID) {
        this.book = bookID;
        this.author = authorID;
    }

    public BookAuthor() {

    }

    public Author getAuthor() {
        return author;
    }
    public void setAuthor(Author author) {
        this.author = author;
    }
    public Book getBookID() {
        return book;
    }
    public void setBookID(Book bookID) {
        this.book = bookID;
    }
}
