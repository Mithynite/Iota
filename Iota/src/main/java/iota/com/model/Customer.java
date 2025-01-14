package iota.com.model;

import iota.com.annotations.*;

import java.util.Date;
import java.util.List;

@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", isPrimaryKey = true, canBeNull = false)
    private Long id;

    @Column(name = "customer_name", canBeNull = false)
    private String name;

    @Column(name = "email", canBeNull = false)
    private String email;

    @Column(name = "phone_number")
    private String phone;

    @Column(name = "gender")
    private Gender gender;

    private String genderAsString;

    @Column(name = "birthdate")
    private Date birthdate;

    @OneToMany(mappedBy = "customer") // A customer can have multiple bookings
    private List<Booking> bookings;

    public Customer() {}
    public Customer(String name, String email, String phone, Gender gender, Date birthdate) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.genderAsString = gender.name(); // Convert enum to string
        this.birthdate = birthdate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public Gender getGender() {
        return gender;
    }

    public String getGenderAsString() {
        return genderAsString;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Customer id='" + id + '\'' + ", name='" + name + '\'' + ", email='" + email + '\'' +
                ", phone='" + phone + '\'' + ", gender=" + gender + ", birthdate=" + birthdate;
    }
}
