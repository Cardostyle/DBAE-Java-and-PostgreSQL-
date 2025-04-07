package A2;

import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name="buch")
public class Book {

    @Id
    @Column(name="buchid")
    private int bookID;
    @Column(name="jahr")
    private int year;
    @Column(name="titel")
    private String bookName;
    @ManyToOne
    @JoinColumn(name="verlag_id")
    private Publisher publisher;

    @OneToMany(mappedBy = "book") //rausziehen der Autoren aus Buch
    private List<BookAuthor> bookAuthors;

    public Book(int bookID, int year, String bookName, Publisher publisherID) {
        this.bookID = bookID;
        this.year = year;
        this.bookName = bookName;
        this.publisher = publisherID;
    }

    public Book() {

    }

    public int getBookID() {
        return bookID;
    }
    public void setBookID(int bookID) {
        this.bookID = bookID;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public String getBookName() {
        return bookName;
    }
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
    public Publisher getPublisher() {
        return publisher;
    }
    public void setPublisher(Publisher publisherID) {
        this.publisher = publisherID;
    }

@Override
    public String toString() {
// Autoren als String-Liste zusammenfassen
    String authors;

    if (bookAuthors != null && !bookAuthors.isEmpty()) {
        StringBuilder authorNames = new StringBuilder();
        for (BookAuthor ba : bookAuthors) {
            authorNames.append(ba.getAuthor().getAuthorName())
                    .append(" ")
                    .append(ba.getAuthor().getAuthorSurname())
                    .append(", ");
        }
        // Entfernen des letzten Kommas und Leerzeichens
        if (authorNames.length() > 0) {
            authorNames.setLength(authorNames.length() - 2);
        }
        authors = authorNames.toString();
    } else {
        authors = "No authors available";
    }


        return "Buch [Buch Id=" + bookID + ", " +
                "Jahr=" + year + ", " +
                "Titel=" + bookName + ", " +
                "Verlag=" + (publisher != null ? publisher.getPublisherName(): "None") + ", " +
                "Autor=" + authors + "]";

}

}
