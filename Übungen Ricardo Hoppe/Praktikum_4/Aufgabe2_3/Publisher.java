package Aufgabe2_3;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "verlag", schema = "kleinebibliothek")
public class Publisher {
    @Id
    @Column(name = "verlagsId")
    private int verlagsId;
    @Column(name = "name")
    private String name;
    @Column(name = "ort")
    private String city;
    @Column(name = "plz")
    private String postalCode;
    @Column(name = "strasse")
    private String street;

    public int getVerlagsId() {
        return verlagsId;
    }

    public void setVerlagsId(int verlagsId) {
        this.verlagsId = verlagsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "Publisher{" +
                "verlagsId=" + verlagsId +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", street='" + street + '\'' +
                '}';
    }
}
