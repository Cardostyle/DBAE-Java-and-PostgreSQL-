package A2;

import jakarta.persistence.*;


@Entity
@Table(name = "verlag")
public class Publisher {
    @Id
    @Column(name="verlagsId")
    private int publisherID;
    @Column(name="ort")
    private String city;
    @Column(name="plz")
    private String postalCode;
    @Column(name="strasse")
    private String street;
    @Column(name="name")
    private String publisherName;

    public Publisher(int publisherID, String city, String postalCode, String street, String publisherName) {
        this.publisherID = publisherID;
        this.city = city;
        this.postalCode = postalCode;
        this.street = street;
        this.publisherName = publisherName;

    }

    public Publisher() {

    }

    public int getPublisherID() {
        return publisherID;
    }
    public void setPublisherID(int publisherID) {
        this.publisherID = publisherID;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getPostalCode() {
        return postalCode;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public String getPublisherName() {
        return publisherName;
    }
    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    @Override
    public String toString() {
        return "Verlag [publisherID=" + publisherID + ", city=" + city + ", postalCode=" +
                postalCode + ", street=" + street + ", publisherName=" + publisherName + "]";
    }

}
