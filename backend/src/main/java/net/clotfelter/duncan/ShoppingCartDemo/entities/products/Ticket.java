package net.clotfelter.duncan.ShoppingCartDemo.entities.products;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="ticket")
@Data
public class Ticket {
    @Id @Column(name="id")
    String id;

    @Column(name="user")
    String user;

    @Column(name="units")
    int units;

    @OneToOne
    Film film;

    @Column(columnDefinition = "TEXT")
    String purchase;
}
