package iota.com.model;

import iota.com.annotations.*;

import java.util.Date;

@Table(name = "service")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", isPrimaryKey = true, canBeNull = false)
    private Long id;

    @Column(name = "service_name", canBeNull = false)
    private String name;

    @Column(name = "price", canBeNull = false)
    private float price;

    public Service(String name, float price) {
        this.name = name;
        this.price = price;
    }
}
