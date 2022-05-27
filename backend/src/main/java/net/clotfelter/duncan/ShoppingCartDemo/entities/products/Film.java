package net.clotfelter.duncan.ShoppingCartDemo.entities.products;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="film")
@Data
public class Film {
    @Id @Column(name="id")
    int id;

    @Column(name="price")
    double price;

    @Column(name="currency")
    String currency;

    @Column(name="name")
    String name;

    @Column(name="show_time")
    String showTime;

    @Column(name="description")
    String description;
}
