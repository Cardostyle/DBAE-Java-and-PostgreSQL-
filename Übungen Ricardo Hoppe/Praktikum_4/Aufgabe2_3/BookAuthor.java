package Aufgabe2_3;

import jakarta.persistence.*;


@Entity
@Table(name = "buch_autor", schema = "kleinebibliothek")
@IdClass(BookAuthorId.class)
public class BookAuthor {
    @Id
    @ManyToOne
    @JoinColumn(name = "buecher_buchid")
    private Book book;

    @Id
    @ManyToOne
    @JoinColumn(name = "autoren_autorid")
    private Author author;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "BookAuthor{" +
                "book=" + (book != null ? book.getTitle() : "None") +
                ", author=" + (author != null ? author.getFirstName() + " " + author.getLastName() : "None") +
                '}';
    }
}
