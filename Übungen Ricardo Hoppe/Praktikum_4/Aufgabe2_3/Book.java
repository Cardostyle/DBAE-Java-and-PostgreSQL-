package Aufgabe2_3;

import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "buch", schema = "kleinebibliothek")
public class Book {
    @Id
    @Column (name = "buchid")
    private int buchId;
    @Column(name = "titel")
    private String title;
    @Column(name = "jahr")
    private int year;

    @ManyToOne
    @JoinColumn(name = "verlag_id")
    private Publisher publisher;

    @OneToMany(mappedBy = "book")
    private List<BookAuthor> bookAuthors;

    public Book(int buchId, String title, int year, Publisher publisher) {
        this.buchId = buchId;
        this.title = title;
        this.year = year;
        this.publisher = publisher;
    }

    public Book() {

    }

    public Integer getBuchId() {
        return buchId;
    }

    public void setBuchId(int buchId) {
        this.buchId = buchId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public String toString() {
        // Autoren als String-Liste zusammenfassen
        String authors;

        if (bookAuthors != null && !bookAuthors.isEmpty()) {
            StringBuilder authorNames = new StringBuilder();
            for (BookAuthor ba : bookAuthors) {
                authorNames.append(ba.getAuthor().getFirstName())
                        .append(" ")
                        .append(ba.getAuthor().getLastName())
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

        return "Book{" +
                "buchId=" + buchId +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", publisher=" + (publisher != null ? publisher.getName() : "None") +
                ", authors=" + authors +
                '}';
    }

}
